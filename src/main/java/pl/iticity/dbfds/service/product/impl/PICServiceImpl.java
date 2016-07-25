package pl.iticity.dbfds.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.product.ProductState;
import pl.iticity.dbfds.repository.product.PICRepository;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class PICServiceImpl extends AbstractScopedService<ProductInformationCarrier, PICRepository> implements PICService {

    @Autowired
    private DomainService domainService;

    @Override
    public ProductInformationCarrier savePIC(ProductInformationCarrier pic) {
        pic.setDomain(PrincipalUtils.getCurrentDomain());
        pic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        return repo.save(pic);
    }

    @Override
    public ProductInformationCarrier createNew() {
        ProductInformationCarrier pic = new ProductInformationCarrier();
        pic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        pic.setCreationDate(new Date());
        pic.setState(ProductState.IN_PROGRESS);
        pic.setMasterProductNumber(getNextMasterProductNumber(PrincipalUtils.getCurrentDomain()));
        return pic;
    }

    @Override
    public Long getNextMasterProductNumber(Domain domain) {
        Domain d = domainService.findById(domain.getId());
        long id = d.getLastMasterProductNumber() +1;
        d.setLastMasterProductNumber(id);
        domainService.save(d);
        return id;
    }

}
