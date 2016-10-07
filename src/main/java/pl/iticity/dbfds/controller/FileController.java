package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.service.document.DocumentService;
import pl.iticity.dbfds.service.document.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/{id}/desktop", method = RequestMethod.GET)
    public boolean openOnDestkop(@PathVariable("id") String id, HttpServletRequest request) {
        documentService.openOnDesktop(id,request);
        return true;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public FileInfo getFiles(@RequestBody String[] files, HttpServletResponse response) throws IOException {
        return fileService.zipFiles(files);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public boolean postFileName(@RequestBody FileInfo file) {
        fileService.changeName(file);
        return true;
    }

    @RequestMapping(value = "/names", method = RequestMethod.POST)
    public String[] getFileName(@RequestBody String[] ids) {
        return fileService.getFileNames(ids);
    }
}
