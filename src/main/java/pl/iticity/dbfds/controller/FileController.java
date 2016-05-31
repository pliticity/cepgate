package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/files")
public class FileController {

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void getFiles(@RequestBody String[] files, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip, application/octet-stream");
        ZipFile zip = new ZipFile("file.zip");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream os = new ZipOutputStream(baos);


/*        for (int i=0; i<files.length; i++) {
            System.out.println("Adding: "+files[i]);
            FileInputStream fi = new
                    FileInputStream(files[i]);
            origin = new
                    BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files[i]);
            out.putNextEntry(entry);
            int count;
            while((count = origin.read(data, 0,
                    BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }*/

        int bytesRead;
        byte[] buffer = new byte[1024];
        ZipInputStream is = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
        OutputStream outputStream = response.getOutputStream();
        while ((bytesRead = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

}
