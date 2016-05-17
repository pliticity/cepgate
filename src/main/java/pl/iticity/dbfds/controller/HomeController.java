package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "auth";
    }

    @RequestMapping("/home")
    public String h() {
        return "index";
    }

}
