package pl.iticity.dbfds.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.product.ProductState;
import pl.iticity.dbfds.repository.product.PICRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class PICServiceImpl extends AbstractService<ProductInformationCarrier, PICRepository> implements PICService {

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
        String idNumber = MessageFormat.format("{0}-{1}",PrincipalUtils.getCurrentDomain().getAccountNo(),pic.getMasterProductNumber());
        pic.setProductIdNumber(idNumber);
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

    @Override
    public List<ProductInformationCarrier> findByDomain(Domain domain) {
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    @Override
    public List<ProductInformationCarrier> findByDomainAndPrincipal(Domain domain, Principal principal) {
        return repo.findByDomainAndPrincipalAndRemovedIsFalse(domain,principal);
    }

}
