package pl.iticity.dbfds.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.document.DocumentTemplate;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.document.TemplateService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<DocumentTemplate> getTemplates(@RequestParam(value = "domainId",required = false) String domainId) {
        domainId = StringUtils.isEmpty(domainId) ? null : domainId;
        return templateService.findByDomain(domainId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    List<DocumentTemplate> deleteTemplate(@PathVariable(value = "id") String id,@RequestParam(value = "domainId",required = false) String domainId) {
        templateService.delete(templateService.findById(id));
        return getTemplates(domainId);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    DocumentTemplate postUploadFile(@RequestParam("file") MultipartFile file,@RequestParam(value = "domainId",required = false) String domainId) throws IOException, InvalidFormatException {
        return templateService.createTemplate(file,domainId);
    }

}
