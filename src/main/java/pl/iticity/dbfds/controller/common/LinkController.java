package pl.iticity.dbfds.controller.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.BaseModel;
import pl.iticity.dbfds.model.common.Bond;
import pl.iticity.dbfds.model.common.BondType;
import pl.iticity.dbfds.model.common.ObjectType;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.common.BondService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class LinkController extends BaseController {

    @Autowired
    private BondService bondService;

    private Map<String, Class> linkables;

    @PostConstruct
    public void postConstruct() {
        linkables = Maps.newHashMap();
        linkables.put("document", DocumentInformationCarrier.class);
        linkables.put("project", ProjectInformationCarrier.class);
        linkables.put("product", ProductInformationCarrier.class);
        linkables.put("quotation", QuotationInformationCarrier.class);
    }

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public Bond postLinkDocuments(@RequestBody CreateBondDTO dto) {
        return bondService.createBond(dto.getFirstId(), linkables.get(dto.getFirstType()), dto.isFirstRevision(), dto.getSecondId(), linkables.get(dto.getSecondType()), dto.isSecondRevision(), BondType.LINK);
    }

    @RequestMapping(value = "/link", method = RequestMethod.GET,params = {"oId","oType","dic"})
    public List<Bond> getLinks(@RequestParam("oId") String oId, @RequestParam("oType") String oType, @RequestParam(value = "dic",required = false) boolean dic) {
        List<ObjectType> objectTypes = null;
        if(dic){
            objectTypes = Lists.newArrayList(ObjectType.DOCUMENT);
        }else{
            objectTypes = Lists.newArrayList(ObjectType.PRODUCT,ObjectType.PROJECT,ObjectType.QUOTATION);
        }
        return bondService.findBondsForObject(oId,linkables.get(oType),objectTypes);
    }

    @RequestMapping(value = "/link/{linkId}", method = RequestMethod.GET, params = {"number"})
    public BaseModel getLinkObject(@PathVariable("linkId") String linkId, @RequestParam("number") String number) {
        return bondService.findObjectForLink(linkId, "first".equals(number));
    }

    @RequestMapping(value = "/unlink", method = RequestMethod.POST, params = {"linkId"})
    public boolean deleteLinkDocuments(@RequestParam(value = "linkId") String linkId) {
        bondService.deleteBond(linkId);
        return true;
    }

}
