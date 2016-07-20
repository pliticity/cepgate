package pl.iticity.dbfds.service.project.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.repository.project.PICRepository;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.project.PICService;

import java.util.List;

@Service
public class PICServiceImpl extends AbstractService<ProductInformationCarrier, PICRepository> implements PICService {

    @Override
    public List<ProductInformationCarrier> findByDomain(Domain domain) {
        return repo.findByDomain(domain);
    }

}
