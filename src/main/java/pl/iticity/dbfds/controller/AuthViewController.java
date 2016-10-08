package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.iticity.dbfds.service.common.PrincipalService;

@Controller
@RequestMapping(value = "/auth")
public class AuthViewController extends ViewController {

    @Autowired
    private PrincipalService principalService;

    @Override
    public String getViewName() {
        return "auth";
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public String getSignOut() {
        principalService.unAuthenticate();
        return "redirect:/document";
    }
}
