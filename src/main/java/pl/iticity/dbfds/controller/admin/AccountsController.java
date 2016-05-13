package pl.iticity.dbfds.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.service.DomainService;

import java.util.HashMap;
import java.util.List;

@Controller
public class AccountsController {

    @Value("${file.base}")
    private String filePath;

    @Autowired
    private DomainService domainService;

    @RequestMapping("/admin/accounts/save")
    public
    @ResponseBody
    HashMap save(@RequestBody Domain account) {
        HashMap toReturn = new HashMap();
        domainService.save(account);
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/accounts/delete")
    public
    @ResponseBody
    HashMap deleteAccount(@RequestBody Domain domain) {
        HashMap toReturn = new HashMap();
        domainService.delete(domain);
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/accounts/toggle")
    public
    @ResponseBody
    HashMap toggleAccount(@RequestBody Domain domain) {
        HashMap toReturn = new HashMap();
        domain.setActive(!domain.isActive());
        domainService.save(domain);
        return toReturn;
    }

    @RequestMapping("/admin/accounts/edit")
    public
    @ResponseBody
    HashMap editAccount(String id) {
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping("/admin/accounts/list")
    public
    @ResponseBody
    List<Domain> list() {
        return domainService.findAll();
    }

    @RequestMapping("/admin/accounts/index")
    public String index() {

        return "admin/accounts/index";
    }
}
