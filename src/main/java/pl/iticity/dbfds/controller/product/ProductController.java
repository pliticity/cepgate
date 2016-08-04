package pl.iticity.dbfds.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.mixins.product.DetailsPICMixin;
import pl.iticity.dbfds.model.mixins.product.ListPICMixin;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.service.common.LinkService;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private PICService picService;

    @Autowired
    private LinkService linkService;

    @RequestMapping(value = "",params = {"new"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String getNewProduct() {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class,picService.createNew());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    String postSaveProduct(@RequestBody ProductInformationCarrier pic) {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class,picService.savePIC(pic));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getProduct(@PathVariable("id") String id) {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class,picService.findById(id));
    }

    @RequestMapping(value = "/link/{pId}", method = RequestMethod.POST)
    public @ResponseBody
    List<Link> postLinkDocuments(@PathVariable(value = "pId") String pId, @RequestBody DocumentInfo linkTo){
        return linkService.createLink(pId,ProductInformationCarrier.class,linkTo);
    }

    @RequestMapping(value = "/link/{pId}", method = RequestMethod.DELETE)
    public @ResponseBody
    List<Link> deleteLinkDocuments(@PathVariable(value = "pId") String pId, @RequestBody Link link){
        return linkService.deleteLink(pId,ProductInformationCarrier.class,link);
    }

}
