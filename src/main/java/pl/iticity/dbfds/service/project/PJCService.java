package pl.iticity.dbfds.service.project;

import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.ScopedService;

import java.util.List;

public interface PJCService extends ScopedService<ProjectInformationCarrier>{

    public ProjectInformationCarrier savePJC(ProjectInformationCarrier pjc);

    public ProjectInformationCarrier newPJC();

    public Long getNextMasterNumber(Domain domain);

}
