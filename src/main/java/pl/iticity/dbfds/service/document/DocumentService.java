package pl.iticity.dbfds.service.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface DocumentService extends Service<DocumentInfo> {

    public String documentsToJson(List<DocumentInfo> documents) throws JsonProcessingException;

    public String newDocumentToJson(DocumentInfo documentInfo) throws JsonProcessingException;

    public void favourite(String id, boolean val);

    public DocumentInfo copyDocument(String docId, final List<String> files) throws FileNotFoundException;

    public void removeDocument(String docId);

    public DocumentInfo create(DocumentInfo documentInfo);

    public DocumentInfo createNewDocumentInfo() throws JsonProcessingException;

    public String changeState(String id, DocumentState state) throws JsonProcessingException;

    public DocumentInfo save(DocumentInfo documentInfo);

    public Long getNextMasterDocumentNumber(Domain domain);

    public List<DocumentInfo> findByCreatedBy(Principal principal);

    public List<DocumentInfo> findAll();

    public List<DocumentInfo> findRecent();

    public List<DocumentInfo> findByPredicate(Predicate predicate);

    public List<FileInfo> appendFile(String documentId, FileInfo fileInfo);

    public DocumentInfo getById(String id);

    public List<DocumentInfo> findMy();

    public List<DocumentInfo> findFavourite();

    public String autoCompleteDocument(String documentName) throws JsonProcessingException;

    public List<Link> linkDocuments(String linkFromId, DocumentInfo linkTo);

    public FileInfo appendTemplateFile(String docId, String tId) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException;
}
