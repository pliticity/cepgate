package pl.iticity.dbfds.service.common.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.common.TransmittalService;
import pl.iticity.dbfds.service.document.FileService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;

@Service
public class TransmittalServiceImpl implements TransmittalService {

    @Autowired
    private FileService fileService;

    public File createTransmittal(Principal sender, Principal recipient, DocumentInfo documentInfo) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();

        doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page);

        drawTable(contentStream,sender,recipient,documentInfo);

        contentStream.close();

        File newFile = fileService.createTempFile();
        doc.save(newFile);
        return newFile;
    }

    private void drawTable(PDPageContentStream contentStream, Principal sender, Principal recipient, DocumentInfo documentInfo) throws IOException {
        int lineY = 700;
        PDFont font = PDType1Font.HELVETICA;
        contentStream.setFont(font, 12);

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText("Sender company info");
        contentStream.endText();

        contentStream.setStrokingColor(Color.BLACK);
        contentStream.addRect(100,lineY-2,400f,1f);
        contentStream.stroke();

        lineY-=13;
        lineY = addCompanyInfo(contentStream,sender,lineY);

        lineY-=13;
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText("Recipient company info");
        contentStream.endText();

        contentStream.setStrokingColor(Color.BLACK);
        contentStream.addRect(100,lineY-2,400f,1f);
        contentStream.stroke();

        lineY-=13;
        lineY = addCompanyInfo(contentStream,recipient,lineY);

        lineY-=13;
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Document number : {0}",documentInfo.getDocumentNumber()));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("File type : {0}","?"));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Document Title : {0}",StringEscapeUtils.escapeJava(documentInfo.getDocumentName())));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Document revision : {0}",documentInfo.getRevision() != null ? documentInfo.getRevision().getEffective() : "n/a"));
        contentStream.endText();

        lineY = lineY-100;
        contentStream.addRect(450,lineY,120f,80f);
        contentStream.stroke();

        lineY -= 13;
        contentStream.beginText();
        contentStream.newLineAtOffset(450, lineY);
        contentStream.showText("Signature");
        contentStream.endText();

        lineY -= 13;
        contentStream.beginText();
        contentStream.newLineAtOffset(450, lineY);
        contentStream.showText(MessageFormat.format("Date : {0}",new Date()));
        contentStream.endText();

        contentStream.close();
    }

    private int newLine(PDPageContentStream contentStream, int y) throws IOException {
        y-=13;
        contentStream.newLineAtOffset(100, y);
        return y;
    }

    private int addCompanyInfo(PDPageContentStream contentStream,Principal p, int lineY) throws IOException {
        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Company : {0}",p.getCompany()));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Country : {0}",p.getCountry()));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Phone : {0}",p.getPhone()));
        contentStream.endText();

        contentStream.beginText();
        lineY = newLine(contentStream,lineY);
        contentStream.showText(MessageFormat.format("Webiste : {0}",p.getUrl()));
        contentStream.endText();

        return lineY;
    }

    private String fillTemplate(DocumentInfo documentInfo){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("/templates/templates/transmittal.vm");
        VelocityContext context = new VelocityContext();
        context.put("classificationId", documentInfo.getClassification().getClassificationId());
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

}
