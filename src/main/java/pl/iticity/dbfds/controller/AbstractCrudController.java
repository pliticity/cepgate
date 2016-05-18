package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.service.AbstractService;

import java.util.List;

public abstract class AbstractCrudController<MODEL, SERVICE extends AbstractService> {

    @Autowired
    protected SERVICE service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody MODEL postCreate(@RequestBody MODEL model) {
        return model;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<MODEL> getAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody MODEL getOne(@PathVariable String id) {
        return (MODEL) service.findById(id);
    }

    public SERVICE getService() {
        return service;
    }

    public void setService(SERVICE service) {
        this.service = service;
    }
}
