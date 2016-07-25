package pl.iticity.dbfds.repository;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

public interface ScopedRepository<MODEL extends Scoped> {

    public List<MODEL> findByDomainAndRemovedIsFalse(Domain domain);

    public List<MODEL> findByDomainAndPrincipalAndRemovedIsFalse(Domain domain, Principal principal);

    public MODEL findOneByDomainAndPrincipalAndRemovedIsFalse(Domain domain, Principal principal);
}
