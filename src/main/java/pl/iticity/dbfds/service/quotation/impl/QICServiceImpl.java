package pl.iticity.dbfds.service.quotation.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.repository.quotation.QICRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.quotation.QICService;
import pl.iticity.dbfds.util.PrincipalUtils;

@Service
public class QICServiceImpl extends AbstractScopedService<QuotationInformationCarrier,String,QICRepository> implements QICService{

    @Override
    public QuotationInformationCarrier saveQIC(QuotationInformationCarrier qic) {
        qic.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        qic.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(qic);
        return qic;
    }

}
