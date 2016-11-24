package pl.iticity.dbfds.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.repository.ScopedRepository;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

public interface PICRepository extends ScopedRepository<ProductInformationCarrier,String>, ClassifiableRepository<ProductInformationCarrier> {

    public List<ProductInformationCarrier> findByDomainAndRemovedIsFalseAndProductIdLike(Domain domain, String pId);

}
