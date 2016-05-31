package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.zip.ZipFile;

@Controller
@RequestMapping("/files")
public class FileController {

/*    @RequestMapping(value = "",method = RequestMethod.GET)
    public void getFiles(@RequestBody String[] files, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip, application/octet-stream");
        ZipFile zip = new ZipFile("file.zip");
        byte[] buffer = new byte[1024];
        int bytesRead;
        OutputStream outputStream = response.getOutputStream();
        while ((bytesRead = new FileInputStream().read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }*/

}
