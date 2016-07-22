package pl.iticity.dbfds.controller.admin;

import org.springframework.stereotype.Controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.common.impl.PrincipalServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController{

    @Autowired
    private FileService fileService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("")
    public String getAdminView() throws IllegalAccessException {
        return "backoffice";
    }

    @RequestMapping("domain")
    public
    @ResponseBody
    List<Domain> getDomains() {
        AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN,null);
        List<Domain> domains = domainService.findAll();
        for (Domain domain : domains) {
            domain.setNoOfUsers(principalService.findByDomain(domain).size());
            domain.setNoOfFiles(fileService.countByDomain(domain));
            domain.setMemory(fileService.countMemoryByDomain(domain));
        }
        return domains;
    }

    @RequestMapping("domain/{id}")
    public
    @ResponseBody
    Domain getDomain(@PathVariable("id") String id) {
        AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN,null);
        Domain domain = domainService.findById(id);
        domain.setPrincipals(principalService.findByDomain(domain));
        domain.setNoOfFiles(fileService.countByDomain(domain));
        domain.setMemory(fileService.countMemoryByDomain(domain));
        domain.setNoOfUsers(domain.getPrincipals().size());
        return domain;
    }

    @RequestMapping(value = "domain/{id}",method = RequestMethod.PUT, params = {"active"})
    public
    @ResponseBody
    List<Domain> putToggleDomainActive(@PathVariable("id") String id, @RequestParam("active") boolean active) {
        AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN,null);
        Domain domain = domainService.findById(id);
        domain.setActive(active);
        domainService.save(domain);
        return getDomains();
    }


}
