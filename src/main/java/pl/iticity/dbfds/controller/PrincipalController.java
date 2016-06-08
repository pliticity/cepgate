package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping(value = "/principal")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping(value = "")
    public
    @ResponseBody
    String getPrincipalsInDomain() throws JsonProcessingException {
        return principalService.principalsSelectToJson(principalService.findByDomain(PrincipalUtils.getCurrentDomain()));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public
    @ResponseBody
    Principal postCreatePrincipal(@RequestBody Principal principal) throws JsonProcessingException {
        principal.setRole(Role.CLIENT);
        principal.setDomain(PrincipalUtils.getCurrentDomain());
        principal.setCountry(PrincipalUtils.getCurrentPrincipal().getCountry());
        principal.setPhone("12");
        return principalService.save(principal);
    }

}
