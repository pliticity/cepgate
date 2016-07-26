package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.security.Principal;

import java.io.File;
import java.io.IOException;

public interface TransmittalService {

    public File createTransmittal(Principal sender, Principal recipient, DocumentInfo documentInfo) throws IOException;

}
