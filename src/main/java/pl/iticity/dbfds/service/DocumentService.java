package pl.iticity.dbfds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.mixins.AutoCompleteDocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.DocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.NewDocumentInfoMixIn;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class DocumentService extends AbstractService<DocumentInfo, DocumentInfoRepository> {

    private static final Logger logger = Logger.getLogger(DocumentService.class);

    @Autowired
    private DomainService domainService;

    @Autowired
    private FileService fileService;

    public String documentsToJson(List<DocumentInfo> documents) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInfo.class, DocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documents);
    }

    public String newDocumentToJson(DocumentInfo documentInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInfo.class, NewDocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documentInfo);
    }

    public void favourite(String id, boolean val) {
        final Principal current = PrincipalUtils.getCurrentPrincipal();
        DocumentInfo info = repo.findOne(id);
        if (info.getFavourites() != null) {
            DocumentFavourite docFav = Iterables.find(info.getFavourites(), new com.google.common.base.Predicate<DocumentFavourite>() {
                @Override
                public boolean apply(@Nullable DocumentFavourite documentFavourite) {
                    return current.getId().equals(documentFavourite.getPrincipal().getId());
                }
            }, null);
            if (docFav != null && !val) {
                info.getFavourites().remove(docFav);
            } else if (docFav == null && val) {
                docFav = new DocumentFavourite();
                docFav.setPrincipal(current);
                info.getFavourites().add(docFav);
            }
        } else if (val) {
            info.setFavourites(Lists.<DocumentFavourite>newArrayList());
            DocumentFavourite docFav = new DocumentFavourite();
            docFav.setPrincipal(current);
            info.getFavourites().add(docFav);
        }
        repo.save(info);
    }

    public DocumentInfo copyDocument(String docId, final List<String> files) throws FileNotFoundException {
        DocumentInfo documentInfo = repo.findOne(docId);
        DocumentInfo copy = documentInfo.clone();
        copy.getLinks().add(new Link(documentInfo,LinkType.COPY_FROM));
        copy.setMasterDocumentNumber(getNextMasterDocumentNumber());
        copy.setDocumentNumber(String.valueOf(copy.getMasterDocumentNumber()));
        List<FileInfo> filesToCopy = Lists.newArrayList(Iterables.filter(documentInfo.getFiles(), new com.google.common.base.Predicate<FileInfo>() {
            @Override
            public boolean apply(@Nullable FileInfo fileInfo) {
                return files.contains(fileInfo.getId());
            }
        }));
        copy.setFiles(fileService.copyFiles(filesToCopy));
        repo.save(copy);

        documentInfo.getLinks().add(new Link(copy,LinkType.COPY_TO));
        repo.save(documentInfo);

        return copy;
    }

    public void removeDocument(String docId) {
        DocumentInfo documentInfo = repo.findOne(docId);
        documentInfo.setRemoved(true);
        repo.save(documentInfo);
    }

    public DocumentInfo create(DocumentInfo documentInfo) {
        Domain current = PrincipalUtils.getCurrentDomain();
        documentInfo.setDomain(current);
        return repo.save(documentInfo);
    }

    public DocumentInfo createNewDocumentInfo() throws JsonProcessingException {
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setMasterDocumentNumber(getNextMasterDocumentNumber());
        documentInfo.setDocumentNumber(String.valueOf(documentInfo.getMasterDocumentNumber()));
        documentInfo.setCreatedBy(PrincipalUtils.getCurrentPrincipal());
        documentInfo.setCreationDate(new Date());
        documentInfo.setRevision(new RevisionSymbol(0l));
        documentInfo.setState(DocumentState.IN_PROGRESS);
        return documentInfo;
        //return newDocumentToJson(documentInfo);
    }

    public DocumentState changeState(String id, DocumentState state){
        DocumentInfo doc = repo.findOne(id);
        doc.setState(state);
        repo.save(doc);
        return doc.getState();
    }

    @Override
    public DocumentInfo save(DocumentInfo documentInfo) {
        DocumentInfo doc = repo.findOne(documentInfo.getId());
        documentInfo.setRevisions(doc.getRevisions());
        return super.save(documentInfo);
    }

    public Long getNextMasterDocumentNumber() {
        Domain d = domainService.findById(PrincipalUtils.getCurrentDomain().getId());
        long id = d.getLastMasterDocumentNumber() +1;
        d.setLastMasterDocumentNumber(id);
        domainService.save(d);
        return id;
    }

    public List<DocumentInfo> findByCreatedBy(Principal principal) {
        return repo.findByCreatedByAndRemovedIsFalseOrderByCreationDateAsc(principal);
    }

    public List<DocumentInfo> findAll() {
        return repo.findByDomainAndRemovedIsFalseOrderByCreationDateAsc(PrincipalUtils.getCurrentDomain());
    }

    public List<DocumentInfo> findRecent() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        return repo.findByActivities_PrincipalAndActivities_DateGreaterThanAndActivities_TypeAndRemovedIsFalseOrderByActivities_DateAsc(PrincipalUtils.getCurrentPrincipal(), lastMonth.toDate(), DocumentActivity.ActivityType.OPENED);
    }

    public List<DocumentInfo> findByPredicate(Predicate predicate) {
        return Lists.newArrayList(repo.findAll(predicate));
    }

    public List<FileInfo> appendFile(String documentId, FileInfo fileInfo) {
        DocumentInfo documentInfo = repo.findOne(documentId);
        documentInfo.getFiles().add(fileInfo);
        repo.save(documentInfo);
        return documentInfo.getFiles();
    }

    public DocumentInfo getById(String id) {
        DocumentInfo documentInfo = repo.findOne(id);
        DocumentActivity activity = null;
        if (documentInfo.getActivities() != null) {
            activity = Iterables.find(documentInfo.getActivities(), new com.google.common.base.Predicate<DocumentActivity>() {
                @Override
                public boolean apply(@Nullable DocumentActivity documentActivity) {
                    return documentActivity.getPrincipal().getId().equals(PrincipalUtils.getCurrentPrincipal().getId()) && DocumentActivity.ActivityType.OPENED.equals(documentActivity.getType());
                }
            }, null);
        }
        if (activity == null) {
            activity = new DocumentActivity(DocumentActivity.ActivityType.OPENED, PrincipalUtils.getCurrentPrincipal(), new Date());
            documentInfo.getActivities().add(activity);
        } else {
            activity.setDate(new Date());
        }
        repo.save(documentInfo);
        if (documentInfo.getFavourites() != null) {
            DocumentFavourite fav = Iterables.find(documentInfo.getFavourites(), new com.google.common.base.Predicate<DocumentFavourite>() {
                @Override
                public boolean apply(@Nullable DocumentFavourite documentFavourite) {
                    return PrincipalUtils.getCurrentPrincipal().getId().equals(documentFavourite.getPrincipal().getId());
                }
            }, null);
            documentInfo.setFavourite(fav != null);
        } else {
            documentInfo.setFavourite(false);
        }
        return documentInfo;
    }

    public List<DocumentInfo> findMy() {
        return repo.findByCreatedByAndRemovedIsFalseOrderByCreationDateAsc(PrincipalUtils.getCurrentPrincipal());
    }

    public List<DocumentInfo> findFavourite() {
        return repo.findByFavourites_PrincipalOrderByCreationDateAsc(PrincipalUtils.getCurrentPrincipal());
    }

    public String autoCompleteDocument(String documentName) throws JsonProcessingException {
        List<DocumentInfo> documents = repo.findByDomainAndRemovedIsFalseAndDocumentNumberLike(PrincipalUtils.getCurrentDomain(), documentName);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInfo.class, AutoCompleteDocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documents);
    }

    public List<Link> linkDocuments(String linkFromId, DocumentInfo linkTo){
        DocumentInfo from = repo.findOne(linkFromId);
        from.getLinks().add(new Link(linkTo,LinkType.LINK));
        repo.save(from);
        return repo.findOne(from.getId()).getLinks();
    }
}
