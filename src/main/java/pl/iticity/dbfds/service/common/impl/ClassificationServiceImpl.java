package pl.iticity.dbfds.service.common.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.repository.common.ClassificationRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.ClassificationHelper;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainHelper;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class ClassificationServiceImpl extends AbstractScopedService<Classification, String, ClassificationRepository> implements ClassificationService {

    private static final Logger logger = Logger.getLogger(ClassificationServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DomainHelper domainHelper;

    @Autowired
    private ClassificationHelper classificationHelper;

    private Map<String,List<ClassificationType>> models;

    @PostConstruct
    public void postConstruct(){
        models = Maps.newHashMap();
        models.put("product",Lists.<ClassificationType>newArrayList(ClassificationType.PRODUCT_MODEL,ClassificationType.PRODUCT_FAMILY,ClassificationType.PRODUCT_GROUP,ClassificationType.PRODUCT_LINE));
        models.put("project",Lists.<ClassificationType>newArrayList(ClassificationType.PROJECT));
        models.put("quotation",Lists.<ClassificationType>newArrayList(ClassificationType.QUOTATION));
        models.put("document",Lists.<ClassificationType>newArrayList(ClassificationType.values()));
    }

    public List<Classification> findByDomain(Domain domain, boolean onlyActive) {
        if (domain == null) {
            domain = PrincipalUtils.getCurrentDomain();
        }
        if (onlyActive) {
            return repo.findByDomainAndActiveIsTrueAndRemovedIsFalse(domain);
        }
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    public List<Classification> findByDomain(Domain domain, boolean onlyActive, String without) {
        if (domain == null) {
            domain = PrincipalUtils.getCurrentDomain();
        }
        return repo.findByDomainAndIdNotAndRemovedIsFalse(domain, without);
    }

    public List<Classification> findByDomainForClassification(String domainId, boolean onlyActive, String forClassification) {
        Domain domain = domainHelper.fetchDomain(domainId);
        Classification classification = null;
        if (!StringUtils.isEmpty(forClassification)) {
            classification = repo.findOne(forClassification);
        }
        if (classification == null) {
            List<Classification> classifications = repo.findByDomainAndRemovedIsFalse(domain);
            for (Classification c : classifications) {
                c.setAssigned(isAssigned(c));
            }
            return classifications;
        } else {
            List<Classification> classifications = repo.findByDomainAndIdNotAndRemovedIsFalse(domain, classification.getId());
            final Classification finalClassification = classification;
            Iterables.removeIf(classifications, new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification thisClassification) {
                    return violatesCircularDependency(finalClassification, thisClassification);
                }
            });
            for (Classification c : classifications) {
                c.setAssigned(isAssigned(c));
            }
            return classifications;
        }
    }

    public List<Classification> addClassification(final Classification classification, String domainId) {
        Domain domain = domainHelper.fetchDomain(domainId);
        Classification c = repo.findOne(classification.getId());
        if (c == null) {
            classification.setId(null);
        }else if(c.getModelId()!=null && c.getModelClazz()!=null){
            classification.setModelId(c.getModelId());
            classification.setModelClazz(c.getModelClazz());
            classificationHelper.updateModelClass(classification);
        }
        classification.setDomain(domain);
        classification.setActive(true);
        classification.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        if (classification.getParents() != null) {
            Iterables.removeIf(classification.getParents(), new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification thisClassification) {
                    return violatesCircularDependency(classification, thisClassification);
                }
            });
        }
        repo.save(classification);
        return findByDomain(domain, false);
    }

    private boolean violatesCircularDependency(Classification thisC, Classification fromList) {
        if (fromList.getParents() == null) {
            return false;
        } else if (fromList.getParents().contains(thisC)) {
            return true;
        } else {
            for (Classification c : fromList.getParents()) {
                if (violatesCircularDependency(thisC, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Classification> transform(List<Classification> classifications) {
        return Lists.newArrayList(Iterables.transform(classifications, new Function<Classification, Classification>() {
            @Nullable
            @Override
            public Classification apply(@Nullable Classification classification) {
                return repo.findByDomainAndClassificationId(PrincipalUtils.getCurrentDomain(), classification.getClassificationId());
            }
        }));
    }

    public List<Classification> toggleClassification(String id, boolean toggle) {
        Classification classification = repo.findOne(id);
        AuthorizationProvider.assertRole(Role.ADMIN, classification.getDomain());
        classification.setActive(toggle);
        classification.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(classification);
        return findByDomain(classification.getDomain(), false);
    }

    public boolean exists(String clId, String id, String domainId) {
        Domain domain = domainHelper.fetchDomain(domainId);
        Classification c = repo.findByDomainAndClassificationId(domain, clId);
        if (c == null) {
            return false;
        }
        if (id != null && id.equals(c.getId())) {
            return false;
        }
        return true;
    }

    public List<Classification> deleteClassification(final String id) {
        Classification classification = repo.findOne(id);
        AuthorizationProvider.assertRole(Role.ADMIN, classification.getDomain());
        if (!isAssigned(classification)) {
            List<Classification> classifications = repo.findByDomainAndRemovedIsFalse(classification.getDomain());
            for (Classification c : classifications) {
                if (c.getParents() != null) {
                    Iterables.removeIf(c.getParents(), new Predicate<Classification>() {
                        @Override
                        public boolean apply(@Nullable Classification classification) {
                            return id != null && id.equals(classification.getId());
                        }
                    });
                    c.setPrincipal(PrincipalUtils.getCurrentPrincipal());
                    c.setDomain(PrincipalUtils.getCurrentDomain());
                    repo.save(c);
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(classification.getModelClazz()) && org.apache.commons.lang.StringUtils.isNotEmpty(classification.getModelId())) {
                Object o = classificationHelper.getModel(classification);
                if(o!=null){
                    mongoTemplate.remove(o);
                }
            }
            repo.delete(classification);
        }
        return findByDomain(classification.getDomain(), false);
    }

    @Override
    public List<Classification> findForModel(Domain domain,String model) {;
        List<ClassificationType> types = models.get(model);
        List<Classification> classifications = repo.findByDomainAndActiveIsTrueAndRemovedIsFalseAndTypeIn(domain,types);
        if("document".equals(model)){
            classifications.addAll(repo.findByDomainAndActiveIsTrueAndRemovedIsFalseAndTypeIsNull(domain));
        }
        return classifications;
    }

    @Override
    public boolean isAssigned(Classification classification) {
        for (ClassifiableRepository cr : applicationContext.getBeansOfType(ClassifiableRepository.class).values()) {
            List list = cr.findByClassification(classification);
            if (list != null && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Classification findByModelIdAndModelClazz(String id, String clazz) {
        return repo.findByModelIdAndModelClazz(id,clazz);
    }

}
