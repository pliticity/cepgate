package pl.iticity.dbfds.service.product;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.service.Service;

import java.util.List;

public interface PICService extends Service<ProductInformationCarrier> {

    public List<ProductInformationCarrier> findByDomain(Domain domain);

}
