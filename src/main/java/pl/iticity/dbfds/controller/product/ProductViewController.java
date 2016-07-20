package pl.iticity.dbfds.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.iticity.dbfds.controller.ViewController;

@Controller
@RequestMapping("/product")
public class ProductViewController extends ViewController {

    @Override
    public String getViewName() {
        return "product";
    }
}
