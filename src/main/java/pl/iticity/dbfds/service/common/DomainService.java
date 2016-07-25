package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.Domain;

public interface DomainService extends pl.iticity.dbfds.service.Service<Domain> {

    public Domain findByName(String name);

}
