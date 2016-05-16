package pl.iticity.dbfds.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.DocumentService;
import pl.iticity.dbfds.service.FileService;

@Controller

public class DocumentManagerController extends BaseController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileService fileService;

    private boolean canUserEditDocument() {
        boolean toReturn = true;
        return toReturn;
    }

    private boolean isUser() {
        boolean toReturn = true;
        return toReturn;
    }


    @RequestMapping("/documents/loadDocuments")
    public
    @ResponseBody
    HashMap<String, List<DocumentInfo>> loadDocuments(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "days", required = false) String days, HttpSession session) {
        Domain domain = getAuthenticatedAccount(session).getDomain();
        boolean isClientUser = false;
        Principal aClient = null;

        SecurityUtils.getSubject().hasRole(Role.CLIENT.name());

        if (SecurityUtils.getSubject().hasRole(Role.CLIENT.name())) {
            aClient = (Principal) SecurityUtils.getSubject().getPrincipal();
            isClientUser = true;
        }
        HashMap<String, List<DocumentInfo>> toReturn = new HashMap<String, List<DocumentInfo>>();
        if (domain != null) {

            List<DocumentInfo> allDocuments = documentService.findAll();

            List<DocumentInfo> myDocuments = documentService.findByCreatedBy((Principal) SecurityUtils.getSubject().getPrincipal());
            List<DocumentInfo> recentDocuments = documentService.findByCreatedBy((Principal) SecurityUtils.getSubject().getPrincipal());
            List<DocumentInfo> favoriteDocuments = documentService.findByCreatedBy((Principal) SecurityUtils.getSubject().getPrincipal());

            toReturn.put("my_documents", myDocuments);
            toReturn.put("recent_documents", recentDocuments);
            toReturn.put("favorite_documents", favoriteDocuments);
            toReturn.put("search_documents", allDocuments);

        }
        return toReturn;

    }

    @RequestMapping("/documents/new")
    public
    @ResponseBody
    DocumentInfo newDocument(String type, HttpSession session) {
        Domain account = getAuthenticatedAccount(session).getDomain();
        DocumentInfo aDocument = null;
        if (account != null) {
            aDocument = new DocumentInfo();
            aDocument.setMasterDocumentNumber(documentService.getNextMasterDocumentNumber());
            aDocument.setDocumentNumber(String.valueOf(aDocument.getMasterDocumentNumber()));
        }
        return aDocument;

    }

    @RequestMapping("/member/upload")
    public String index() {
        return "member/upload";
    }


    private void deleteDocumentFiles(DocumentInfo documentInfo) {
        for (FileInfo fileInfo : documentInfo.getFiles()) {
            fileService.removeContent(fileInfo);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/delete/{id}")
    public
    @ResponseBody
    HashMap<String, String> delete(@PathVariable String id, HttpSession session) {
        DocumentInfo aDocument = documentService.findById(id);
        HashMap<String, String> toReturn = new HashMap<String, String>();
        if (aDocument != null) {
            deleteDocumentFiles(aDocument);
            documentService.delete(aDocument);
            toReturn.put("result", "success");
            toReturn.put("description", "Document was deleted");
        } else {
            toReturn.put("result", "fail");
            toReturn.put("description", "Document was not found");
        }
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/toggleFavorite/{id}")
    public
    @ResponseBody
    HashMap<String, String> toggleFavorite(@PathVariable String id, HttpSession session) {
        HashMap<String, String> toReturn = new HashMap<String, String>();
        toReturn.put("result", "success");
        toReturn.put("description", "Document Saved");
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/markAsOpened/{id}")
    public
    @ResponseBody
    HashMap<String, String> markAsOpened(@PathVariable String id, HttpSession session) {
        DocumentInfo aDocument = documentService.findById(id);
        HashMap<String, String> toReturn = new HashMap<String, String>();
        if (aDocument != null) {
            if (true) {
                toReturn.put("result", "success");
                toReturn.put("description", "Document Saved");
            } else {
                toReturn.put("result", "fail");
                toReturn.put("description", "Document was saved");
            }
        }
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/canDownload/{id}")
    public
    @ResponseBody
    HashMap<String, String> canDownload(@PathVariable String id, HttpSession session) {
        DocumentInfo aDocument = documentService.findById(id);
        HashMap<String, String> toReturn = new HashMap<String, String>();
        toReturn.put("can_download", "true");
        toReturn.put("description", "Document can be downloaded");
        toReturn.put("id", id);
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/downloadDocument/{id}", produces = "application/zip")
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String id, HttpSession session, HttpServletResponse response) {
        DocumentInfo aDocument = documentService.findById(id);

        if (aDocument != null) {
            List<FileInfo> allFiles = aDocument.getFiles();

            if (allFiles == null || allFiles.size() == 0) {
                return null;
            } else {

                try {
                    byte[] buffer = new byte[1024];
                    String zipFile = FileService.DATA_DIR + UUID.randomUUID().toString() + ".zip";
                    FileOutputStream fos = new FileOutputStream(zipFile);
                    ZipOutputStream zos = new ZipOutputStream(fos);
                    for (int i = 0; i < allFiles.size(); i++) {
                        File srcFile = fileService.getFileForFileInfo(allFiles.get(i));
                        FileInputStream fis = new FileInputStream(srcFile);
                        // begin writing a new ZIP entry, positions the stream to the start of the entry data
                        zos.putNextEntry(new ZipEntry(allFiles.get(i).getName()));
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                        // close the InputStream
                        fis.close();
                    }
                    zos.close();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Description", "File Transfer");
                    headers.add("Content-Type", "application/zip");
                    headers.add("Content-Disposition", "attachment; filename='" + aDocument.getDocumentName() + ".zip'");
                    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                    headers.add("Pragma", "no-cache");
                    headers.add("Expires", "0");

                    return ResponseEntity
                            .ok()
                            .headers(headers)
                            .contentType(
                                    MediaType.parseMediaType("application/zip"))
                            .body(new InputStreamResource(new FileInputStream(zipFile)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/getDocument")
    public
    @ResponseBody
    HashMap<String, Object> getDocument(@PathVariable String id, HttpSession session, HttpServletResponse response) {

        DocumentInfo aDocument = documentService.findById(id);
        HashMap<String, Object> toReturn = new HashMap<String, Object>();
        if (aDocument != null) {
            toReturn.put("result", true);
            toReturn.put("description", "Document Not found");
            toReturn.put("document", aDocument);
        } else {
            toReturn.put("result", false);
            toReturn.put("description", "Document Not found");
        }
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/documents/copyDocument")
    public
    @ResponseBody
    HashMap<String, String> copyDocument(@RequestBody HashMap copyFiles, HttpSession session, HttpServletResponse response) {
        String id = (String) copyFiles.get("id");
        DocumentInfo aDocument = documentService.findById(id);
        HashMap<String, String> toReturn = new HashMap<String, String>();
        if (aDocument != null) {
            DocumentInfo newDocument = new DocumentInfo();
            newDocument.setDocumentName(newDocument.getDocumentName() + " (Copy)");
            newDocument.setCreationDate(new Date());
            documentService.save(newDocument);
            toReturn.put("result", "true");
            toReturn.put("description", "Files successfully copied.");


        } else {
            toReturn.put("result", "false");
            toReturn.put("description", "Files not found.");
        }

        return toReturn;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String id, HttpSession session, HttpServletResponse response) {
        FileInfo aFile = fileService.findById(id);
        if (aFile != null) {
                File tempFile;
                tempFile = fileService.getFileForFileInfo(aFile);
                if (tempFile.exists()) {
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Description", "File Transfer");
                        headers.add("Content-Type", "application/octet-stream");
                        headers.add("Content-Disposition", "attachment; filename=" + aFile.getName());
                        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                        headers.add("Pragma", "no-cache");
                        headers.add("Expires", "0");

                        return ResponseEntity
                                .ok()
                                .headers(headers)
                                .contentType(
                                        MediaType.parseMediaType("application/octet-stream"))
                                .body(new InputStreamResource(new FileInputStream(fileService.getFileForFileInfo(aFile))));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/canDownloadFile/{id}")
    public
    @ResponseBody
    HashMap<String, String> canDownloadFile(@PathVariable String id, HttpSession session) {
        HashMap<String, String> toReturn = new HashMap<String, String>();
        toReturn.put("can_download", "true");
        toReturn.put("description", "File can be downloaded");
        toReturn.put("id", id);
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/documents/create")
    public @ResponseBody DocumentInfo createDocument(@RequestBody  DocumentInfo documentInfo){
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        documentInfo.setDomain(principal.getDomain());
        documentService.save(documentInfo);
        return documentInfo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/documents/file/upload")
    public @ResponseBody boolean uploadDocument(@RequestParam("file") MultipartFile file){
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documents/docNo")
    public @ResponseBody String getDocNo(){
        return String.valueOf(documentService.findAll().size()+1);
    }

/*    @RequestMapping(method = RequestMethod.POST, value = "/documents/save", consumes = "multipart/form-data")
    public
    @ResponseBody
    HashMap<String, String> save(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "classificationId", required = false) String classificationId,
                                 @RequestParam(value = "classificationName", required = false) String classificationName, @RequestParam(value = "tags", required = false) String tags,
                                 @RequestParam(value = "documentName", required = false) String documentName, @RequestParam(value = "documentName2", required = false) String documentName2,
                                 @RequestParam(value = "documentName3", required = false) String documentName3,
                                 @RequestParam(value = "masterDocumentNo", required = false) String masterDocumentNo, @RequestParam(value = "documentNumber", required = false) String documentNumber,
                                 @RequestParam(value = "internalFileName", required = false) String internalFileName, @RequestParam(value = "documentNo", required = false) String documentNo,
                                 @RequestParam(value = "documentType", required = false) String documentType, @RequestParam(value = "plannedIssueDate", required = false) String plannedIssueDate, @RequestParam(value = "internalExternal", required = false) String internalExternal,
                                 @RequestParam(name = "files[]", required = true) MultipartFile[] files, Model model, HttpSession session, HttpServletRequest request) throws Exception {
        BaseUser account = getAuthenticatedAccount(session);
        String accountId = account.getId();
        Documents aDocument = this.documentRepository.findById(id);

        DateFormatter dateFormat = new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        aDocument.populate(classificationId, classificationName, documentName, documentName2, documentName3, masterDocumentNo, documentNumber, internalFileName, documentNo, documentType, internalExternal, dateFormat.parse(plannedIssueDate, Locale.UK), tags);
        aDocument.setAccountId(accountId);
        if (StringUtils.isEmpty(aDocument.getCreatedBy()) || StringUtils.isEmpty(aDocument.getCreatorId())) {
            if (account instanceof Accounts) {
                aDocument.setCreatorType("Users");
                aDocument.setCreatorId(accountId);
            } else {
                aDocument.setCreatorType("ClientUsers");
                aDocument.setCreatorId(accountId);
                aDocument.setAccountId(clientUsersRepository.findOne(accountId).getAccountid());
            }
        }
        Documents savedDocuments = documentRepository.save(aDocument);

        HashMap<String, String> mp = new HashMap<String, String>();
        String aFile;
        try {
            Collection<Part> fileParts = request.getParts(); // Retrieves <input type="file" name="file">
            Iterator<Part> it = fileParts.iterator();
            List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(aDocument.getId());

            while (it.hasNext()) {
                Part tempPart = it.next();
                if (tempPart.getName().matches("files.*id.*")) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    FileCopyUtils.copy(tempPart.getInputStream(), stream);
                    stream.close();
                    mp.put(stream.toString(), "");
                }

                if (tempPart.getContentType() != null) {
                    aFile = UUID.randomUUID().toString() + tempPart.getSubmittedFileName();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(filePath + "/" + aFile)));
                    FileCopyUtils.copy(tempPart.getInputStream(), stream);
                    Files tempFile = new Files(tempPart.getSubmittedFileName(), aFile, new Date(), aDocument.getId());
                    this.fileRepository.save(tempFile);
                }

            }

            for (int i = 0; i < allFiles.size(); i++) {
                if (!mp.containsKey(allFiles.get(i).getId())) {
                    this.fileRepository.delete(allFiles.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedDocuments != null) {
            mp.put("result", "success");
            mp.put("description", "Document(s) saved successfully");
        } else {
            mp.put("result", "fail");
            mp.put("description", "Save failed");
        }
        return mp;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/documents/create", consumes = "multipart/form-data")
    public
    @ResponseBody
    HashMap<String, String> create(@RequestParam(value = "classificationId", required = false) String classificationId,
                                   @RequestParam(value = "classificationName", required = false) String classificationName, @RequestParam(value = "tags", required = false) String tags,
                                   @RequestParam(value = "documentName", required = false) String documentName, @RequestParam(value = "documentName2", required = false) String documentName2,
                                   @RequestParam(value = "documentName3", required = false) String documentName3,
                                   @RequestParam(value = "masterDocumentNo", required = false) String masterDocumentNo, @RequestParam(value = "documentNumber", required = false) String documentNumber,
                                   @RequestParam(value = "internalFileName", required = false) String internalFileName, @RequestParam(value = "documentNo", required = false) String documentNo,
                                   @RequestParam(value = "documentType", required = false) String documentType, @RequestParam(value = "plannedIssueDate", required = false) String plannedIssueDate, @RequestParam(value = "internalExternal", required = false) String internalExternal,
                                   @RequestParam(name = "files[]", required = true) MultipartFile[] files, Model model, HttpSession session, HttpServletRequest request) throws Exception {
        BaseUser account = getAuthenticatedAccount(session);
        String accountId = account.getId();
        Documents aDocument = new Documents();

        DateFormatter dateFormat = new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        aDocument.populate(classificationId, classificationName, documentName, documentName2, documentName3, masterDocumentNo, documentNumber, internalFileName, documentNo, documentType, internalExternal, dateFormat.parse(plannedIssueDate, Locale.UK), tags);
        aDocument.setAccountId(accountId);
        aDocument.setCreationDate(new Date());
        aDocument.setDeleted(false);
        if (StringUtils.isEmpty(aDocument.getCreatedBy()) || StringUtils.isEmpty(aDocument.getCreatorId())) {
            if (account instanceof Accounts) {
                aDocument.setCreatorType("Users");
                aDocument.setCreatorId(accountId);
            } else {
                aDocument.setCreatorType("ClientUsers");
                aDocument.setCreatorId(accountId);
                aDocument.setAccountId(clientUsersRepository.findOne(accountId).getAccountid());
            }
        }
        Documents savedDocuments = documentRepository.save(aDocument);
        HashMap<String, String> mp = new HashMap<String, String>();
        String aFile;
        try {
            Collection<Part> fileParts = request.getParts(); // Retrieves <input type="file" name="file">

            Iterator<Part> it = fileParts.iterator();
            while (it.hasNext()) {
                Part tempPart = it.next();
                if (tempPart.getName().matches("files.*id.*")) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    FileCopyUtils.copy(tempPart.getInputStream(), stream);
                    stream.close();
                    mp.put(stream.toString(), "");
                }

                if (tempPart.getContentType() != null) {
                    aFile = UUID.randomUUID().toString() + tempPart.getSubmittedFileName();
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(filePath + "/" + aFile)));
                    FileCopyUtils.copy(tempPart.getInputStream(), stream);
                    Files tempFile = new Files(tempPart.getSubmittedFileName(), aFile, new Date(), aDocument.getId());
                    this.fileRepository.save(tempFile);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedDocuments != null) {
            mp.put("result", "success");
            mp.put("description", "Document(s) saved successfully");
        } else {
            mp.put("result", "fail");
            mp.put("description", "Save failed");
        }
        return mp;

    }*/


}
