package pl.iticity.dbfds.service.document.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.joda.time.LocalDateTime;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.common.Bond;
import pl.iticity.dbfds.model.common.BondType;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.common.ObjectType;
import pl.iticity.dbfds.model.document.*;
import pl.iticity.dbfds.model.mixins.AutoCompleteDocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.DocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.DocumentInfoStateChangeMixin;
import pl.iticity.dbfds.model.mixins.NewDocumentInfoMixIn;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.BondService;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.DocumentService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.document.TemplateService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl extends AbstractService<DocumentInformationCarrier, String, DocumentInfoRepository> implements DocumentService {

    private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class);

    @Autowired
    private DomainService domainService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BondService bondService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private ClassificationService classificationService;

    public String documentsToJson(List<DocumentInformationCarrier> documents) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInformationCarrier.class, DocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documents);
    }

    public String newDocumentToJson(DocumentInformationCarrier documentInformationCarrier) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInformationCarrier.class, NewDocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documentInformationCarrier);
    }

    public void favourite(String id, boolean val) {
        final Principal current = PrincipalUtils.getCurrentPrincipal();
        DocumentInformationCarrier info = repo.findOne(id);
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

    public DocumentInformationCarrier copyDocument(String docId, final List<String> files) throws FileNotFoundException {
        DocumentInformationCarrier documentInformationCarrier = repo.findOne(docId);
        DocumentInformationCarrier copy = documentInformationCarrier.clone();

        copy.setMasterDocumentNumber(getNextMasterDocumentNumber(PrincipalUtils.getCurrentDomain()));

        DecimalFormat decimalFormat = new DecimalFormat("#");

        String docNo = MessageFormat.format("{0}-{1}", documentInformationCarrier.getDomain().getAccountNo(), decimalFormat.format(copy.getMasterDocumentNumber()));
        copy.setDocumentNumber(docNo);
        List<FileInfo> filesToCopy = Lists.newArrayList(Iterables.filter(documentInformationCarrier.getFiles(), new com.google.common.base.Predicate<FileInfo>() {
            @Override
            public boolean apply(@Nullable FileInfo fileInfo) {
                return files.contains(fileInfo.getId());
            }
        }));
        copy.setFiles(fileService.copyFiles(filesToCopy));
        copy.setPlannedIssueDate(null);
        copy.setResponsibleUser(null);
        repo.save(copy);

        bondService.createBond(documentInformationCarrier.getId(), DocumentInformationCarrier.class, false, copy.getId(), DocumentInformationCarrier.class, false, BondType.COPY);

        repo.save(documentInformationCarrier);

        return copy;
    }

    public void removeDocument(String docId) {
        DocumentInformationCarrier documentInformationCarrier = repo.findOne(docId);
        documentInformationCarrier.setRemoved(true);
        repo.save(documentInformationCarrier);
    }

    public DocumentInformationCarrier create(DocumentInformationCarrier doc) {
        if (doc.getId() != null) {
            DocumentInformationCarrier dic = repo.findOne(doc.getId());
            doc.setRevisions(dic.getRevisions());
            doc.setFavourites(dic.getFavourites());
        }
        Domain current = PrincipalUtils.getCurrentDomain();
        doc.setDomain(current);
        doc.setClassification(classificationService.findById(doc.getClassification().getId()));
        doc = repo.save(doc);
        createLink(doc);
        return doc;
    }

    private void createLink(DocumentInformationCarrier dic){
        List<Bond> bonds = bondService.findBondsForObject(dic.getId(),DocumentInformationCarrier.class,ObjectType.getNonDic());
        Bond bond = Iterables.find(bonds, new com.google.common.base.Predicate<Bond>() {
            @Override
            public boolean apply(@Nullable Bond bond) {
                return BondType.DOC.equals(bond.getBondType());
            }
        },null);
        if(bond!=null){
            bondService.deleteBond(bond.getId());
        }
        if(dic.getClassification()!=null){
            Classification c = classificationService.findById(dic.getClassification().getId());
            if(c.getModelId() != null && c.getModelClazz() !=null){
                try {
                    Class clazz = Class.forName(c.getModelClazz());
                    bondService.createBond(dic.getId(),DocumentInformationCarrier.class,false,c.getModelId(),clazz,false,BondType.DOC);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DocumentInformationCarrier createNewDocumentInfo() throws JsonProcessingException {
        Domain domain = PrincipalUtils.getCurrentDomain();
        DocumentInformationCarrier documentInformationCarrier = new DocumentInformationCarrier();
        documentInformationCarrier.setMasterDocumentNumber(getNextMasterDocumentNumber(PrincipalUtils.getCurrentDomain()));

        DecimalFormat decimalFormat = new DecimalFormat("#");

        String docNo = MessageFormat.format("{0}-{1}", domain.getAccountNo(), decimalFormat.format(documentInformationCarrier.getMasterDocumentNumber()));
        documentInformationCarrier.setDocumentNumber(docNo);
        documentInformationCarrier.setCreatedBy(PrincipalUtils.getCurrentPrincipal());
        documentInformationCarrier.setCreationDate(new Date());
        documentInformationCarrier.setRevision(new RevisionSymbol(0l));
        documentInformationCarrier.setState(DocumentState.IN_PROGRESS);
        return documentInformationCarrier;
        //return newDocumentToJson(documentInformationCarrier);
    }

    public String changeState(String id, DocumentState state) throws JsonProcessingException {
        DocumentInformationCarrier doc = repo.findOne(id);
        doc.setState(state);
        if (DocumentState.ARCHIVED.equals(state)) {
            doc.setArchivedDate(new Date());
        } else {
            doc.setArchivedDate(null);
        }
        repo.save(doc);
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(DocumentInformationCarrier.class, DocumentInfoStateChangeMixin.class);
        return mapper.writeValueAsString(doc);
    }

    @Override
    public DocumentInformationCarrier save(DocumentInformationCarrier documentInformationCarrier) {
        DocumentInformationCarrier doc = repo.findOne(documentInformationCarrier.getId());
        documentInformationCarrier.setRevisions(doc.getRevisions());
        documentInformationCarrier.setFavourites(doc.getFavourites());
        super.save(documentInformationCarrier);
        //doc = repo.findOne(documentInformationCarrier.getId());
        createLink(doc);
        return doc;
    }

    public Long getNextMasterDocumentNumber(Domain domain) {
        Domain d = domainService.findById(domain.getId());
        long id = d.getLastMasterDocumentNumber() + 1;
        d.setLastMasterDocumentNumber(id);
        domainService.save(d);
        return id;
    }

    public List<DocumentInformationCarrier> findAll() {
        return repo.findByDomainAndRemovedIsFalseOrderByCreationDateAsc(PrincipalUtils.getCurrentDomain());
    }

    public List<DocumentInformationCarrier> findRecent() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        return repo.findByActivities_PrincipalAndActivities_DateGreaterThanAndActivities_TypeAndRemovedIsFalseOrderByActivities_DateDesc(PrincipalUtils.getCurrentPrincipal(), lastMonth.toDate(), DocumentActivity.ActivityType.OPENED);
    }

    public List<FileInfo> appendFile(String documentId, FileInfo fileInfo) {
        DocumentInformationCarrier documentInformationCarrier = repo.findOne(documentId);
        documentInformationCarrier.getFiles().add(fileInfo);
        repo.save(documentInformationCarrier);
        return documentInformationCarrier.getFiles();
    }

    public DocumentInformationCarrier getById(String id) {
        DocumentInformationCarrier documentInformationCarrier = repo.findOne(id);
        DocumentActivity activity = null;
        if (documentInformationCarrier.getActivities() != null) {
            activity = Iterables.find(documentInformationCarrier.getActivities(), new com.google.common.base.Predicate<DocumentActivity>() {
                @Override
                public boolean apply(@Nullable DocumentActivity documentActivity) {
                    return documentActivity.getPrincipal().getId().equals(PrincipalUtils.getCurrentPrincipal().getId()) && DocumentActivity.ActivityType.OPENED.equals(documentActivity.getType());
                }
            }, null);
        }
        if (activity == null) {
            activity = new DocumentActivity(DocumentActivity.ActivityType.OPENED, PrincipalUtils.getCurrentPrincipal(), new Date());
            documentInformationCarrier.getActivities().add(activity);
        } else {
            activity.setDate(new Date());
        }
        repo.save(documentInformationCarrier);
        if (documentInformationCarrier.getFavourites() != null) {
            DocumentFavourite fav = Iterables.find(documentInformationCarrier.getFavourites(), new com.google.common.base.Predicate<DocumentFavourite>() {
                @Override
                public boolean apply(@Nullable DocumentFavourite documentFavourite) {
                    return PrincipalUtils.getCurrentPrincipal().getId().equals(documentFavourite.getPrincipal().getId());
                }
            }, null);
            documentInformationCarrier.setFavourite(fav != null);
        } else {
            documentInformationCarrier.setFavourite(false);
        }
        return documentInformationCarrier;
    }

    public List<DocumentInformationCarrier> findMy() {
        return repo.findByCreatedByAndRemovedIsFalseOrderByCreationDateAsc(PrincipalUtils.getCurrentPrincipal());
    }

    public List<DocumentInformationCarrier> findFavourite() {
        return repo.findByFavourites_PrincipalOrderByCreationDateAsc(PrincipalUtils.getCurrentPrincipal());
    }

    public String autoCompleteDocument(String documentName) throws JsonProcessingException {
        List<DocumentInformationCarrier> documents = repo.findByDomainAndRemovedIsFalseAndDocumentNumberLike(PrincipalUtils.getCurrentDomain(), documentName);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DocumentInformationCarrier.class, AutoCompleteDocumentInfoMixIn.class);
        return objectMapper.writeValueAsString(documents);
    }

    public FileInfo appendTemplateFile(String docId, String tId) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException {
        DocumentTemplate template = templateService.findById(tId);
        AuthorizationProvider.isInDomain(template.getDomain());
        DocumentInformationCarrier doc = findById(docId);
        AuthorizationProvider.isInDomain(doc.getDomain());
        FileInfo copy = templateService.copyFileAndFillMeta(template.getFile(), doc);
        doc.getFiles().add(copy);
        repo.save(doc);
        return copy;
    }
}
