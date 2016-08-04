package pl.iticity.dbfds.service.project.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.project.PJCRepository;
import pl.iticity.dbfds.repository.quotation.QICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
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

    @Override
    public ProjectInformationCarrier savePJC(ProjectInformationCarrier pjc) {
        pjc.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        pjc.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(pjc);
        if (pjc.getSymbol() != null && !classificationService.exists(pjc.getSymbol(), null)) {
            Classification classification = new Classification();
            classification.setId("-1");
            classification.setDomain(pjc.getDomain());
            classification.setActive(true);
            classification.setPrincipal(pjc.getPrincipal());
            classification.setRemoved(false);
            classification.setType("Project");
            classification.setName(pjc.getName() != null ? pjc.getName() : "PROJECT HAD NO NAME");
            classification.setClassificationId(pjc.getSymbol());
            classification.setModelId(pjc.getId());
            classification.setModelClazz(ProjectInformationCarrier.class.getName());
            classificationService.addClassification(classification, pjc.getDomain());
        }
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
