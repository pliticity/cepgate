package pl.iticity.dbfds.service.common.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.model.common.Link;
import pl.iticity.dbfds.model.common.LinkType;
import pl.iticity.dbfds.model.common.LinkedObjectType;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.common.LinkRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.LinkService;
import pl.iticity.dbfds.service.document.DocumentService;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Service
public class LinkServiceImpl extends AbstractService<Link,String,LinkRepository> implements LinkService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LinkRepository linkRepository;

    private Map<LinkedObjectType, Class> linkable;

    @PostConstruct
    public void postConstruct() {
        linkable = Maps.newHashMap();
        linkable.put(LinkedObjectType.QIC, QuotationInformationCarrier.class);
        linkable.put(LinkedObjectType.DIC, DocumentInformationCarrier.class);
        linkable.put(LinkedObjectType.PIC, ProjectInformationCarrier.class);
        linkable.put(LinkedObjectType.PJC, ProjectInformationCarrier.class);
    }

    @Override
    public Set<Link> createLink(String id, Class<? extends Linkable> clazz, String objectId, Class<? extends Scoped> objectClass, LinkType linkType) {
        if (StringUtils.isEmpty(id) || clazz == null || objectClass == null || StringUtils.isEmpty(objectId)) {
            throw new IllegalArgumentException();
        }
        Linkable linkable = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
        if (linkable == null) {
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.isInDomain(linkable.getDomain());
        Scoped scoped = mongoTemplate.findOne(Query.query(Criteria.where("id").is(objectId)), objectClass);
        if (scoped == null) {
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.isInDomain(scoped.getDomain());
        Link link = new Link();
        link.setObjectId(objectId);
        link.setObjectType(resolveObjectType(objectClass));
        link.setLinkType(linkType);

        linkRepository.save(link);
        if (linkable.getLinks() == null) {
            linkable.setLinks(Sets.<Link>newHashSet());
        }
        linkable.getLinks().add(link);
        mongoTemplate.save(linkable);
        return linkable.getLinks();
    }

    private LinkedObjectType resolveObjectType(final Class objectClass) {
        Map.Entry<LinkedObjectType,Class> en = Iterables.find(linkable.entrySet(), new Predicate<Map.Entry<LinkedObjectType, Class>>() {
            @Override
            public boolean apply(@Nullable Map.Entry<LinkedObjectType, Class> entry) {
                return entry.getValue().equals(objectClass);
            }
        },null);
        if(en==null){
            throw new IllegalArgumentException();
        }
        return en.getKey();
    }

    @Override
    public Set<Link> deleteLink(String id, Class<? extends Linkable> clazz, final Link link) {
        if (StringUtils.isEmpty(id) || clazz == null || link == null || StringUtils.isEmpty(link.getId())) {
            throw new IllegalArgumentException();
        }
        Linkable linkable = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), clazz);
        if (linkable == null) {
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.isInDomain(linkable.getDomain());
        boolean removed = Iterables.removeIf(linkable.getLinks(), new Predicate<Link>() {
            @Override
            public boolean apply(@Nullable Link thatLink) {
                return link.getId().equals(thatLink.getId());
            }
        });
        if (removed) {
            mongoTemplate.save(linkable);
            linkRepository.delete(link);
        }
        return linkable.getLinks();
    }

    @Override
    public Linkable getLinkObject(String id) {
        if (StringUtils.isEmpty(id) || linkRepository.findOne(id) == null) {
            throw new IllegalArgumentException();
        }
        Link link = linkRepository.findOne(id);
        Linkable object = (Linkable) mongoTemplate.findOne(Query.query(Criteria.where("id").is(link.getObjectId())), linkable.get(link.getObjectType()));
        return object;
    }

}

