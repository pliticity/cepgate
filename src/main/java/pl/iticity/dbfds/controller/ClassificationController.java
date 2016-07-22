package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Classification> getDocumentTypes(@RequestParam("active") boolean active,@RequestParam("without") String without){
        if(StringUtils.isEmpty(without) || "0".equals(without)){
            return classificationService.findByDomain(PrincipalUtils.getCurrentDomain(),active);
        }else{
            return classificationService.findByDomain(PrincipalUtils.getCurrentDomain(),active,without);
        }
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public @ResponseBody boolean getDocumentTypes(@RequestParam("id") String id, @RequestParam("clId") String clId){
        return classificationService.exists(clId,id);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody List<Classification> deleteClassification(@PathVariable("id") String id){
        return classificationService.deleteClassification(id);
    }
}
