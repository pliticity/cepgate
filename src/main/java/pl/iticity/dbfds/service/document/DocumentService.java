package pl.iticity.dbfds.service.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.DocumentState;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface DocumentService extends Service<DocumentInformationCarrier> {

    public String documentsToJson(List<DocumentInformationCarrier> documents) throws JsonProcessingException;

    public String newDocumentToJson(DocumentInformationCarrier documentInformationCarrier) throws JsonProcessingException;

    public void favourite(String id, boolean val);

    public DocumentInformationCarrier copyDocument(String docId, final List<String> files) throws FileNotFoundException;

    public void removeDocument(String docId);

    public DocumentInformationCarrier create(DocumentInformationCarrier documentInformationCarrier);

    public DocumentInformationCarrier createNewDocumentInfo() throws JsonProcessingException;

    public String changeState(String id, DocumentState state) throws JsonProcessingException;

    public DocumentInformationCarrier save(DocumentInformationCarrier documentInformationCarrier);

    public Long getNextMasterDocumentNumber(Domain domain);

    public List<DocumentInformationCarrier> findAll();

    public List<DocumentInformationCarrier> findRecent();

    public List<FileInfo> appendFile(String documentId, FileInfo fileInfo);

    public DocumentInformationCarrier getById(String id);

    public List<DocumentInformationCarrier> findMy();

    public List<DocumentInformationCarrier> findFavourite();

    public String autoCompleteDocument(String documentName) throws JsonProcessingException;

    public FileInfo appendTemplateFile(String docId, String tId) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException;

}
