package pl.iticity.dbfds.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller

public class SignupController extends BaseController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/signup/index")
    public String index() {
        return "signup/index";
    }

    @RequestMapping("/signup/login")
    public String login() {
        return "signup/login";
    }

    @RequestMapping(value = "/signup/login", method = RequestMethod.POST)
    public String postLogin(@RequestParam(name = "username") final String username, @RequestParam(name = "password") final String password) {
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
    HashMap signup(@RequestBody Principal principal) {
        HashMap mp = new HashMap();
        Principal p = principalService.findByEmail(principal.getEmail());
        if (p != null) {
            mp.put("result", "error");
            mp.put("message", "Account already exists.");
        } else {
            principalService.registerPrincipal(principal);
            //account.setDateCreated(new Date());
            //account.setPasswd(passwordEncoder.encode(account.getPasswd()));
            //repository.save(account);
            mp.put("result", "success");
            mp.put("message", "Success");
        }
        return mp;
    }


}
