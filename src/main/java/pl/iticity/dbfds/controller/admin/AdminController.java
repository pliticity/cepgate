package pl.iticity.dbfds.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.omg.CORBA.UnknownUserException;
import org.springframework.beans.factory.annotation.Autowired;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.FileService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

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
        List<Domain> domains = domainService.findAll();
        for (Domain domain : domains) {
            domain.setNoOfUsers(principalService.findByDomain(domain).size());
        }
        return domains;
    }

    @RequestMapping("domain/{id}")
    public
    @ResponseBody
    Domain getDomain(@PathVariable("id") String id) {
        Domain domain = domainService.findById(id);
        domain.setPrincipals(principalService.findByDomain(domain));
        domain.setNoOfFiles(fileService.countByDomain(domain));
        domain.setMemory(fileService.countMemoryByDomain(domain));
        return domain;
    }


}
