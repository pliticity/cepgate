package pl.iticity.dbfds.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DocumentService;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.PrincipalService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller

public class SignupController extends BaseController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DomainService domainService;

    @RequestMapping("/signup/index")
    public String index() {
        return "signup/index";
    }

    @RequestMapping("/signup/login")
    public String login() {
        return "signup/login";
    }

    @RequestMapping(value = "/signup/login", method = RequestMethod.POST)
    public String postLogin(@RequestParam(name = "username") final String username, @RequestParam(name = "password") final String password, HttpServletResponse response) {
        SecurityUtils.getSubject().login(new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return new Principal(username, password);
            }

            @Override
            public Object getCredentials() {
                return password;
            }
        });
        return "signup/login";
    }

    @RequestMapping("/signup/authenticated")
    public
    @ResponseBody
    HashMap authenticated(HttpSession session) {
        HashMap toReturn = new HashMap();
        toReturn.put("result", "true");
        toReturn.put("firstName", "");
        toReturn.put("lastName", "");
        return toReturn;
    }

    @RequestMapping("/signup/signup")
    public
    @ResponseBody
    HashMap signup(Principal principal) {
        HashMap mp = new HashMap();
        Principal p = principalService.findByEmail(principal.getEmail());
        Domain d = domainService.findByName(principal.getEmail());
        if (p != null && d!=null) {
            mp.put("result", "error");
            mp.put("message", "Account already exists.");
        } else {
            Domain domain = new Domain();
            domain.setName(principal.getEmail());
            domainService.save(domain);
            principal.setDomain(domain);
            principal.setRole(Role.ADMIN);
            principalService.registerPrincipal(principal);
            mp.put("result", "success");
            mp.put("message", "Success");
        }
        return mp;
    }


}
