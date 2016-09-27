package pl.iticity.dbfds.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.product.ProductState;
import pl.iticity.dbfds.repository.product.PICRepository;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class PICServiceImpl extends AbstractScopedService<ProductInformationCarrier,String, PICRepository> implements PICService {

    @Autowired
    private DomainService domainService;

    @Autowired
    private ClassificationService classificationService;

    @Override
    public ProductInformationCarrier savePIC(ProductInformationCarrier pic) {
        pic.setDomain(PrincipalUtils.getCurrentDomain());
        pic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(pic);
        if (pic.getProductId() != null && !classificationService.exists(pic.getProductId(), null)) {
            Classification classification = new Classification();
            classification.setId("-1");
            classification.setDomain(pic.getDomain());
            classification.setActive(true);
            classification.setPrincipal(pic.getPrincipal());
            classification.setRemoved(false);
            classification.setType("Product Model");
            classification.setName(pic.getName() != null ? pic.getName() : "PRODUCT HAD NO NAME");
            classification.setClassificationId(pic.getProductId() != null ? pic.getProductId() : "PRODUCT HAD NO ID");
            classification.setModelId(pic.getId());
            classification.setModelClazz(ProductInformationCarrier.class.getName());
            classificationService.addClassification(classification, pic.getDomain());
        }
        return pic;
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
    public String getNextMasterProductNumber(Domain domain) {
        Domain d = domainService.findById(domain.getId());
        long id = d.getLastMasterProductNumber() +1;
        d.setLastMasterProductNumber(id);
        domainService.save(d);
        return MessageFormat.format("P{0}",id);
    }

}
