package pl.iticity.dbfds.controller.quotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.quotation.ListQICMixin;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.quotation.QICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/quotation")
public class QuotationListController extends BaseController{

    @Autowired
    private QICService qicService;

    @RequestMapping(value = "/query",params = {"my"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String getMyQuotationList() {
        List<QuotationInformationCarrier> list = qicService.findByDomainAndPrincipal(PrincipalUtils.getCurrentDomain(),PrincipalUtils.getCurrentPrincipal());
        return convertToString(QuotationInformationCarrier.class, ListQICMixin.class,list);
    }

    @RequestMapping(value = "/query",params = {"all"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String getAllQuotationList() {
        List<QuotationInformationCarrier> list = qicService.findByDomain(PrincipalUtils.getCurrentDomain());
        return convertToString(QuotationInformationCarrier.class, ListQICMixin.class,list);
    }
}
