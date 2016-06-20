package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @RequestMapping("")
    public String home() {
        return "dashboard";
    }

}
