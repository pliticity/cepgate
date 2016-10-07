package pl.iticity.dbfds.service.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.Revision;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public interface RevisionService {

    public List<Revision> addRevision(String docId) throws JsonProcessingException, FileNotFoundException;

    public DocumentInformationCarrier fetchRevision(String docId, final String rev) throws IOException;
}
