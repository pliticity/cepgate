package pl.iticity.dbfds.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.repository.ScopedRepository;
import pl.iticity.dbfds.security.Principal;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractScopedService<MODEL extends Scoped, ID extends Serializable ,REPO extends ScopedRepository<MODEL,ID>> extends AbstractService<MODEL,ID,REPO> implements ScopedService<MODEL> {

    @Override
    public List<MODEL> findByDomain(Domain domain) {
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    @Override
    public List<MODEL> findByDomainAndPrincipal(Domain domain, Principal principal) {
        return repo.findByDomainAndPrincipalAndRemovedIsFalse(domain,principal);
    }

    @Override
    public MODEL findOneByDomainAndPrincipal(Domain domain, Principal principal) {
        return repo.findOneByDomainAndPrincipalAndRemovedIsFalse(domain,principal);
    }

}
