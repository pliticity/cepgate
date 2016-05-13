package pl.iticity.dbfds.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;

@Controller
public class AdminClientUsersController extends BaseController {


    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/admin/clientusers/save")
    public
    @ResponseBody
    HashMap save(@RequestBody Principal principal) {
        principalService.save(principal);
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/clientusers/delete")
    public
    @ResponseBody
    HashMap deleteUser(@RequestBody Principal principal, HttpSession session) {
        HashMap toReturn = new HashMap();
        principalService.delete(principal);
        return toReturn;
    }

    @RequestMapping("/admin/clientusers/edit")
    public
    @ResponseBody
    HashMap edit(String id) {
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping("/admin/clientusers/list")
    public
    @ResponseBody
    HashMap<String, Object> list(@RequestBody Domain account, HttpSession session) {

        HashMap<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("result", "success");
        toReturn.put("data", principalService.findByDomain(account));
        return toReturn;
    }

    @RequestMapping("/admin/clientusers/index")
    public String index() {
        return "member/clientusers/index";
    }
}
