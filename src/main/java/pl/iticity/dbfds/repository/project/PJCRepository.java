package pl.iticity.dbfds.repository.project;

import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.repository.ScopedRepository;

public interface PJCRepository extends ScopedRepository<ProjectInformationCarrier,String>, ClassifiableRepository<ProjectInformationCarrier>{
}
