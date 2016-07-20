package pl.iticity.dbfds.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class ViewController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getView() {
        return getViewName();
    }

    public abstract String getViewName();

}
