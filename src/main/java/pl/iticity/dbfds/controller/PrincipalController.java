package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.PrincipalMixin;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping(value = "/principal")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DomainService domainService;

    @RequestMapping(value = "", params = {"id"})
    public String getPrincipalsInDomain(@RequestParam("id") String id) throws JsonProcessingException {
        Domain domain = org.apache.commons.lang.StringUtils.isNotEmpty(id) ? domainService.findById(id) : PrincipalUtils.getCurrentDomain();
        return principalService.principalsSelectToJson(principalService.findByDomain(domain));
    }

    @RequestMapping(value = "")
    public String getPrincipalsInDomain() throws JsonProcessingException {
        Domain domain = PrincipalUtils.getCurrentDomain();
        return principalService.principalsSelectToJson(principalService.findByDomain(domain));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Principal postCreatePrincipal(@RequestBody Principal principal, @RequestParam("domainId") String domainId) throws JsonProcessingException {
        return principalService.addPrincipal(principal, domainId);
    }

    @RequestMapping(value = "/{id}", params = {"active"}, method = RequestMethod.POST)
    public List<Principal> postChangeActive(@PathVariable("id") String id, @RequestParam("active") boolean active) {
        return principalService.changeActive(id, active);
    }

    @RequestMapping(value = "/{id}", params = {"role"}, method = RequestMethod.PUT)
    public List<Principal> purChangeRole(@PathVariable("id") String id, @RequestParam("role") Role role) {
        return principalService.changeRole(id, role);
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public String getCurrentPrincipal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Principal.class, PrincipalMixin.class);
        return mapper.writeValueAsString(PrincipalUtils.getCurrentPrincipal());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public List<Principal> postCreatePrincipal(@RequestBody Principal principal) throws JsonProcessingException {
        Principal dbPrincipal = principalService.findById(principal.getId());
        AuthorizationProvider.assertRole(Role.ADMIN, dbPrincipal.getDomain());
        dbPrincipal.setFirstName(principal.getFirstName());
        dbPrincipal.setLastName(principal.getLastName());
        dbPrincipal.setAcronym(principal.getAcronym());
        principalService.save(dbPrincipal);
        return principalService.findByDomain(dbPrincipal.getDomain());
    }

    @RequestMapping(value = "/acronym", method = RequestMethod.GET)
    public boolean getExists(@RequestParam(name = "id") String id, @RequestParam(name = "q") String acronym) {
        return principalService.acronymExistsInDomain(id, acronym, PrincipalUtils.getCurrentDomain());
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public boolean postPassword(@RequestBody Principal principal) {
        principalService.changePassword(principal);
        return true;
    }

}
