package pl.iticity.dbfds.controller.quotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.quotation.DetailsQICMixin;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.quotation.QICService;

@RestController
@RequestMapping("/quotation")
public class QuotationController extends BaseController {

    @Autowired
    private QICService qicService;

    @RequestMapping(value = "", params = {"new"}, method = RequestMethod.GET)
    public String getNewQuotation() {
        return convertToString(QuotationInformationCarrier.class, DetailsQICMixin.class, qicService.newQIC());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postSaveQuotation(@RequestBody QuotationInformationCarrier qic) {
        return convertToString(QuotationInformationCarrier.class, DetailsQICMixin.class, qicService.saveQIC(qic));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getQuotation(@PathVariable("id") String id) {
        return convertToString(QuotationInformationCarrier.class, DetailsQICMixin.class, qicService.findById(id));
    }

}
