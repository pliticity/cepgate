package pl.iticity.dbfds.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;

import java.util.List;

public interface PICRepository extends MongoRepository<ProductInformationCarrier,String> {

    public List<ProductInformationCarrier> findByDomain(Domain domain);

}
