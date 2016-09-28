package pl.iticity.dbfds.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping("/doctype")
public class DocTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public List<DocumentType> deleteClassification(@PathVariable("id") String id) {
        return documentTypeService.deleteDocType(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DocumentType> getDocumentTypes(@RequestParam("active") boolean active, @RequestParam(value = "domainId", required = false) String domainId) {
        domainId = StringUtils.isEmpty(domainId) ? null : domainId;
        return documentTypeService.findByDomain(domainId, active);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<DocumentType> postDocumentType(@RequestBody DocumentType documentType, @RequestParam(value = "domainId", required = false) String domainId) {
        domainId = StringUtils.isEmpty(domainId) ? null : domainId;
        return documentTypeService.addDocType(documentType, domainId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, params = {"toggle"})
    public List<DocumentType> deleteDocType(@PathVariable("id") String id, @RequestParam("toggle") boolean toggle) {
        return documentTypeService.toggleDocType(id, toggle);
    }
}
