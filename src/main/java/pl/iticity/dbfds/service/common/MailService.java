package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.Mail;

import javax.servlet.http.HttpServletRequest;

public interface MailService {

    public void sendDocument(Mail email, boolean zip, HttpServletRequest request, boolean appendTransmittal);

}
