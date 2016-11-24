package pl.iticity.dbfds.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.product.DetailsPICMixin;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.service.product.PICService;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private PICService picService;

    @RequestMapping(value = "", params = {"new"}, method = RequestMethod.GET)
    public String getNewProduct() {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class, picService.createNew());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postSaveProduct(@RequestBody ProductInformationCarrier pic) {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class, picService.savePIC(pic));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getProduct(@PathVariable("id") String id) {
        return convertToString(ProductInformationCarrier.class, DetailsPICMixin.class, picService.findById(id));
    }

    @RequestMapping(value = "/autocomplete/{pId}", method = RequestMethod.GET)
    public String getAutoCompleteDocument(@PathVariable(value = "pId") String pId) throws JsonProcessingException {
        return picService.autoCompleteProduct(pId);
    }


}
