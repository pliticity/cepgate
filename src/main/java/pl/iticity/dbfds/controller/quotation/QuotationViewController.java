package pl.iticity.dbfds.controller.quotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/quotation")
public class QuotationViewController extends ViewController {

    @Override
    public String getViewName() {
        return "quotation";
    }
}
