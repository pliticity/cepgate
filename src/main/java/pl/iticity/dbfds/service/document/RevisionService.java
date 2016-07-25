package pl.iticity.dbfds.service.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public interface RevisionService {

    public List<Revision> addRevision(String docId) throws JsonProcessingException, FileNotFoundException;

    public DocumentInfo fetchRevision(String docId, final String rev) throws IOException;
}
