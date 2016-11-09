package pl.iticity.dbfds.controller.desktop;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.document.OperatingSystem;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/desktop")
public class DesktopController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean registerToken(@RequestBody DesktopTokenDTO dto) {
        return principalService.setDesktopToken(dto.getToken());
    }

    @RequestMapping(method = RequestMethod.GET, params = {"download"})
    public void downloadDesktop(HttpServletResponse response, @RequestParam("download") OperatingSystem download) throws IOException {
        File desktop = fileService.getDesktopVersion(download);
        FileInputStream fis = new FileInputStream(desktop);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=cepgate-desktop.zip");
        IOUtils.copy(fis, response.getOutputStream());
        response.flushBuffer();
    }
}
