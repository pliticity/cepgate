package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.service.DocumentService;

@Controller
@RequestMapping("/document")
public class DocumentController extends AbstractCrudController<DocumentInfo,DocumentService>{

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String getDocumentView(){
        return "document";
    }

}
