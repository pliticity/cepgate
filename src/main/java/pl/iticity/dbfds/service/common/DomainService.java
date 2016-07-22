package pl.iticity.dbfds.service.common;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.DomainRepository;
import pl.iticity.dbfds.service.AbstractService;

@Service
public class DomainService extends AbstractService<Domain,DomainRepository> {

    public Domain findByName(String name){
        return repo.findByName(name);
    }

}
