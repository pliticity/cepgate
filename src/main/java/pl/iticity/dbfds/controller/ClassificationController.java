package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.mixins.classification.ListClassificationMixin;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping("/classification")
public class ClassificationController extends BaseController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getClassifications(@RequestParam("active") boolean active, @RequestParam("for") String forClassification, @RequestParam(value = "domainId", required = false) String domainId) {
        domainId = StringUtils.isEmpty(domainId) ? null : domainId;
        List<Classification> classifications = null;
        classifications = classificationService.findByDomainForClassification(domainId, active, forClassification);
        return convertToString(Classification.class, ListClassificationMixin.class, classifications);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = {"model"})
    public String getClassifications(@RequestParam("model") String model) {
        return convertToString(Classification.class, ListClassificationMixin.class, classificationService.findForModel(PrincipalUtils.getCurrentDomain(), model));
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public boolean getDocumentTypes(@RequestParam("id") String id, @RequestParam("clId") String clId, @RequestParam(value = "domainId", required = false) String domainId) {
        return classificationService.exists(clId, id, domainId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postClassification(@RequestBody Classification classification, @RequestParam(value = "domainId", required = false) String domainId) {
        List<Classification> classifications = classificationService.addClassification(classification, domainId);
        return convertToString(Classification.class, ListClassificationMixin.class, classifications);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, params = {"toggle"})
    public String toggleClassification(@PathVariable("id") String id, @RequestParam("toggle") boolean toggle) {
        List<Classification> classifications = classificationService.toggleClassification(id, toggle);
        return convertToString(Classification.class, ListClassificationMixin.class, classifications);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteClassification(@PathVariable("id") String id) {
        List<Classification> classifications = classificationService.deleteClassification(id);
        return convertToString(Classification.class, ListClassificationMixin.class, classifications);
    }
}
