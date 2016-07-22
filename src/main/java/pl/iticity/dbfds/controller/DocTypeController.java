package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.service.document.DocumentTypeService;

import java.util.List;

@Controller
@RequestMapping("/doctype")
public class DocTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody List<DocumentType> deleteClassification(@PathVariable("id") String id){
        return documentTypeService.deleteDocType(id);
    }

}
