package pl.iticity.dbfds.service.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.BaseModel;
import pl.iticity.dbfds.model.common.Bond;
import pl.iticity.dbfds.model.common.BondType;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.common.ObjectType;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.Revision;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.common.BondRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.BondService;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BondServiceImpl extends AbstractScopedService<Bond, String, BondRepository> implements BondService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ClassificationService classificationService;

    private Map<Class, ObjectType> objectTypes;

    @PostConstruct
    public void postConstruct() {
        objectTypes = Maps.newHashMap();
        objectTypes.put(ProductInformationCarrier.class, ObjectType.PRODUCT);
        objectTypes.put(ProjectInformationCarrier.class, ObjectType.PROJECT);
        objectTypes.put(DocumentInformationCarrier.class, ObjectType.DOCUMENT);
        objectTypes.put(QuotationInformationCarrier.class, ObjectType.QUOTATION);
    }

    private Class getClassFromObjectType(final ObjectType type) {
        return Iterables.find(objectTypes.entrySet(), new Predicate<Map.Entry<Class, ObjectType>>() {
            @Override
            public boolean apply(@Nullable Map.Entry<Class, ObjectType> classObjectTypeEntry) {
                return type.equals(classObjectTypeEntry.getValue());
            }
        }).getKey();
    }

    @Override
    public Bond createBond(String aId, Class<? extends BaseModel> aClass, boolean aRevision, String bId, Class<? extends BaseModel> bClass, boolean bRevision, BondType bondType) {
        Bond bond = new Bond();
        if (StringUtils.isEmpty(aId) || aClass == null || StringUtils.isEmpty(bId) || bClass == null || bondType == null) {
            throw new IllegalArgumentException();
        }
        BaseModel a = resolveObject(aId, aClass);
        bond.setFirstId(a.getId());
        bond.setFirstType(resolveObjectType(a.getClass()));
        if (aRevision && DocumentInformationCarrier.class.equals(a.getClass())) {
            DocumentInformationCarrier dic = (DocumentInformationCarrier) a;
            if (dic.getRevision() != null) {
                bond.setFirstRevision(dic.getRevision().getEffective());
            }
        }

        BaseModel b = resolveObject(bId, bClass);
        bond.setSecondId(b.getId());
        bond.setSecondType(resolveObjectType(b.getClass()));
        if (bRevision && DocumentInformationCarrier.class.equals(b.getClass())) {
            DocumentInformationCarrier dic = (DocumentInformationCarrier) b;
            if (dic.getRevision() != null) {
                bond.setSecondRevision(dic.getRevision().getEffective());
            }
        }

        bond.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        bond.setDomain(PrincipalUtils.getCurrentDomain());
        bond.setBondType(bondType);
        bond.setCreationDate(new Date());

        return repo.save(bond);
    }

    private BaseModel resolveObject(String id, Class<? extends BaseModel> clazz) {
        BaseModel model = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
        if (model == null) {
            throw new IllegalArgumentException();
        }
        return model;
    }

    private ObjectType resolveObjectType(Class clazz) {
        ObjectType type = objectTypes.get(clazz);
        if (type == null) {
            throw new IllegalArgumentException();
        }
        return type;
    }

    @Override
    public void deleteBond(String id) {
        if (StringUtils.isEmpty(id) || repo.findOne(id) == null) {
            throw new IllegalArgumentException();
        }
        Bond bond = repo.findOne(id);
        if (bond == null) {
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.isInDomain(bond.getDomain());
        repo.delete(bond);
    }

    @Override
    public List<Bond> findBondsForObject(final String oId, Class oClass, final List<ObjectType> includes) {
        if (StringUtils.isEmpty(oId) || oClass == null) {
            throw new IllegalArgumentException();
        }
        ObjectType type = objectTypes.get(oClass);
        if (type == null) {
            throw new IllegalArgumentException();
        }
        List<Bond> firstBonds = Lists.newArrayList();
        firstBonds.addAll(repo.findByFirstTypeAndFirstIdOrderByCreationDateAsc(type, oId));
        if (includes != null) {
            Iterables.removeIf(firstBonds, new Predicate<Bond>() {
                @Override
                public boolean apply(@Nullable Bond bond) {
                    return !includes.contains(bond.getSecondType());
                }
            });
        }

        List<Bond> secondBonds = Lists.newArrayList();
        secondBonds.addAll(repo.findBySecondTypeAndSecondIdOrderByCreationDateAsc(type, oId));
        if (includes != null) {
            Iterables.removeIf(secondBonds, new Predicate<Bond>() {
                @Override
                public boolean apply(@Nullable Bond bond) {
                    return !includes.contains(bond.getFirstType());
                }
            });
        }

        List<Bond> bonds = Lists.newArrayList();
        bonds.addAll(firstBonds);
        bonds.addAll(secondBonds);

        if (bonds.size() > 0) {
            AuthorizationProvider.isInDomain(bonds.get(0).getDomain());
        }
        return bonds;
    }

    @Override
    public void deleteBondsOfObject(String oId, Class oClass) {
        if (StringUtils.isEmpty(oId) || oClass == null) {
            throw new IllegalArgumentException();
        }
        List<Bond> bonds = findBondsForObject(oId, oClass, null);
        for (Bond b : bonds) {
            AuthorizationProvider.isInDomain(b.getDomain());
            deleteBond(b.getId());
        }
    }

    @Override
    public BaseModel findObjectForLink(String linkId, boolean first, boolean classification) {
        if (StringUtils.isEmpty(linkId)) {
            throw new IllegalArgumentException();
        }
        Bond bond = repo.findOne(linkId);
        if (bond == null) {
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.isInDomain(bond.getDomain());
        String id = null;
        Class clazz = null;
        String revision = null;
        if (first) {
            id = bond.getFirstId();
            clazz = getClassFromObjectType(bond.getFirstType());
            revision = bond.getFirstRevision();
        } else {
            id = bond.getSecondId();
            clazz = getClassFromObjectType(bond.getSecondType());
            revision = bond.getSecondRevision();
        }
        BaseModel model = null;
        if (StringUtils.isEmpty(revision) && !DocumentInformationCarrier.class.equals(clazz)) {
            model = (BaseModel) mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
            if (classification) {
                Classification c = classificationService.findByModelIdAndModelClazz(id, clazz.getName());
                if (c == null) {
                    try {
                        Method m = clazz.getMethod("getClassification");
                        c = (Classification) m.invoke(model);
                    } catch (NoSuchMethodException e) {
                        throw new IllegalArgumentException();
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException();
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException();
                    }
                }
                model = c;
            }
        } else if (StringUtils.isEmpty(revision) && DocumentInformationCarrier.class.equals(clazz)) {
            model = (BaseModel) mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
        } else if (StringUtils.isNotEmpty(revision) && DocumentInformationCarrier.class.equals(clazz)) {
            DocumentInformationCarrier dic = (DocumentInformationCarrier) mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
            if (dic.getRevision() != null && revision.equals(dic.getRevision().getEffective())) {
                model = dic;
            } else if (dic.getRevisions() != null && !dic.getRevisions().isEmpty()) {
                for (Revision rev : dic.getRevisions()) {
                    if (revision.equals(rev.getRevision().getEffective())) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            model = mapper.readValue(rev.getData(), DocumentInformationCarrier.class);
                        } catch (IOException e) {
                            throw new IllegalArgumentException();
                        }
                        break;
                    }
                }
                if (model == null) {
                    model = dic;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (model == null) {
            deleteBond(bond.getId());
            throw new IllegalArgumentException();
        }
        return model;
    }
}
