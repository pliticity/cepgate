package pl.iticity.dbfds.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DocumentTypeService;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.FileService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/domain")
public class DomainAdminController {

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
        return SecurityUtils.getSubject().hasRole(Role.ADMIN.name());
    }

    @RequestMapping(value = "", params = {"isGlobalAdmin"})
    public
    @ResponseBody
    boolean getIsGlobalAdmin() {
        return SecurityUtils.getSubject().hasRole(Role.GLOBAL_ADMIN.name());
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
        domain.setNoOfFiles(fileService.countByDomain(domain));
        domain.setMemory(fileService.countMemoryByDomain(domain));
        domain.setNoOfUsers(domain.getPrincipals().size());
        return domain;
    }

    @RequestMapping(value = "/docType",method = RequestMethod.POST)
    public
    @ResponseBody
    List<DocumentType> postDocumentType(@RequestBody DocumentType documentType) {
        return documentTypeService.addDocType(documentType,PrincipalUtils.getCurrentDomain());
    }

    @RequestMapping(value = "/docType/{id}",method = RequestMethod.PUT,params = {"toggle"})
    public
    @ResponseBody
    List<DocumentType> deleteDocType(@PathVariable("id") String id,@RequestParam("toggle") boolean toggle) {
        return documentTypeService.toggleDocType(id,toggle);
    }
}
