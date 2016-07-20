package pl.iticity.dbfds.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.product.ListPICMixin;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductListController extends BaseController{

    @Autowired
    private PICService picService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public
    @ResponseBody
    String getProductList() {
        List<ProductInformationCarrier> list = picService.findByDomain(PrincipalUtils.getCurrentDomain());
        return convertToString(ProductInformationCarrier.class, ListPICMixin.class,list);
    }

}
