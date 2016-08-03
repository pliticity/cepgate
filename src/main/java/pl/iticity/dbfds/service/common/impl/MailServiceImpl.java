package pl.iticity.dbfds.service.common.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.MailService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.common.TransmittalService;
import pl.iticity.dbfds.service.document.DocumentService;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Set;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = Logger.getLogger(MailServiceImpl.class);

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

    @Autowired
    private TransmittalService transmittalService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private DocumentTypeService documentTypeService;

    public void sendDocument(Mail email, boolean zip, HttpServletRequest request, boolean appendTransmittal) {
        //DocumentInfo doc = documentService.findById(docId);
        //AuthorizationProvider.isInDomain(doc.getDomain());
        MimeMessage mail = mailSender.createMimeMessage();
        File transmittal = null;

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setFrom(defaultConfig.getSmtpFrom());
            if(StringUtils.isEmpty(email.getRecipient()) || !EmailValidator.getInstance().isValid(email.getRecipient())){
                helper.setTo(PrincipalUtils.getCurrentPrincipal().getEmail());
            }else{
                helper.setTo(email.getRecipient());
            }
            if(StringUtils.isEmpty(email.getSubject())) {
                helper.setSubject("This is an e-mail generated from Cepgate.");
            }else{
                helper.setSubject(email.getSubject());
            }
            StringBuilder text = new StringBuilder("This is an e-mail generated from Cepgate.<br/>");
            if(!StringUtils.isEmpty(email.getMessage())){
                text.append("<br/>"+email.getMessage()+"<br/>");
            }
            for (String f : email.getFiles()) {
                DocumentInfo doc = documentInfoRepository.findByFiles_Id(f);
                text.append(fillTemplate(doc,request));
            }
            helper.setText(text.toString(),true);

            if (!zip) {
                for (String f : email.getFiles()) {
                    FileInfo fileInfo = fileService.findById(f);
                    File file = fileService.getFileForFileInfo(fileInfo);
                    helper.addAttachment(fileInfo.getName(), file);
                }
            }

            if(appendTransmittal){
                Principal sender = PrincipalUtils.getCurrentPrincipal();
                Principal recipient = principalService.findByEmail(email.getRecipient());
                Set<DocumentInfo> docs = Sets.newHashSet(Iterables.transform(Lists.newArrayList(email.getFiles()), new Function<String, DocumentInfo>() {
                    @Nullable
                    @Override
                    public DocumentInfo apply(@Nullable String s) {
                        return documentInfoRepository.findByFiles_Id(s);
                    }
                }));
                transmittal = transmittalService.createTransmittal(sender,recipient,Lists.<DocumentInfo>newArrayList(docs),email);
                helper.addAttachment("transmittal.pdf",transmittal);

                DocumentInfo doc = documentService.createNewDocumentInfo();
                Classification c = Iterables.find(classificationService.findByDomain(PrincipalUtils.getCurrentDomain(), false), new Predicate<Classification>() {
                    @Override
                    public boolean apply(@Nullable Classification classification) {
                        return Classification.EMAIL.getClassificationId().equals(classification.getClassificationId());
                    }
                }, null);
                doc.setClassification(c);
                doc.setDocumentName(StringUtils.isEmpty(mail.getSubject()) ? String.valueOf(doc.getMasterDocumentNumber()) : mail.getSubject());
                doc.setKind(DocumentInfo.Kind.INTERNAL);

                DocumentType dt= Iterables.find(documentTypeService.findByDomain(PrincipalUtils.getCurrentDomain(), false), new Predicate<DocumentType>() {
                    @Override
                    public boolean apply(@Nullable DocumentType docT) {
                        return DocumentType.EMAIL.getTypeId().equals(docT.getTypeId());
                    }
                }, null);

                doc.setDocType(dt);
                doc.setDomain(PrincipalUtils.getCurrentDomain());
                documentService.create(doc);

                ByteArrayInputStream bais = new ByteArrayInputStream(text.toString().getBytes());
                FileInfo mailFile = fileService.createFile(PrincipalUtils.getCurrentDomain(),"mail.txt","text/plain",bais,PrincipalUtils.getCurrentPrincipal());

                FileInputStream fis = new FileInputStream(new File(transmittal.getAbsolutePath()));
                FileInfo transFile = fileService.createFile(PrincipalUtils.getCurrentDomain(),"transmittal.pdf","application/pdf",fis,PrincipalUtils.getCurrentPrincipal());

                doc.getFiles().add(mailFile);
                doc.getFiles().add(transFile);

                documentService.save(doc);
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        mailSender.send(mail);
        if(transmittal!=null){
            transmittal.delete();
        }
    }

    private String fillTemplate(DocumentInfo documentInfo,HttpServletRequest request){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        ///home/pmajchrz/other/dbfds/dbfds/src/main/
        Template t = ve.getTemplate("/templates/mail/files.vm");
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
