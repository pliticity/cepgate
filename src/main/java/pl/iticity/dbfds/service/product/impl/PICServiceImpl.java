package pl.iticity.dbfds.service.product.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.repository.product.PICRepository;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.product.PICService;

import java.util.List;

@Service
public class PICServiceImpl extends AbstractService<ProductInformationCarrier, PICRepository> implements PICService {

    @Override
    public List<ProductInformationCarrier> findByDomain(Domain domain) {
        return repo.findByDomain(domain);
    }

}
