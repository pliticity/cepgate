package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<MODEL,ID extends Serializable,REPO extends MongoRepository<MODEL,ID>> implements Service<MODEL> {

    @Autowired
    protected REPO repo;

    public List<MODEL> findAll(){
        return repo.findAll();
    }

    public MODEL save(MODEL model){
        return repo.save(model);
    }

    public void delete(MODEL model){
        repo.delete(model);
    }

    public MODEL findById(ID id){
        return repo.findOne(id);
    }

}
