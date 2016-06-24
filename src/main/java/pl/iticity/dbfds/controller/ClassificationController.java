package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentTemplate;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.service.ClassificationService;
import pl.iticity.dbfds.service.FileService;
import pl.iticity.dbfds.service.TemplateService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Classification> getDocumentTypes(@RequestParam("active") boolean active){
        return classificationService.findByDomain(PrincipalUtils.getCurrentDomain(),active);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public
    @ResponseBody
    List<Classification> postClassification(@RequestBody Classification classification) {
        return classificationService.addClassification(classification,PrincipalUtils.getCurrentDomain());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT,params = {"toggle"})
    public
    @ResponseBody
    List<Classification> toggleClassification(@PathVariable("id") String id,@RequestParam("toggle") boolean toggle) {
        return classificationService.toggleClassification(id,toggle);
    }
}
