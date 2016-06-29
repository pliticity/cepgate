package pl.iticity.dbfds.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.MessageFormat;

@Service
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DefaultConfig defaultConfig;

    @Autowired
    private FileService fileService;

    public void sendDocument(String docId, String[] files, boolean zip) {
        //DocumentInfo doc = documentService.findById(docId);
        //AuthorizationProvider.isInDomain(doc.getDomain());
        MimeMessage mail = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setFrom(defaultConfig.getSmtpFrom());
            helper.setTo(PrincipalUtils.getCurrentPrincipal().getEmail());
            helper.setSubject("files");

            if (!zip) {
                helper.setText("files");

                for (String f : files) {
                    FileInfo fileInfo = fileService.findById(f);
                    File file = fileService.getFileForFileInfo(fileInfo);
                    helper.addAttachment(fileInfo.getName(), file);
                }
            }else{
                FileInfo fileInfo = fileService.zipFiles(files);
                String msg = MessageFormat.format("<a href=\"http://webapp-cepgate.rhcloud.com/file/{0}?temp=true\">link to files</a> this is a one time link",fileInfo.getSymbol());
                helper.setText(msg,true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        mailSender.send(mail);
    }
}
