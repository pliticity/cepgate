package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;

public interface DomainService extends pl.iticity.dbfds.service.Service<Domain> {

    public Domain findByName(String name);

    public Domain patch(Domain domain);

    public Principal changeSU(String domainId, String principalId);

}
