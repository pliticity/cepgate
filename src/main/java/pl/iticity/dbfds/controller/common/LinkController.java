package pl.iticity.dbfds.controller.common;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.model.common.Link;
import pl.iticity.dbfds.model.common.LinkType;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.common.LinkService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class LinkController extends BaseController {

    @Autowired
    private LinkService linkService;

    private Map<String, Class> linkables;

    @PostConstruct
    public void postConstruct() {
        linkables = Maps.newHashMap();
        linkables.put("document", DocumentInformationCarrier.class);
        linkables.put("project", ProjectInformationCarrier.class);
        linkables.put("product", ProductInformationCarrier.class);
        linkables.put("quotation", QuotationInformationCarrier.class);
    }

    @RequestMapping(value = "/link", method = RequestMethod.POST, params = {"parentId", "parentType", "objectId", "objectType", "linkType"})
    public List<Link> postLinkDocuments(@RequestParam(value = "parentId") String parentId, @RequestParam(value = "parentType") String parentType, @RequestParam(value = "objectId") String objectId, @RequestParam(value = "objectType") String objectType, @RequestParam(value = "linkType") String linkType) {
        return linkService.createLink(parentId, linkables.get(parentType), objectId, linkables.get(objectType), LinkType.valueOf(linkType));
    }

    @RequestMapping(value = "/link/{linkId}", method = RequestMethod.GET)
    public Linkable getLinkObject(@PathVariable("linkId") String linkId) {
        return linkService.getLinkObject(linkId);
    }

    @RequestMapping(value = "/unlink", method = RequestMethod.POST, params = {"parentId", "parentType"})
    public List<Link> deleteLinkDocuments(@RequestParam(value = "parentId") String parentId, @RequestParam(value = "parentType") String parentType, @RequestBody Link link) {
        return linkService.deleteLink(parentId, linkables.get(parentType), link);
    }

}
