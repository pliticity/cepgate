package pl.iticity.dbfds.service.project.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.project.PJCRepository;
import pl.iticity.dbfds.repository.quotation.QICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.project.PJCService;
import pl.iticity.dbfds.util.PrincipalUtils;

@Service
public class PJCServiceImpl extends AbstractScopedService<ProjectInformationCarrier,String,PJCRepository> implements PJCService {

    @Override
    public ProjectInformationCarrier savePJC(ProjectInformationCarrier pjc) {
        pjc.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        pjc.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(pjc);
        return pjc;
    }
}
