package pl.iticity.dbfds.repository.quotation;

import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.repository.ScopedRepository;

public interface QICRepository extends ScopedRepository<QuotationInformationCarrier,String>,ClassifiableRepository<QuotationInformationCarrier>{
}
