package pl.iticity.dbfds.service;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.repository.FileRepository;
import pl.iticity.dbfds.security.Principal;

import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileService extends AbstractService<FileInfo,FileRepository>{

    private static final Logger logger = Logger.getLogger(FileService.class.getName());
    public static final String DATA_DIR = "/home/pmajchrz/other/dbfds/data/";

    public FileInfo findBySymbol(String symbol) {
        if(logger.isLoggable(Level.INFO)){
            logger.log(Level.INFO,MessageFormat.format("Finding Content by Symbol {0}",symbol));
        }
        return repo.findBySymbol(symbol);
    }

    @Transactional
    public FileInfo createFile(Domain domain, String fileName, String mime){
        FileInfo fileInfo = new FileInfo();
        fileInfo.setType(mime);
        fileInfo.setSymbol(computeSymbol(fileName));
        fileInfo.setPath(computeContentPath(domain, (Principal) SecurityUtils.getSubject().getPrincipal()));
        fileInfo.setName(fileName);
        fileInfo.setUploadDate(DateTime.now().toDate());
        return repo.save(fileInfo);
    }

    public void updateContent(FileInfo fileInfo){
        repo.save(fileInfo);
    }

    @Transactional
    public void removeContent(FileInfo fileInfo){
        File f = getFileForFileInfo(fileInfo);
        f.delete();
        repo.delete(fileInfo);
    }

    public File getFileForFileInfo(FileInfo fileInfo) {
        if (fileInfo != null) {
            File f = new File(DATA_DIR + fileInfo.getPath() + fileInfo.getSymbol());
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    public OutputStream getOutputStreamFromContent(FileInfo fileInfo){
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
            File f = new File(DATA_DIR + fileInfo.getPath() + fileInfo.getSymbol());
            f.createNewFile();
            return new FileOutputStream(f);
        } catch (IOException e) {
            logger.throwing(FileService.class.getName(), "openOutputStream", e);
        }
        return new ByteArrayOutputStream();
    }

    private void createDirectories(FileInfo fileInfo) {
        String dir = MessageFormat.format("{0}{1}", DATA_DIR, fileInfo.getPath());
        File file = new File(dir);
        file.mkdirs();
    }

    private String computeContentPath(Domain domain, Principal principal) {
        return MessageFormat.format("/domain/{0}/principal/{1}/", String.valueOf(domain.getId()),principal.getEmail());
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

}
