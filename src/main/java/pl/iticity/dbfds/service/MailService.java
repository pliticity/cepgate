package pl.iticity.dbfds.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
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
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.StringWriter;
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
    private DocumentInfoRepository documentInfoRepository;

    @Autowired
    private FileService fileService;

    public void sendDocument(String docId, String[] files, boolean zip,HttpServletRequest request) {
        //DocumentInfo doc = documentService.findById(docId);
        //AuthorizationProvider.isInDomain(doc.getDomain());
        MimeMessage mail = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setFrom(defaultConfig.getSmtpFrom());
            helper.setTo(PrincipalUtils.getCurrentPrincipal().getEmail());
            helper.setSubject("This is an e-mail generated from Cepgate.");

            StringBuilder text = new StringBuilder("This is an e-mail generated from Cepgate.");
            for (String f : files) {
                DocumentInfo doc = documentInfoRepository.findByFiles_Id(f);
                text.append(fillTemplate(doc,request));
            }
            helper.setText(text.toString(),true);

            if (!zip) {
                for (String f : files) {
                    FileInfo fileInfo = fileService.findById(f);
                    File file = fileService.getFileForFileInfo(fileInfo);
                    helper.addAttachment(fileInfo.getName(), file);
                }
            }else{
               // FileInfo fileInfo = fileService.zipFiles(files);
             //   String msg = MessageFormat.format("<a href=\"http://webapp-cepgate.rhcloud.com/file/{0}?temp=true\">link to files</a> this is a one time link",fileInfo.getSymbol());
             //   helper.setText(msg,true);
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        mailSender.send(mail);
    }

    private String fillTemplate(DocumentInfo documentInfo,HttpServletRequest request){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        ///home/pmajchrz/other/dbfds/dbfds/src/main/
        Template t = ve.getTemplate( "/templates/mail/files.vm" );
        VelocityContext context = new VelocityContext();
        context.put("classificationId", documentInfo.getClassification().getClassificationId());
        context.put("classificationName", documentInfo.getClassification().getName());
        context.put("documentTitle", documentInfo.getDocumentName());
        context.put("documentNumber", documentInfo.getDocumentNumber());
        context.put("revision", documentInfo.getRevision().getEffective());
        context.put("link",generateLink(documentInfo,request));
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

    private String generateLink(DocumentInfo documentInfo,HttpServletRequest request){
        String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
        base+="document#/dic/"+documentInfo.getId();
        return base;
    }
}
