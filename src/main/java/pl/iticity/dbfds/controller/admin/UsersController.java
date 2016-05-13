package pl.iticity.dbfds.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;

import java.util.HashMap;
import java.util.List;

@Controller
public class UsersController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/admin/users/save")
    public
    @ResponseBody
    HashMap save(@RequestBody Principal principal) {
        principalService.save(principal);
        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/users/delete")
    public
    @ResponseBody
    HashMap deleteUser(@RequestBody Principal account) {
        HashMap toReturn = new HashMap();
        if (account != null) {
            principalService.delete(account);
        }
        return toReturn;
    }

    @RequestMapping("/admin/users/edit")
    public
    @ResponseBody
    HashMap edit(String id) {
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping("/admin/users/list")
    public
    @ResponseBody
    List<Principal> list() {
        return principalService.findAll();
    }

    @RequestMapping("/admin/users/index")
    public String index() {
        return "admin/users/index";
    }
}
