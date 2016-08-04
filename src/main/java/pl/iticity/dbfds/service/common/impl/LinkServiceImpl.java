package pl.iticity.dbfds.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.LinkType;
import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.service.common.LinkService;
import pl.iticity.dbfds.service.document.DocumentService;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocumentService documentService;

    @Override
    public List<Link> createLink(String id,Class<? extends Linkable> clazz, DocumentInfo doc) {
        doc = documentService.findById(doc.getId());
        Linkable linkable = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)),clazz);
        Link link = new Link(doc, LinkType.LINK);
        if(!linkable.getLinks().contains(link)){
            linkable.getLinks().add(link);
        }
        mongoTemplate.save(linkable);
        return linkable.getLinks();
    }

    @Override
    public List<Link> deleteLink(String id,Class<? extends Linkable> clazz, Link link) {
        Linkable linkable = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)),clazz);
        linkable.getLinks().remove(link);
        mongoTemplate.save(linkable);
        return linkable.getLinks();
    }
}

