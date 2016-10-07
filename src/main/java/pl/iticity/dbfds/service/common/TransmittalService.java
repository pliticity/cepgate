package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.Mail;
import pl.iticity.dbfds.security.Principal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TransmittalService {

    public File createTransmittal(Principal sender, Principal recipient, List<DocumentInformationCarrier> documentInformationCarrier, Mail mail) throws IOException;

}
