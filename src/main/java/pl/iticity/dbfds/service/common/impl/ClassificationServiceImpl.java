package pl.iticity.dbfds.service.common.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.repository.common.ClassificationRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class ClassificationServiceImpl extends AbstractScopedService<Classification,String,ClassificationRepository> implements ClassificationService {

    @Autowired
    private ApplicationContext applicationContext;

    public List<Classification> findByDomain(Domain domain, boolean onlyActive){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        if(onlyActive){
            return repo.findByDomainAndActiveIsTrueAndRemovedIsFalse(domain);
        }
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    public List<Classification> findByDomain(Domain domain, boolean onlyActive, String without){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        return repo.findByDomainAndIdNotAndRemovedIsFalse(domain,without);
    }

    public List<Classification> findByDomainForClassification(Domain domain, boolean onlyActive, String forClassification){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        Classification classification = null;
        if(!StringUtils.isEmpty(forClassification)){
            classification = repo.findOne(forClassification);
        }
        if(classification == null){
            List<Classification> classifications = repo.findByDomainAndRemovedIsFalse(domain);
            for (Classification c : classifications) {
                c.setAssigned(isAssigned(c));
            }
            return classifications;
        }else{
            List<Classification> classifications = repo.findByDomainAndIdNotAndRemovedIsFalse(domain,classification.getId());
            final Classification finalClassification = classification;
            Iterables.removeIf(classifications, new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification thisClassification) {
                    return violatesCircularDependency(finalClassification,thisClassification);
                }
            });
            for(Classification c : classifications){
                c.setAssigned(isAssigned(c));
            }
            return classifications;
        }
    }

    public List<Classification> addClassification(final Classification classification, Domain domain){
        if(repo.findOne(classification.getId())==null){
           classification.setId(null);
        }
        classification.setDomain(domain);
        classification.setActive(true);
        classification.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        if(classification.getParents()!=null){
            Iterables.removeIf(classification.getParents(), new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification thisClassification) {
                    return violatesCircularDependency(classification,thisClassification);
                }
            });
        }
        repo.save(classification);
        return findByDomain(domain,false);
    }

    private boolean violatesCircularDependency(Classification thisC, Classification fromList){
        if(fromList.getParents()==null){
            return false;
        }else if(fromList.getParents().contains(thisC)) {
            return true;
        }else{
            for(Classification c : fromList.getParents()){
              if(violatesCircularDependency(thisC,c)){
                  return true;
              }
            }
        }
        return false;
    }

    private List<Classification> transform(List<Classification> classifications){
        return Lists.newArrayList(Iterables.transform(classifications, new Function<Classification, Classification>() {
            @Nullable
            @Override
            public Classification apply(@Nullable Classification classification) {
                return repo.findByDomainAndClassificationId(PrincipalUtils.getCurrentDomain(),classification.getClassificationId());
            }
        }));
    }

    public List<Classification> toggleClassification(String id, boolean toggle){
        Classification classification = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,classification.getDomain());
        classification.setActive(toggle);
        classification.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(classification);
        return findByDomain(classification.getDomain(),false);
    }

    public boolean exists(String clId, String id){
        Classification c = repo.findByDomainAndClassificationId(PrincipalUtils.getCurrentDomain(),clId);
        if(c==null){
            return false;
        }
        if(id!=null && id.equals(c.getId())){
            return false;
        }
        return true;
    }

    public List<Classification> deleteClassification(String id){
        Classification classification = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,classification.getDomain());
        if(!isAssigned(classification)) {
            repo.delete(classification);
        }
        return findByDomain(classification.getDomain(),false);
    }

    @Override
    public List<Classification> findForProduct() {
        List<String> types = Lists.newArrayList("Product Line","Product Family","Product Group","Product Model");
        return repo.findByDomainAndActiveIsTrueAndRemovedIsFalseAndTypeIn(PrincipalUtils.getCurrentDomain(),types);
    }

    @Override
    public boolean isAssigned(Classification classification) {
        for(ClassifiableRepository cr : applicationContext.getBeansOfType(ClassifiableRepository.class).values()){
            List list = cr.findByClassification(classification);
            if(list!=null && !list.isEmpty()){
                return true;
            }
        }
        return false;
    }

}
