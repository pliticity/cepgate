package pl.iticity.dbfds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Comment;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Revision;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.FileNotFoundException;
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
        long rev = document.getRevisionNo() == 0l ? 1l : document.getRevisionNo();
        Revision revision = new Revision(rev,document);
        document.getRevisions().add(revision);
        documentInfoRepository.save(document);
        return document.getRevisions();
    }
}
