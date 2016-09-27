package pl.iticity.dbfds.service.document;

import org.apache.poi.hpsf.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.document.DocumentTemplate;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.service.ScopedService;

import java.io.*;
import java.util.List;

public interface TemplateService extends ScopedService<DocumentTemplate> {

    public List<DocumentTemplate> findByDomain(Domain domain);

    public DocumentTemplate create(FileInfo fileInfo);

    public File appendMetadataToTemplate(File file, DocumentInformationCarrier documentInformationCarrier) throws IOException, NoPropertySetStreamException, MarkUnsupportedException, UnexpectedPropertySetTypeException;

    public FileInfo copyFileAndFillMeta(FileInfo fileInfo,DocumentInformationCarrier documentInformationCarrier) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException;

    public DocumentTemplate createTemplate(MultipartFile file) throws IOException, InvalidFormatException;
}
