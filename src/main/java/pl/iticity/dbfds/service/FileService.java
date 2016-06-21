package pl.iticity.dbfds.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.repository.FileRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.util.DefaultConfig;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService extends AbstractService<FileInfo, FileRepository> {

    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    @Autowired
    private DefaultConfig defaultConfig;

    @Autowired
    private DocumentInfoRepository documentInfoRepository;

    private String dataDir;

    @PostConstruct
    public void postConstruct() {
        dataDir = defaultConfig.getProperty(DefaultConfig.DATA_PATH);
    }

    public void changeName(FileInfo fileInfo){
        FileInfo fromDb = repo.findOne(fileInfo.getId());
        fromDb.setName(fileInfo.getName());
        repo.save(fromDb);
    }

    public FileInfo findBySymbol(String symbol) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, MessageFormat.format("Finding Content by Symbol {0}", symbol));
        }
        return repo.findBySymbol(symbol);
    }

    @Transactional
    public FileInfo createFile(Domain domain, String fileName, String mime, InputStream inputStream) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setType(mime);
        fileInfo.setSymbol(computeSymbol(fileName));
        fileInfo.setPath(computeContentPath(domain, (Principal) SecurityUtils.getSubject().getPrincipal()));
        fileInfo.setName(fileName);
        fileInfo.setUploadDate(DateTime.now().toDate());
        writeFile(inputStream,getOutputStreamFromContent(fileInfo));
        return repo.save(fileInfo);
    }

    public String createFileDownloadName(DocumentInfo documentInfo,String fname){
        if(documentInfo==null){
            return fname;
        }
        String name = MessageFormat.format("{0}-{1}-{2}-{3}",documentInfo.getDocumentNumber(),documentInfo.getDocType().getName(),documentInfo.getDocumentName(),fname);
        return name;
    }

    public void updateContent(FileInfo fileInfo) {
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
            logger.throwing(FileService.class.getName(), "writeFile", e);
        }
    }

    private OutputStream openOutputStream(FileInfo fileInfo) {
        try {
            File f = new File(dataDir + fileInfo.getPath() + fileInfo.getSymbol());
            f.createNewFile();
            return new FileOutputStream(f);
        } catch (IOException e) {
            logger.throwing(FileService.class.getName(), "openOutputStream", e);
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
            logger.throwing(FileService.class.getName(), "computeSymbol", e);
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
        repo.save(copy);
        return copy;
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(DefaultConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
}
