package pl.iticity.dbfds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Comment;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Revision;
import pl.iticity.dbfds.model.RevisionSymbol;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class RevisionService {

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    @Autowired
    private FileService fileService;

    public List<Revision> addRevision(String docId) throws JsonProcessingException, FileNotFoundException {
        DocumentInfo document = documentInfoRepository.findOne(docId);
        document.setFiles(fileService.copyFiles(document.getFiles()));
        Revision revision = new Revision(document.getRevision(),document);
        document.setRevision(document.getRevision().next());
        document.getRevisions().add(revision);
        documentInfoRepository.save(document);
        return document.getRevisions();
    }

    public DocumentInfo fetchRevision(String docId, final String rev) throws IOException {
        DocumentInfo document = documentInfoRepository.findOne(docId);
        Revision revision = Iterables.find(document.getRevisions(), new Predicate<Revision>() {
            @Override
            public boolean apply(@Nullable Revision revision) {
                return rev.equals(revision.getRevision().getEffective());
            }
        });
        return revision.getRevisionData();
    }
}
