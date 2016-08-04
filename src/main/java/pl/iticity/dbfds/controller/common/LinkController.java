package pl.iticity.dbfds.controller.common;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.mixins.project.DetailsPJCMixin;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.common.LinkService;
import pl.iticity.dbfds.service.project.PJCService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class LinkController extends BaseController {

    @Autowired
    private LinkService linkService;

    private Map<String, Class> linkables;

    @PostConstruct
    public void postConstruct(){
        linkables = Maps.newHashMap();
        linkables.put("document",DocumentInfo.class);
        linkables.put("project",ProjectInformationCarrier.class);
        linkables.put("product",ProductInformationCarrier.class);
        linkables.put("quotation",QuotationInformationCarrier.class);
    }

    @RequestMapping(value = "/link/{id}", method = RequestMethod.POST, params = {"type"})
    public @ResponseBody
    List<Link> postLinkDocuments(@RequestParam(value = "type") String type,@PathVariable(value = "id") String id, @RequestBody DocumentInfo linkTo){
        return linkService.createLink(id,linkables.get(type),linkTo);
    }

    @RequestMapping(value = "/unlink/{id}", method = RequestMethod.POST, params = {"type"})
    public @ResponseBody
    List<Link> deleteLinkDocuments(@RequestParam(value = "type") String type,@PathVariable(value = "id") String id, @RequestBody Link link){
        return linkService.deleteLink(id,linkables.get(type),link);
    }

}
