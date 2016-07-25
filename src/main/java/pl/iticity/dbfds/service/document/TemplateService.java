package pl.iticity.dbfds.service.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.hpsf.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.repository.document.DocumentTemplateRepository;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.ScopedService;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface TemplateService extends ScopedService<DocumentTemplate> {

    public List<DocumentTemplate> findByDomain(Domain domain);

    public DocumentTemplate create(FileInfo fileInfo);

    public File appendMetadataToTemplate(File file, DocumentInfo documentInfo) throws IOException, NoPropertySetStreamException, MarkUnsupportedException, UnexpectedPropertySetTypeException;

    public FileInfo copyFileAndFillMeta(FileInfo fileInfo,DocumentInfo documentInfo) throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException, MarkUnsupportedException;

    public DocumentTemplate createTemplate(MultipartFile file) throws IOException, InvalidFormatException;
}
