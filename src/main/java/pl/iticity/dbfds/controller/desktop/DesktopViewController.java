package pl.iticity.dbfds.controller.desktop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/desktop")
public class DesktopViewController extends ViewController {
    @Override
    public String getViewName() {
        return "desktop";
    }
}
