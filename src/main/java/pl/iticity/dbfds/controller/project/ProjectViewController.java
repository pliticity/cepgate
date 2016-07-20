package pl.iticity.dbfds.controller.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/project")
public class ProjectViewController extends ViewController {

    @Override
    public String getViewName() {
        return "project";
    }
}
