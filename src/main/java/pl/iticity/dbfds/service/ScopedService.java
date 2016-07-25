package pl.iticity.dbfds.service;


import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

public interface ScopedService<MODEL extends Scoped> extends Service<MODEL>{

    public List<MODEL> findByDomain(Domain domain);

    public List<MODEL> findByDomainAndPrincipal(Domain domain, Principal principal);

    public MODEL findOneByDomainAndPrincipal(Domain domain, Principal principal);

}
