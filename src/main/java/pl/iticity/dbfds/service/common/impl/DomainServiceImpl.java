package pl.iticity.dbfds.service.common.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.common.DomainRepository;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.DomainService;

@Service
public class DomainServiceImpl extends AbstractService<Domain,DomainRepository> implements DomainService {

    public Domain findByName(String name){
        return repo.findByName(name);
    }

}
