package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.document.Comment;
import pl.iticity.dbfds.service.document.CommentService;

import java.util.List;

@Controller
@RequestMapping("/document/{id}/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Comment> postAddComment(@PathVariable(value = "id") String id, @RequestBody Comment comment) {
        return commentService.addComment(id,comment);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    Comment postAddComment(@PathVariable(value = "id") String id) {
        return commentService.createNewComment();
    }

}
