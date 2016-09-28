package pl.iticity.dbfds.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.document.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.DomainOwnerMixin;
import pl.iticity.dbfds.model.mixins.PrincipalSelectMixin;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping("/domain")
public class DomainController extends BaseController{

    @Autowired
    private FileService fileService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @RequestMapping(value = "", params = {"isAdmin"})
    public
    @ResponseBody
    boolean getIsAdmin() {
        return SecurityUtils.getSubject().isAuthenticated() && SecurityUtils.getSubject().hasRole(Role.ADMIN.name());
    }

    @RequestMapping(value = "", params = {"isGlobalAdmin"})
    public
    @ResponseBody
    boolean getIsGlobalAdmin() {
        return SecurityUtils.getSubject().isAuthenticated() && SecurityUtils.getSubject().hasRole(Role.GLOBAL_ADMIN.name());
    }

    @RequestMapping(value = "", params = {"domain"})
    public
    @ResponseBody
    Domain getDomain() throws IllegalAccessException {
        if(SecurityUtils.getSubject().isAuthenticated()){
            return PrincipalUtils.getCurrentDomain();
        }
        return null;
    }

    @RequestMapping("/{id}")
    public
    @ResponseBody
    String getDomain(@PathVariable("id") String id) throws JsonProcessingException {
        Domain domain = domainService.findById(id);
        domain.setPrincipals(principalService.findByDomain(domain));
        domain.setNoOfFiles(fileService.countByDomain(domain));
        domain.setMemory(fileService.countMemoryByDomain(domain));
        domain.setNoOfUsers(domain.getPrincipals().size());

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Principal.class, DomainOwnerMixin.class);
        Principal owner = principalService.findByEmail(domain.getName());
        domain.setOwner(owner);
        return mapper.writeValueAsString(domain);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String saveDomain(@RequestBody Domain domain){
        domain = domainService.patch(domain);
        return convertToString(Domain.class,Domain.class,domain);
    }

    @RequestMapping(value = "/{id}",params = {"newOwner"},method = RequestMethod.POST)
    public String saveDomain(@PathVariable("id") String id, @RequestParam("newOwner") String newOwner){
        Principal principal = domainService.changeSU(id,newOwner);
        return convertToString(Principal.class, PrincipalSelectMixin.class,principal);
    }

}
