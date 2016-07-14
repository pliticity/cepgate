package pl.iticity.dbfds.service;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.apache.poi.hpsf.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.DocumentTemplateRepository;
import pl.iticity.dbfds.repository.DocumentTypeRepository;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TemplateService extends AbstractService<DocumentTemplate, DocumentTemplateRepository> {

    private static final Logger logger = Logger.getLogger(TemplateService.class);

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
        return repo.save(template);
    }

    private File appendMetadataToTemplate(File file, DocumentInfo documentInfo) throws IOException, NoPropertySetStreamException, MarkUnsupportedException, UnexpectedPropertySetTypeException {

            File newFile = new File(defaultConfig.getDataPath()+"/temp/"+String.valueOf(System.currentTimeMillis()));
        try{
            OPCPackage opc = OPCPackage.open(file);
            PackageProperties pp = opc.getPackageProperties();
            XWPFDocument document = new XWPFDocument(opc);

            document.getProperties().getCustomProperties().addProperty("classificationName", documentInfo.getClassification().getName());
            document.getProperties().getCustomProperties().addProperty("classificationId", documentInfo.getClassification().getClassificationId());
            document.getProperties().getCustomProperties().addProperty("documentType", documentInfo.getDocType().getName());
            document.getProperties().getCustomProperties().addProperty("documentTypeID", documentInfo.getDocType().getTypeId());
            document.getProperties().getCustomProperties().addProperty("documentNumber", documentInfo.getDocumentNumber());
            String rDate = "";
            String rNumber = "";
            if(documentInfo.getRevisions() != null && !documentInfo.getRevisions().isEmpty()){
                Revision r = documentInfo.getRevisions().get(documentInfo.getRevisions().size()-1);
                rNumber= r.getRevision().getEffective();
                rDate = r.getDate().toString();
            }
            document.getProperties().getCustomProperties().addProperty("revisionDate", rDate);
            document.getProperties().getCustomProperties().addProperty("revisionNumber", rNumber);
            document.getProperties().getCustomProperties().addProperty("documentTitle", documentInfo.getDocumentName());
            document.getProperties().getCustomProperties().addProperty("documentResponsibleAcronym", documentInfo.getResponsibleUser() !=null ? documentInfo.getResponsibleUser().getAcronym() : "");

            document.getProperties().commit();
            document.write(new FileOutputStream(newFile));

            Nullable<String> foo = pp.getLastModifiedByProperty();
            pp.setCreatorProperty(PrincipalUtils.getCurrentPrincipal().getEmail());
            pp.setLastModifiedByProperty(PrincipalUtils.getCurrentPrincipal().getEmail() + System.currentTimeMillis());
            pp.setModifiedProperty(new Nullable<Date>(new Date()));
            pp.setTitleProperty(documentInfo.getDocumentName());
            pp.setSubjectProperty(documentInfo.getDocumentName());

            opc.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return newFile;
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

}
