package pl.iticity.dbfds.controller;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.model.query.QDocumentInfoBinderCustomizer;
import pl.iticity.dbfds.service.DocumentService;
import pl.iticity.dbfds.service.FileService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/document")
public class DocumentController{

    @Autowired
    private DocumentService service;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String getDocumentView(){
        return "document";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody DocumentInfo postCreate(@RequestBody DocumentInfo model) {
        service.create(model);
        return model;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<DocumentInfo> getAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody DocumentInfo getOne(@PathVariable String id) {
        return service.findById(id);
    }

    @RequestMapping(value ="/query", method = RequestMethod.GET)
    public @ResponseBody
    List<DocumentInfo> getAllDocuments(@QuerydslPredicate(root = DocumentInfo.class,bindings = QDocumentInfoBinderCustomizer.class) Predicate predicate){
        return service.findByPredicate(predicate);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public @ResponseBody DocumentInfo getNewDocument(){
        return service.createNewDocumentInfo();
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public @ResponseBody List<FileInfo> postUploadFiles(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
        FileInfo fileInfo = fileService.createFile(PrincipalUtils.getCurrentDomain(),file.getOriginalFilename(),file.getContentType(),file.getInputStream());
        return service.appendFile(id,fileInfo);
    }

    @RequestMapping(value = "/{id}/delete/{fileId}",method = RequestMethod.DELETE)
    public @ResponseBody List<FileInfo> deleteFileInfo(@PathVariable("id") String id, @PathVariable("fileId") String fileId){
        return fileService.removeContent(id,fileId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody DocumentInfo putSave(@PathVariable("id") String id,@RequestBody DocumentInfo model) {
        service.save(model);
        return model;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
