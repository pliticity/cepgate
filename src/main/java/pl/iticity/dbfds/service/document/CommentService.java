package pl.iticity.dbfds.service.document;

import pl.iticity.dbfds.model.document.Comment;

import java.util.List;

public interface CommentService {

    public Comment createNewComment();

    public List<Comment> addComment(String id, Comment comment);

}
