package pl.iticity.dbfds.repository;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

public interface ScopedRepository<MODEL> {

    public List<MODEL> findByDomainAndRemovedIsFalse(Domain domain);

    public List<MODEL> findByDomainAndPrincipalAndRemovedIsFalse(Domain domain, Principal principal);

}
