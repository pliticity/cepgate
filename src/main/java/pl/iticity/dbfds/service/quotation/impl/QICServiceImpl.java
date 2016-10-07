package pl.iticity.dbfds.service.quotation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.quotation.QICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.ClassificationHelper;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.quotation.QICService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class QICServiceImpl extends AbstractScopedService<QuotationInformationCarrier,String,QICRepository> implements QICService{

    @Autowired
    private DomainService domainService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClassificationHelper classificationHelper;

    @Override
    public QuotationInformationCarrier saveQIC(QuotationInformationCarrier qic) {
        qic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        qic.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(qic);
        classificationHelper.createModelClassification(PrincipalUtils.getCurrentDomain(),PrincipalUtils.getCurrentPrincipal(),ClassificationType.QUOTATION,qic.getName(),qic.getSymbol(),qic.getId(),QuotationInformationCarrier.class,qic.getClassification());
        return qic;
    }

    @Override
    public QuotationInformationCarrier newQIC() {
        QuotationInformationCarrier qic = new QuotationInformationCarrier();
        qic.setMasterNumber(MessageFormat.format("QIC-{0}",getNextMasterNumber(PrincipalUtils.getCurrentDomain())));
        qic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        qic.setCreationDate(new Date());
        return qic;
    }

    @Override
    public Long getNextMasterNumber(Domain domain) {
        Domain d = domainService.findById(domain.getId());
        long id = d.getLastMasterQuotationNumber() +1;
        d.setLastMasterQuotationNumber(id);
        domainService.save(d);
        return id;
    }

}
