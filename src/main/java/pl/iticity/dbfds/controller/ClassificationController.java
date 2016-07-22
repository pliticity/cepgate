package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.mixins.classification.ListClassificationMixin;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/classification")
public class ClassificationController extends BaseController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody String getClassifications(@RequestParam("active") boolean active, @RequestParam("for") String forClassification){
        List<Classification> classifications = null;
        classifications =classificationService.findByDomainForClassification(PrincipalUtils.getCurrentDomain(),active,forClassification);
        return convertToString(Classification.class, ListClassificationMixin.class,classifications);
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public @ResponseBody boolean getDocumentTypes(@RequestParam("id") String id, @RequestParam("clId") String clId){
        return classificationService.exists(clId,id);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public
    @ResponseBody
    String postClassification(@RequestBody Classification classification) {
        List<Classification> classifications = classificationService.addClassification(classification,PrincipalUtils.getCurrentDomain());
        return convertToString(Classification.class, ListClassificationMixin.class,classifications);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT,params = {"toggle"})
    public
    @ResponseBody
    String toggleClassification(@PathVariable("id") String id,@RequestParam("toggle") boolean toggle) {
        List<Classification> classifications = classificationService.toggleClassification(id,toggle);
        return convertToString(Classification.class, ListClassificationMixin.class,classifications);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteClassification(@PathVariable("id") String id){
        List<Classification> classifications = classificationService.deleteClassification(id);
        return convertToString(Classification.class, ListClassificationMixin.class,classifications);
    }
}
