package pl.iticity.dbfds.service.product;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.ScopedService;

import java.util.List;

public interface PICService extends ScopedService<ProductInformationCarrier> {

    public ProductInformationCarrier savePIC(ProductInformationCarrier pic);

    public ProductInformationCarrier createNew();

    public Long getNextMasterProductNumber(Domain domain);

    public List<ProductInformationCarrier> findByDomain(Domain domain);

    public List<ProductInformationCarrier> findByDomainAndPrincipal(Domain domain, Principal principal);

}
