package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class DashboardController extends ViewController {

    @Override
    public String getViewName() {
        return "dashboard";
    }
}
