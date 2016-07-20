package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectViewController extends ViewController {

    @Override
    public String getViewName() {
        return "project";
    }
}
