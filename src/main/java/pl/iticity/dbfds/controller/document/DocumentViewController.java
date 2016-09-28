package pl.iticity.dbfds.controller.document;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/document")
public class DocumentViewController extends ViewController {
    @Override
    public String getViewName() {
        return "document";
    }
}
