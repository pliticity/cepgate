package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.*;
import pl.iticity.dbfds.model.dto.DocToCopyDTO;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.service.common.LinkService;
import pl.iticity.dbfds.service.document.DocumentService;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.service.document.FileService;
import pl.iticity.dbfds.service.common.MailService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/document")
public class DocumentController {

    private static final Logger logger = Logger.getLogger(DocumentController.class);

    @Autowired
    private DocumentService service;

    @Autowired
    private FileService fileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private LinkService linkService;

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

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getAll() throws JsonProcessingException {
        return service.documentsToJson(service.findAll());
    }

    @RequestMapping(value = "/query", params = {"recent"}, method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getRecent(@RequestParam(name = "recent") String recent) throws JsonProcessingException {
        return service.documentsToJson(service.findRecent());
    }

    @RequestMapping(value = "/query", params = {"my"}, method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getMy(@RequestParam(name = "my") String my) throws JsonProcessingException {
        return service.documentsToJson(service.findMy());
    }

    @RequestMapping(value = "/query", params = {"favourite"}, method = RequestMethod.GET, produces = "application/json")
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

    @RequestMapping(value = "/query", params = {"all"}, method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getAllDocuments(@RequestParam(name = "all") String all) throws JsonProcessingException {
        return service.documentsToJson(service.findAll());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    String getNewDocument() throws JsonProcessingException {
        LocalDateTime start = LocalDateTime.now();
        DocumentInfo s = service.createNewDocumentInfo();
        LocalDateTime end = LocalDateTime.now();
        Duration duration = new Duration(start.toDate().getTime(),end.toDate().getTime());
        logger.info(MessageFormat.format("Request took {0} ms",duration.getMillis()));
        return service.newDocumentToJson(s);
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    List<FileInfo> postUploadFiles(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
        FileInfo fileInfo = fileService.createFile(PrincipalUtils.getCurrentDomain(), file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        return service.appendFile(id, fileInfo);
    }

    @RequestMapping(value = "/{id}/reupload/{fId}", method = RequestMethod.POST)
    public
    @ResponseBody
    String putReUploadFiles(@PathVariable("id") String id,@PathVariable("fId") String fid, @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.updateFileInfo(file.getInputStream(),fid,id);
    }

    @RequestMapping(value = "/{id}/delete/{fileId}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    List<FileInfo> deleteFileInfo(@PathVariable("id") String id, @PathVariable("fileId") String fileId) {
        return fileService.removeContent(id, fileId);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public
    @ResponseBody
    JsonResponse deleteDocument(@RequestBody String[] ids) {
        for (String id : ids) {
            service.removeDocument(id);
        }
        return JsonResponse.success("removed");
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public
    @ResponseBody
    List<DocumentInfo> postCopyDocuments(@RequestBody List<DocToCopyDTO> docs) throws FileNotFoundException {
        List<DocumentInfo> documentInfos = Lists.newArrayList();
        for (DocToCopyDTO doc : docs) {
            documentInfos.add(service.copyDocument(doc.getId(),doc.getFiles()));
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

    @RequestMapping(value = "/{id}", params = {"favourite"}, method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postFavourite(@PathVariable(value = "id") String id, @RequestParam(name = "favourite") boolean fav) {
        service.favourite(id, fav);
        return JsonResponse.success("marked");
    }

    @RequestMapping(value = "/autocomplete/{docName}", method = RequestMethod.GET)
    public @ResponseBody String getAutoCompleteDocument(@PathVariable(value = "docName") String docName) throws JsonProcessingException {
        return service.autoCompleteDocument(docName);
    }

    @RequestMapping(value = "/link/{pId}", method = RequestMethod.POST)
    public @ResponseBody
    List<Link> postLinkDocuments(@PathVariable(value = "pId") String pId, @RequestBody DocumentInfo linkTo){
        return linkService.createLink(pId,DocumentInfo.class,linkTo);
    }

    @RequestMapping(value = "/link/{pId}", method = RequestMethod.DELETE)
    public @ResponseBody
    List<Link> deleteLinkDocuments(@PathVariable(value = "pId") String pId, @RequestBody Link link){
        return linkService.deleteLink(pId,DocumentInfo.class,link);
    }

    @RequestMapping(value = "/{id}/state/{state}", method = RequestMethod.PUT)
    public @ResponseBody
    String putDocumentState(@PathVariable("id") String id, @PathVariable("state") DocumentState state) throws JsonProcessingException {
        return service.changeState(id,state);
    }

    @RequestMapping(value = "/{id}/template/{tId}", method = RequestMethod.PUT)
    public @ResponseBody FileInfo putDocumentFromTemplate(@PathVariable("id") String id, @PathVariable("tId") String tId) throws IOException, NoPropertySetStreamException, MarkUnsupportedException, UnexpectedPropertySetTypeException {
        return service.appendTemplateFile(id,tId);
    }

    @RequestMapping(value = "/{id}/mail", method = RequestMethod.POST, params = {"zip","transmittal"})
    public @ResponseBody boolean postSendMail(@PathVariable("id") String id, @RequestBody Mail mail, @RequestParam("zip") boolean zip,@RequestParam("transmittal") boolean transmittal, HttpServletRequest request){
        mailService.sendDocument(mail,zip,request,transmittal);
        return true;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
