package pl.iticity.dbfds.service.project.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.project.PJCRepository;
import pl.iticity.dbfds.repository.quotation.QICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.ClassificationHelper;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.project.PJCService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class PJCServiceImpl extends AbstractScopedService<ProjectInformationCarrier,String,PJCRepository> implements PJCService {

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private ClassificationHelper classificationHelper;

    @Override
    public ProjectInformationCarrier savePJC(ProjectInformationCarrier pjc) {
        pjc.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        pjc.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(pjc);
        classificationHelper.createModelClassification(PrincipalUtils.getCurrentDomain(),PrincipalUtils.getCurrentPrincipal(),ClassificationType.PROJECT,pjc.getName(),pjc.getSymbol(),pjc.getId(),ProjectInformationCarrier.class,pjc.getClassification());
        return pjc;
    }

    @Override
    public ProjectInformationCarrier newPJC() {
        ProjectInformationCarrier pjc = new ProjectInformationCarrier();
        pjc.setMasterNumber(MessageFormat.format("PJC-{0}",getNextMasterNumber(PrincipalUtils.getCurrentDomain())));
        pjc.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        pjc.setCreationDate(new Date());
        return pjc;
    }

    @Override
    public Long getNextMasterNumber(Domain domain) {
        Domain d = domainService.findById(domain.getId());
        long id = d.getLastMasterProjectNumber() +1;
        d.setLastMasterProjectNumber(id);
        domainService.save(d);
        return id;
    }
}
