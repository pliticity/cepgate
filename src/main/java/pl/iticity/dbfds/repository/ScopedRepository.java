package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.security.Principal;

import java.io.Serializable;
import java.util.List;

public interface ScopedRepository<MODEL extends Scoped, ID extends Serializable> extends MongoRepository<MODEL,ID> {

    public List<MODEL> findByDomainAndRemovedIsFalse(Domain domain);

    public List<MODEL> findByDomainAndPrincipalAndRemovedIsFalse(Domain domain, Principal principal);

    public MODEL findOneByDomainAndPrincipalAndRemovedIsFalse(Domain domain, Principal principal);
}
