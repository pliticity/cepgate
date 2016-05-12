package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class AbstractService<MODEL,REPO extends MongoRepository<MODEL,String>> {

    @Autowired
    protected REPO repo;

    public MODEL findById(String id){
        return repo.findOne(id);
    }

}
