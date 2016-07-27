package pl.iticity.dbfds.service.quotation;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.service.ScopedService;

public interface QICService extends ScopedService<QuotationInformationCarrier>{

    public QuotationInformationCarrier saveQIC(QuotationInformationCarrier qic);

}
