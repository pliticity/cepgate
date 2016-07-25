package pl.iticity.dbfds.service.document;

import pl.iticity.dbfds.model.Comment;

import java.util.List;

public interface CommentService {

    public Comment createNewComment();

    public List<Comment> addComment(String id, Comment comment);

}
