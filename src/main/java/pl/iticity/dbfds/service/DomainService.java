package pl.iticity.dbfds.service;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.DomainRepository;

@Service
public class DomainService extends AbstractService<Domain,DomainRepository> {

    public Domain findByName(String name){
        return repo.findByName(name);
    }

}
