package pl.iticity.dbfds.controller.gadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.common.ListDomainMixin;
import pl.iticity.dbfds.model.mixins.quotation.ListQICMixin;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping("/domain")
public class DomainListController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private PrincipalService principalService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String getAllDomains() {
        AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN, null);
        List<Domain> domains = domainService.findAll();
        for (Domain domain : domains) {
            domain.setNoOfUsers(principalService.findByDomain(domain).size());
            domain.setNoOfFiles(fileService.countByDomain(domain));
            domain.setMemory(fileService.countMemoryByDomain(domain));
        }
        return convertToString(Domain.class, ListDomainMixin.class, domains);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, params = {"active"})
    public String putToggleDomainActive(@PathVariable("id") String id, @RequestParam("active") boolean active) {
        AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN, null);
        Domain domain = domainService.findById(id);
        domain.setActive(active);
        domainService.save(domain);
        return getAllDomains();
    }
}
