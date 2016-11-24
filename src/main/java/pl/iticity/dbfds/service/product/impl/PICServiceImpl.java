package pl.iticity.dbfds.service.product.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.product.ProductState;
import pl.iticity.dbfds.repository.product.PICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.ClassificationHelper;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.product.PICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class PICServiceImpl extends AbstractScopedService<ProductInformationCarrier, String, PICRepository> implements PICService {

    @Autowired
    private DomainService domainService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClassificationHelper classificationHelper;

    @Override
    public ProductInformationCarrier savePIC(ProductInformationCarrier pic) {
        pic.setDomain(PrincipalUtils.getCurrentDomain());
        pic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(pic);
        classificationHelper.createModelClassification(PrincipalUtils.getCurrentDomain(), PrincipalUtils.getCurrentPrincipal(), ClassificationType.PRODUCT_MODEL, pic.getName(), pic.getProductId(), pic.getId(), ProductInformationCarrier.class, pic.getClassification());
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
        long id = d.getLastMasterProductNumber() + 1;
        d.setLastMasterProductNumber(id);
        domainService.save(d);
        return MessageFormat.format("P{0}", id);
    }

    @Override
    public String autoCompleteProduct(String pId) throws JsonProcessingException {
        List<ProductInformationCarrier> products = repo.findByDomainAndRemovedIsFalseAndProductIdLike(PrincipalUtils.getCurrentDomain(), pId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(products);
    }

}
