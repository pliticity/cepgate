package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.mixins.PrincipalMixin;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/principal")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DomainService domainService;

    @RequestMapping(value = "")
    public
    @ResponseBody
    String getPrincipalsInDomain() throws JsonProcessingException {
        return principalService.principalsSelectToJson(principalService.findByDomain(PrincipalUtils.getCurrentDomain()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    Principal postCreatePrincipal(@RequestBody Principal principal, @RequestParam("domainId") String domainId) throws JsonProcessingException {
        AuthorizationProvider.hasRole(Role.ADMIN,domainId == null ? PrincipalUtils.getCurrentDomain() : domainService.findById(domainId));
        principal.setRole(Role.USER);
        if (domainId != null) {
            principal.setDomain(domainService.findById(domainId));
        } else {
            principal.setDomain(PrincipalUtils.getCurrentDomain());
        }
        principal.setCountry(PrincipalUtils.getCurrentPrincipal().getCountry());
        principal.setPhone("12");
        principal.setCreationDate(new Date());
        return principalService.save(principal);
    }

    @RequestMapping(value = "/{id}", params = {"active"}, method = RequestMethod.POST)
    public
    @ResponseBody
    List<Principal> postChangeActive(@PathVariable("id") String id, @RequestParam("active") boolean active) {
        return principalService.changeActive(id, active);
    }

    @RequestMapping(value = "/{id}", params = {"role"}, method = RequestMethod.PUT)
    public
    @ResponseBody
    List<Principal> purChangeRole(@PathVariable("id") String id, @RequestParam("role") Role role) {
        return principalService.changeRole(id,role);
    }

    @RequestMapping(value = "/current",method = RequestMethod.GET)
    public @ResponseBody String getCurrentPrincipal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Principal.class, PrincipalMixin.class);
        return mapper.writeValueAsString(PrincipalUtils.getCurrentPrincipal());
    }

}
