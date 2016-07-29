package pl.iticity.dbfds.service.project;

import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.service.ScopedService;

public interface PJCService extends ScopedService<ProjectInformationCarrier>{

    public ProjectInformationCarrier savePJC(ProjectInformationCarrier pjc);

}
