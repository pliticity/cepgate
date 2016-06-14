package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Comment;
import pl.iticity.dbfds.model.Revision;
import pl.iticity.dbfds.service.CommentService;
import pl.iticity.dbfds.service.RevisionService;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/document/{id}/revision")
public class RevisionController {

    @Autowired
    private RevisionService revisionService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Revision> postAddRevision(@PathVariable(value = "id") String id) throws JsonProcessingException, FileNotFoundException {
        return revisionService.addRevision(id);
    }

}
