package pl.iticity.dbfds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.service.document.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody FileInfo getFiles(@RequestBody String[] files, HttpServletResponse response) throws IOException {
        return fileService.zipFiles(files);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean postFileName(@RequestBody FileInfo file){
        fileService.changeName(file);
        return true;
    }

    @RequestMapping(value = "/names",method = RequestMethod.POST)
    public @ResponseBody String[] getFileName(@RequestBody String[] ids){
        return fileService.getFileNames(ids);
    }
}
