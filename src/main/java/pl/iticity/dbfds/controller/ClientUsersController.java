package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.PrincipalService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class ClientUsersController extends BaseController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/member/clientusers/save")
    public
    @ResponseBody
    HashMap save(@RequestBody Principal principal, HttpSession session) {
        HashMap toReturn = new HashMap();
        Domain domain = getAuthenticatedAccount(session).getDomain();
        if (domain != null) {
            if (principalService.findByEmail(principal.getEmail()) != null) {
                toReturn.put("message", "Duplicate email address already exists.");
                toReturn.put("result", "fail");
            } else if (principal.getId() == null) {
                principal.setRole(Role.CLIENT);
                principal.setDomain(domain);
                principalService.registerPrincipal(principal);
                toReturn.put("result", "success");
            }
        }
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/clientusers/delete")
    public
    @ResponseBody
    HashMap deleteUser(@RequestBody Principal principal, HttpSession session) {
        HashMap toReturn = new HashMap();
        Domain domain = getAuthenticatedAccount(session).getDomain();

        if (domain != null) {
            toReturn.put("result", "success");
            principalService.delete(principal);

        }
        return toReturn;
    }

    @RequestMapping("/member/clientusers/edit")
    public
    @ResponseBody
    HashMap edit(String id) {
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping("/member/clientusers/list")
    public
    @ResponseBody
    HashMap<String, Object> list(HttpSession session) {
        Domain domain = getAuthenticatedAccount(session).getDomain();
        HashMap<String, Object> toReturn = new HashMap<String, Object>();
        if (domain != null) {
            //make sure they have access
            toReturn.put("result", "success");
            toReturn.put("data", principalService.findByDomain(domain));
        } else {
            toReturn.put("result", "fail");
            toReturn.put("message", "You do not have permissions for this area");

        }

        return toReturn;
    }

    @RequestMapping("/member/clientusers/index")
    public String index() {
        return "member/clientusers/index";
    }
}
