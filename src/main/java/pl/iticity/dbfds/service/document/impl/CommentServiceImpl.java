package pl.iticity.dbfds.service.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Comment;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;
import pl.iticity.dbfds.service.document.CommentService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    public Comment createNewComment(){
        Comment comment = new Comment();
        comment.setDate(new Date());
        comment.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        comment.setDomain(PrincipalUtils.getCurrentDomain());
        return comment;
    }

    public List<Comment> addComment(String id, Comment comment){
        DocumentInfo document = documentInfoRepository.findOne(id);
        document.getComments().add(comment);
        documentInfoRepository.save(document);
        return document.getComments();
    }

}
