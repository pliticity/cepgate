package pl.iticity.dbfds.service.document.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.DocumentState;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.model.document.Revision;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.document.RevisionService;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class RevisionServiceImpl implements RevisionService{

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    @Autowired
    private FileService fileService;

    public List<Revision> addRevision(String docId) throws JsonProcessingException, FileNotFoundException {
        DocumentInformationCarrier document = documentInfoRepository.findOne(docId);
        List<FileInfo> files = document.getFiles();
        document.setFiles(fileService.copyFiles(document.getFiles()));
        Revision revision = new Revision(document.getRevision(),document,document.getArchivedDate());
        document.setRevision(document.getRevision().next());
        document.getRevisions().add(revision);
        document.setState(DocumentState.IN_PROGRESS);
        document.setFiles(files);
        documentInfoRepository.save(document);
        return document.getRevisions();
    }

    public DocumentInformationCarrier fetchRevision(String docId, final String rev) throws IOException {
        DocumentInformationCarrier document = documentInfoRepository.findOne(docId);
        Revision revision = Iterables.find(document.getRevisions(), new Predicate<Revision>() {
            @Override
            public boolean apply(@Nullable Revision revision) {
                return rev.equals(revision.getRevision().getEffective());
            }
        });
        return revision.getRevisionData();
    }
}
