package pl.iticity.dbfds.integration.mail;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.mail.imap.IMAPMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.*;
import pl.iticity.dbfds.util.DefaultConfig;

import javax.annotation.Nullable;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MailActivator{

    @Autowired
    private DefaultConfig defaultConfig;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private FileService fileService;

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = Logger.getLogger(MailActivator.class);

    public void activate(org.springframework.messaging.Message<?> message) throws MessagingException, IOException {
        Message payload = ((Message)message.getPayload());
        String from = ((InternetAddress)payload.getFrom()[0]).getAddress();
        Principal principal = principalService.findByEmail(from);
        if(principal!=null){
            DocumentInfo doc = new DocumentInfo();
            doc.setState(DocumentState.IN_PROGRESS);
            doc.setRevision(new RevisionSymbol(0l));
            Classification emailClassitication = Iterables.find(classificationService.findByDomain(principal.getDomain(), false), new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification classification) {
                    return Classification.EMAIL.getClassificationId().equals(classification.getClassificationId());
                }
            }, null);
            if(emailClassitication==null){
                emailClassitication = Classification.EMAIL;
                emailClassitication.setActive(true);
                emailClassitication.setDomain(principal.getDomain());
                classificationService.save(emailClassitication);
            }
            doc.setClassification(emailClassitication);
            doc.setCreatedBy(principal);
            doc.setCreationDate(new Date());
            doc.setDocumentName(payload.getSubject());
            DocumentType emailDocType = Iterables.find(documentTypeService.findByDomain(principal.getDomain(), false), new Predicate<DocumentType>() {
                @Override
                public boolean apply(@Nullable DocumentType documentType) {
                    return DocumentType.EMAIL.getTypeId().equals(documentType.getTypeId());
                }
            }, null);
            if(emailDocType==null){
                emailDocType = DocumentType.EMAIL;
                emailDocType.setActive(true);
                emailDocType.setDomain(principal.getDomain());
                documentTypeService.save(emailDocType);
            }
            doc.setDocType(emailDocType);
            doc.setDomain(principal.getDomain());
            doc.setKind(DocumentInfo.Kind.INTERNAL);
            doc.setRemoved(false);
            doc.setResponsibleUser(principal);
            doc.setPlannedIssueDate(new Date());
            doc.setMasterDocumentNumber(documentService.getNextMasterDocumentNumber(principal.getDomain()));
            doc.setDocumentNumber(String.valueOf(doc.getMasterDocumentNumber()));
            documentInfoRepository.save(doc);
            if(payload.getContent() instanceof MimeMultipart){
                List<FileInfo> files = Lists.newArrayList();
                MimeMultipart multipart = (MimeMultipart) payload.getContent();
                handleMultipart(multipart,principal,files);
                doc.setFiles(files);
                documentInfoRepository.save(doc);
            }

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject("accepted "+payload.getSubject());
            mail.setFrom(defaultConfig.getSmtpFrom());
            mail.setText("cepgate has accepted your document");
            mail.setTo(principal.getEmail());
            mailSender.send(mail);
        }else{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject("rejected "+payload.getSubject());
            mail.setFrom(defaultConfig.getSmtpFrom());
            mail.setText("cepgate has rejected your document");
            mail.setTo(from);
            mailSender.send(mail);
        }
    }

    public void handleMultipart(Multipart mp, Principal principal,List<FileInfo> files) throws MessagingException, IOException {
        int count = mp.getCount();
        for (int i = 0; i < count; i++)
        {
            BodyPart bp = mp.getBodyPart(i);
            Object content = bp.getContent();
            if (content instanceof String)
            {
                // handle string
            }
            else if (content instanceof InputStream)
            {
                InputStream is = (InputStream) content;
                FileInfo file = fileService.createFile(principal.getDomain(),bp.getFileName(),bp.getContentType(),is,principal);
                files.add(file);
                // handle input stream
            }
            else if (content instanceof Message)
            {
                Message message = (Message)(content);
                //handleMessage(message);
            }
            else if (content instanceof Multipart)
            {
                Multipart mp2 = (Multipart)content;
                handleMultipart(mp2,principal,files);
            }
        }
    }

}
