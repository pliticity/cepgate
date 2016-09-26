package pl.iticity.dbfds.controller.gadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/gadmin")
public class GAdminViewController extends ViewController {

    @Override
    public String getViewName() {
        return "gadmin";
    }

}
