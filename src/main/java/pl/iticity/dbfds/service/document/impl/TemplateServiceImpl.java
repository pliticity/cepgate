package pl.iticity.dbfds.service.document.impl;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.document.DocumentTemplateRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.document.TemplateService;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TemplateServiceImpl extends AbstractScopedService<DocumentTemplate, DocumentTemplateRepository> implements TemplateService {

    private static final Logger logger = Logger.getLogger(TemplateServiceImpl.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private DefaultConfig defaultConfig;

    public List<DocumentTemplate> findByDomain(Domain domain) {
        return repo.findByDomain(domain);
    }

    public DocumentTemplate create(FileInfo fileInfo) {
        DocumentTemplate template = new DocumentTemplate();
        template.setDate(new Date());
        template.setDomain(PrincipalUtils.getCurrentDomain());
        template.setFile(fileInfo);
        template.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        return repo.save(template);
    }

    public File appendMetadataToTemplate(File file, DocumentInfo documentInfo) throws IOException, NoPropertySetStreamException, MarkUnsupportedException, UnexpectedPropertySetTypeException {

            File newFile = new File(defaultConfig.getDataPath()+"/temp/"+String.valueOf(System.currentTimeMillis()));
        try{
            OPCPackage opc = OPCPackage.open(file);
            //PackageProperties pp = opc.getPackageProperties();
            XWPFDocument document = new XWPFDocument(opc);

            POIXMLProperties.CustomProperties properties = document.getProperties().getCustomProperties();

            setDocProperty("classificationName", documentInfo.getClassification().getName(),properties);
            setDocProperty("classificationId", documentInfo.getClassification().getClassificationId(),properties);
            setDocProperty("documentType", documentInfo.getDocType().getName(),properties);
            setDocProperty("documentTypeID", documentInfo.getDocType().getTypeId(),properties);
            setDocProperty("documentNumber", documentInfo.getDocumentNumber(),properties);
            String rDate = "";
            String rNumber = "";
            if(documentInfo.getRevisions() != null && !documentInfo.getRevisions().isEmpty()){
                Revision r = documentInfo.getRevisions().get(documentInfo.getRevisions().size()-1);
                rNumber= r.getRevision().getEffective();
                SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
                rDate = format.format(r.getDate());
            }
            setDocProperty("revisionDate", rDate,properties);
            setDocProperty("revisionNumber", rNumber,properties);
            setDocProperty("documentTitle", documentInfo.getDocumentName(),properties);
            setDocProperty("documentResponsibleAcronym", documentInfo.getResponsibleUser() !=null ? documentInfo.getResponsibleUser().getAcronym() : "",properties);

            document.getProperties().commit();
            document.write(new FileOutputStream(newFile));

/*            Nullable<String> foo = pp.getLastModifiedByProperty();
            pp.setCreatorProperty(PrincipalUtils.getCurrentPrincipal().getEmail());
            pp.setLastModifiedByProperty(PrincipalUtils.getCurrentPrincipal().getEmail() + System.currentTimeMillis());
            pp.setModifiedProperty(new Nullable<Date>(new Date()));
            pp.setTitleProperty(documentInfo.getDocumentName());
            pp.setSubjectProperty(documentInfo.getDocumentName());*/

            opc.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return newFile;
    }

    private void setDocProperty(String field, String value, POIXMLProperties.CustomProperties properties){
        if(properties.contains(field)){
            properties.getProperty(field).setLpwstr(value);
        }else{
            properties.addProperty(field,value);
        }
    }

    public FileInfo copyFileAndFillMeta(FileInfo fileInfo,DocumentInfo documentInfo) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException {
        String filePath = defaultConfig.getDataPath() + fileInfo.getPath() + fileInfo.getSymbol();
        File file = new File(filePath);
        File newFile = appendMetadataToTemplate(file,documentInfo);
        FileInputStream fis = new FileInputStream(newFile);
        FileInfo copy = fileService.createFile(PrincipalUtils.getCurrentDomain(),fileInfo.getName(),fileInfo.getType(),fis);
        newFile.delete();
        return copy;
    }

    public DocumentTemplate createTemplate(MultipartFile file) throws IOException, InvalidFormatException {
        FileInfo fileInfo = fileService.createFile(PrincipalUtils.getCurrentDomain(), file.getOriginalFilename(), file.getContentType(), file.getInputStream());

        String filePath = defaultConfig.getDataPath() + fileInfo.getPath() + fileInfo.getSymbol();
        File templateFile = new File(filePath);
        File newFile = new File(defaultConfig.getDataPath()+"/temp/"+String.valueOf(System.currentTimeMillis()));
        File newFile2 = new File(defaultConfig.getDataPath()+"/temp/2"+String.valueOf(System.currentTimeMillis()));
        FileUtils.copyFile(templateFile,newFile);

        try {
            OPCPackage opc = OPCPackage.open(newFile);
            XWPFDocument document = new XWPFDocument(opc);

            setDocProperty("test", "test",document.getProperties().getCustomProperties());
            document.getProperties().commit();
            document.write(new FileOutputStream(newFile2));
            opc.close();
        }catch (Exception e){
            fileService.removeContent(fileInfo);
            throw new IllegalArgumentException("The template is invalid.");
        } finally {
            newFile.delete();
            newFile2.delete();
        }

        return create(fileInfo);
    }
}
