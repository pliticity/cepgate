package pl.iticity.dbfds.service.common.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.Mail;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.common.TransmittalService;
import pl.iticity.dbfds.service.document.FileService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;

@Service
public class TransmittalServiceImpl implements TransmittalService {

    @Autowired
    private FileService fileService;

    public File createTransmittal(Principal sender, Principal recipient, java.util.List<DocumentInformationCarrier> documentInformationCarrier, Mail mail) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        page.setRotation(90);

        doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.concatenate2CTM(0, 1, -1, 0, PDRectangle.A4.getHeight(), 0);

        drawTable(contentStream,sender,recipient, documentInformationCarrier,mail);

        contentStream.close();

        File newFile = fileService.createTempFile();
        doc.save(newFile);
        return newFile;
    }

    private void drawTable(PDPageContentStream contentStream, Principal sender, Principal recipient, java.util.List<DocumentInformationCarrier> documentInformationCarrier, Mail mail) throws IOException {
        int lineY = 800;
        PDFont font = PDType1Font.HELVETICA;
        contentStream.setFont(font, 12);

        contentStream.beginText();
        contentStream.newLineAtOffset(700, lineY);
        contentStream.showText("Transmittal Letter");
        contentStream.endText();
        lineY-=13;

        contentStream.beginText();
        contentStream.newLineAtOffset(700, lineY);
        contentStream.showText(MessageFormat.format("Date : {0}", new LocalDate()));
        contentStream.endText();
        lineY-=13;

        lineY-=26;
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText("From");
        contentStream.endText();

        contentStream.setStrokingColor(Color.BLACK);
        contentStream.addRect(50,lineY-2,700f,1f);
        contentStream.stroke();

        lineY-=13;
        lineY = addCompanyInfo(contentStream,recipient.getCompany(),recipient.getCountry(),recipient.getPhone(),lineY);

        lineY-=26;
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText("To");
        contentStream.endText();

        contentStream.setStrokingColor(Color.BLACK);
        contentStream.addRect(50,lineY-2,700f,1f);
        contentStream.stroke();

        lineY-=13;
        int tempLine = lineY-13;
        lineY = addCompanyInfo(contentStream,mail.getCompany(),mail.getAddress(),mail.getPhone(),lineY);

        contentStream.beginText();
        contentStream.newLineAtOffset(640, tempLine);
        contentStream.showText("Received Signature");
        contentStream.endText();

        tempLine-=13;
        contentStream.addRect(640,tempLine,120f,-60f);
        contentStream.stroke();

        lineY-=163;

        lineY = addDocRow(contentStream,"Document number","Document Type","Document Title","Revision",lineY);

        for(DocumentInformationCarrier doc : documentInformationCarrier){
            lineY = addDocRow(contentStream,doc.getDocumentNumber(),doc.getDocType().getName(),StringEscapeUtils.escapeJava(doc.getDocumentName()),doc.getRevision() != null ? doc.getRevision().getEffective() : "n/a",lineY);
        }

        contentStream.close();
    }

    private int addDocRow(PDPageContentStream contentStream, String number, String type, String title, String revision, int y) throws IOException {
        y-=13;

        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText(number);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(250, y);
        contentStream.showText(type);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(450, y);
        contentStream.showText(title);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(650, y);
        contentStream.showText(revision);
        contentStream.endText();

        return y;
    }

    private int newLine(PDPageContentStream contentStream, int y) throws IOException {
        y-=13;
        contentStream.newLineAtOffset(50, y);
        return y;
    }

    private int addCompanyInfo(PDPageContentStream contentStream, String company, String address, String phone, int lineY) throws IOException {
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Company Name : {0}",company));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Address : {0}",address));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Phone : {0}",phone));
        contentStream.endText();

        return lineY;
    }

    private String fillTemplate(DocumentInformationCarrier documentInformationCarrier){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("/templates/templates/transmittal.vm");
        VelocityContext context = new VelocityContext();
        context.put("classificationId", documentInformationCarrier.getClassification().getClassificationId());
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

}
