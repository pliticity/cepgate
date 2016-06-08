package pl.iticity.dbfds.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/domain")
public class DomainAdminController {

    @Autowired
    private DomainService domainService;

    @Autowired
    private PrincipalService principalService;

    @RequestMapping(value = "", params = {"isAdmin"})
    public
    @ResponseBody
    boolean getIsAdmin() {
        return SecurityUtils.getSubject().hasRole(Role.ADMIN.name());
    }

    @RequestMapping(value = "", params = {"domain"})
    public
    @ResponseBody
    Domain getDomain() throws IllegalAccessException {
        return PrincipalUtils.getCurrentDomain();
    }

    @RequestMapping("/{id}")
    public
    @ResponseBody
    Domain getDomain(@PathVariable("id") String id) {
        Domain domain = domainService.findById(id);
        domain.setPrincipals(principalService.findByDomain(domain));
        return domain;
    }


}
