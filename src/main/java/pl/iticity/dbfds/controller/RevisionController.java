package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.Revision;
import pl.iticity.dbfds.service.document.RevisionService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document/{id}/revision")
public class RevisionController {

    @Autowired
    private RevisionService revisionService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<Revision> postAddRevision(@PathVariable(value = "id") String id) throws JsonProcessingException, FileNotFoundException {
        return revisionService.addRevision(id);
    }

    @RequestMapping(value = "/{rev}", method = RequestMethod.GET)
    public DocumentInformationCarrier postAddRevision(@PathVariable(value = "id") String id, @PathVariable(value = "rev") String rev) throws IOException {
        return revisionService.fetchRevision(id, rev);
    }

}
