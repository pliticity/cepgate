package pl.iticity.dbfds.service;


import java.util.List;

public interface Service<MODEL> {

    public List<MODEL> findAll();

    public MODEL save(MODEL model);

    public void delete(MODEL model);

    public MODEL findById(String id);

}
