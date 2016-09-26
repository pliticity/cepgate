package pl.iticity.dbfds.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/admin")
public class AdminViewController extends ViewController {
    @Override
    public String getViewName() {
        return "admin";
    }
}
