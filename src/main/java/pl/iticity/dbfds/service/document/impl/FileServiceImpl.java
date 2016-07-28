package pl.iticity.dbfds.service.document.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.model.mixins.FileInfoMixin;
import pl.iticity.dbfds.repository.common.FileRepository;
import pl.iticity.dbfds.repository.document.DocumentInfoRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl extends AbstractScopedService<FileInfo,String, FileRepository> implements FileService {

    private static final Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    @Autowired
    private DefaultConfig defaultConfig;

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    private String dataDir;

    @PostConstruct
    public void postConstruct() {
        dataDir = defaultConfig.getDataPath();
    }

    public void changeName(FileInfo fileInfo){
        FileInfo fromDb = repo.findOne(fileInfo.getId());
        fromDb.setName(fileInfo.getName());
        fromDb.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        fromDb.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(fromDb);
    }

    public FileInfo findBySymbol(String symbol) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, MessageFormat.format("Finding Content by Symbol {0}", symbol));
        }
        return repo.findBySymbol(symbol);
    }

    public FileInfo createFile(Domain domain, String fileName, String mime, InputStream inputStream, Principal principal) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setType(mime);
        fileInfo.setSymbol(computeSymbol(fileName));
        fileInfo.setPath(computeContentPath(domain, principal));
        fileInfo.setName(fileName);
        fileInfo.setUploadDate(DateTime.now().toDate());
        fileInfo.setDomain(domain);
        fileInfo.setPrincipal(principal);
        writeFile(inputStream,getOutputStreamFromContent(fileInfo));
        File file = new File(dataDir + fileInfo.getPath() + fileInfo.getSymbol());
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        fileInfo.setSize(kilobytes);
        return repo.save(fileInfo);
    }

    public FileInfo createFile(Domain domain, String fileName, String mime, InputStream inputStream) {
        return createFile(domain,fileName,mime,inputStream,PrincipalUtils.getCurrentPrincipal());
    }

    public String createFileDownloadName(DocumentInfo documentInfo,String fname){
        if(documentInfo==null){
            return fname;
        }
        String name = MessageFormat.format("{0}-{1}-{2}-{3}",documentInfo.getDocumentNumber(),documentInfo.getDocType().getName(),documentInfo.getDocumentName(),fname);
        return name;
    }

    public void updateContent(FileInfo fileInfo) {
        fileInfo.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        fileInfo.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(fileInfo);
    }

    public List<FileInfo> removeContent(String docId, final String fileId){
        DocumentInfo info = documentInfoRepository.findOne(docId);
        FileInfo fileInfo = Iterables.find(info.getFiles(), new Predicate<FileInfo>() {
            @Override
            public boolean apply(@Nullable FileInfo fileInfo) {
                return fileId.equals(fileInfo.getId());
            }
        },null);
        info.getFiles().remove(fileInfo);
        removeContent(fileInfo);
        documentInfoRepository.save(info);
        return info.getFiles();
    }

    @Transactional
    public void removeContent(FileInfo fileInfo) {
        File f = getFileForFileInfo(fileInfo);
        f.delete();
        repo.delete(fileInfo);
    }

    public File getFileForFileInfo(FileInfo fileInfo) {
        if (fileInfo != null) {
            File f = new File(dataDir + fileInfo.getPath() + fileInfo.getSymbol());
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    public File getFileForFileInfo(String fileId) {
        return getFileForFileInfo(repo.findOne(fileId));
    }

    public OutputStream getOutputStreamFromContent(FileInfo fileInfo) {
        createDirectories(fileInfo);
        return openOutputStream(fileInfo);
    }

    private void writeFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        } catch (IOException e) {
            logger.throwing(FileServiceImpl.class.getName(), "writeFile", e);
        }
    }

    private OutputStream openOutputStream(FileInfo fileInfo) {
        try {
            File f = new File(dataDir + fileInfo.getPath() + fileInfo.getSymbol());
            f.createNewFile();
            return new FileOutputStream(f);
        } catch (IOException e) {
            logger.throwing(FileServiceImpl.class.getName(), "openOutputStream", e);
        }
        return new ByteArrayOutputStream();
    }

    private void createDirectories(FileInfo fileInfo) {
        String dir = MessageFormat.format("{0}{1}", dataDir, fileInfo.getPath());
        File file = new File(dir);
        file.mkdirs();
    }

    public FileInfo zipFiles(String[] filesIds) throws IOException {
        FileInfo zip = new FileInfo();
        zip.setPath("/temp/");
        zip.setSymbol(computeSymbol(String.valueOf(new DateTime())));
        createDirectories(zip);

        FileOutputStream fileOutputStream = new FileOutputStream(new File(dataDir + zip.getPath() + zip.getSymbol()));
        ZipOutputStream os = new ZipOutputStream(fileOutputStream);

        for(String fileId : filesIds){
            byte[] buffer = new byte[1024];
            FileInfo fileInfo = repo.findOne(fileId);
            File file = getFileForFileInfo(fileInfo);
            FileInputStream fi = new FileInputStream(file);

            String name = createFileDownloadName(documentInfoRepository.findByFiles_Id(fileInfo.getId()),fileInfo.getName());

            ZipEntry entry = new ZipEntry(name);
            os.putNextEntry(entry);

            int len;
            while ((len = fi.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }

            fi.close();
            os.closeEntry();
        }

        os.close();
        return zip;
    }

    private String computeContentPath(Domain domain, Principal principal) {
        return MessageFormat.format("/domain/{0}/principal/{1}/", String.valueOf(domain.getId()), principal.getEmail());
    }

    private String computeSymbol(String fileName) {
        try {
            String hash = new Sha256Hash(fileName, DateTime.now().toString(), 1024).toHex();
            return URLEncoder.encode(hash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.throwing(FileServiceImpl.class.getName(), "computeSymbol", e);
        }
        return StringUtils.EMPTY;
    }

    public List<FileInfo> copyFiles(List<FileInfo> files) throws FileNotFoundException {
        List<FileInfo> copy = Lists.newArrayList();
        for(FileInfo fileInfo : files){
            copy.add(copyFile(fileInfo));
        }
        return copy;
    }

    public FileInfo copyFile(FileInfo fileInfo) throws FileNotFoundException {
        String filePath = dataDir + fileInfo.getPath() + fileInfo.getSymbol();
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        FileInfo copy = createFile(PrincipalUtils.getCurrentDomain(),fileInfo.getName(),fileInfo.getType(),fis);
        copy.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        copy.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(copy);
        return copy;
    }

    public long countByDomain(Domain domain) {
        return repo.findByDomain(domain).size();
    }

    public BigDecimal countMemoryByDomain(Domain domain) {
        double mem = 0;
        for(FileInfo fileInfo : repo.findByDomain(domain)){
            mem += fileInfo.getSize();
        }
        BigDecimal bigDecimal = new BigDecimal(mem);
        bigDecimal = bigDecimal.divide(new BigDecimal(1000000));
        bigDecimal = bigDecimal.setScale(4,BigDecimal.ROUND_FLOOR);
        return bigDecimal;
    }

    public String updateFileInfo(InputStream inputStream, String fileId, String docId) throws JsonProcessingException {
        FileInfo fileInfo = repo.findOne(fileId);
        AuthorizationProvider.isInDomain(fileInfo.getDomain());
        removeContent(docId,fileId);
        fileInfo = createFile(fileInfo.getDomain(),fileInfo.getName(),fileInfo.getType(),inputStream);
        DocumentInfo doc = documentInfoRepository.findOne(docId);
        doc.getFiles().add(fileInfo);
        documentInfoRepository.save(doc);
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(FileInfo.class, FileInfoMixin.class);
        return mapper.writeValueAsString(fileInfo);
    }

    public String[] getFileNames(String[] ids){
        List<String> names = Lists.newArrayList();
        for(String id : ids){
            DocumentInfo info = documentInfoRepository.findByFiles_Id(id);
            FileInfo file = findById(id);
            names.add(createFileDownloadName(info,file.getName()));
        }
        return names.toArray(new String[names.size()]);
    }

    @Override
    public File createTempFile() {
        File newFile = new File(defaultConfig.getDataPath()+"/temp/"+String.valueOf(System.currentTimeMillis()));
        return newFile;
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(DefaultConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
}
