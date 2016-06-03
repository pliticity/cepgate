package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.model.mixins.DocumentInfoMixIn;
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
public class DocumentController {

    @Autowired
    private DocumentService service;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getDocumentView() {
        return "document";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    DocumentInfo postCreate(@RequestBody DocumentInfo model) {
        service.create(model);
        return model;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getAll() throws JsonProcessingException {
        return service.documentsToJson(service.findAll());
    }

    @RequestMapping(value = "/query",params = {"recent"},method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getRecent(@RequestParam(name = "recent") String recent) throws JsonProcessingException {
        return service.documentsToJson(service.findRecent());
    }

    @RequestMapping(value = "/query",params = {"my"},method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getMy(@RequestParam(name = "my") String my) throws JsonProcessingException {
        return service.documentsToJson(service.findMy());
    }

    @RequestMapping(value = "/query",params = {"favourite"},method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getFavourite(@RequestParam(name = "favourite") String favourite) throws JsonProcessingException {
        return service.documentsToJson(service.findFavourite());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DocumentInfo getOne(@PathVariable String id) {
        return service.getById(id);
    }

    @RequestMapping(value = "/query",params = {"all"}, method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getAllDocuments(@RequestParam(name = "all") String all) throws JsonProcessingException {
        return service.documentsToJson(service.findAll());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET,produces = "application/json")
    public
    @ResponseBody
    String getNewDocument() throws JsonProcessingException {
        return service.createNewDocumentInfo();
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    List<FileInfo> postUploadFiles(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
        FileInfo fileInfo = fileService.createFile(PrincipalUtils.getCurrentDomain(), file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        return service.appendFile(id, fileInfo);
    }

    @RequestMapping(value = "/{id}/delete/{fileId}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    List<FileInfo> deleteFileInfo(@PathVariable("id") String id, @PathVariable("fileId") String fileId) {
        return fileService.removeContent(id, fileId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody JsonResponse deleteDocument(@PathVariable("id") String id) {
        service.removeDocument(id);
        return JsonResponse.success("removed");
    }

    @RequestMapping(value = "/{id}/copy", method = RequestMethod.POST)
    public @ResponseBody DocumentInfo postCopyDocument(@PathVariable("id") String id) {
        return service.copyDocument(id);
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public @ResponseBody List<DocumentInfo> postCopyDocuments(@RequestBody String[] ids) {
        List<DocumentInfo> documentInfos = Lists.newArrayList();
        for(String id : ids){
            documentInfos.add(service.copyDocument(id));
        }
        return documentInfos;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    DocumentInfo putSave(@PathVariable("id") String id, @RequestBody DocumentInfo model) {
        service.save(model);
        return model;
    }

    @RequestMapping(value = "/{id}",params = {"favourite"}, method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postFavourite(@PathVariable(value = "id") String id, @RequestParam(name = "favourite") boolean fav) {
        service.favourite(id,fav);
        return JsonResponse.success("marked");
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
