package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.erling.models.*;
import com.erling.managers.*;

@Controller

public class DocumentManagerController  extends BaseController {
	@Autowired
	private AccountRespository repository;

	@Autowired
	private ClientUsersRespository clientUsersRepository;

	@Autowired
	private DocumentRespository documentRepository;

	@Autowired
	private ClientUsersDocumentsFavoriteRespository clientUsersDocumentsFavoriteRepository;

	@Autowired
	private AccountsDocumentsFavoriteRespository accountsDocumentsFavoriteRepository;
	
	@Autowired
	private ClientUsersDocumentsRecentRespository clientUsersDocumentsRecentRepository;

	@Autowired
	private AccountsDocumentsRecentRespository accountsDocumentsRecentRepository;

	@Autowired
	private FileRespository fileRepository;

	@Value("B:\\Workspace\\dhd\\data")
	private String filePath;
	
	@Value("20")
	private int defaultDays;
	
	private boolean canUserEditDocument(UserDetails account, Documents aDocument){
		boolean toReturn = false;
		if(hasAuthority(account, new SimpleGrantedAuthority("ClientUsers"))){//check if client 
			toReturn = ((ClientUsers) account).getId().equals(aDocument.getCreatorId()) ||
					((ClientUsers) account).getAccountid().equals(aDocument.getAccountId());
		}else {
			toReturn = ((BaseUser) account).getId().equals(aDocument.getAccountId());
		}
		return toReturn;
	}
	
	private boolean isUser(UserDetails account, String theType){
		boolean toReturn = false;
		if(hasAuthority(account, new SimpleGrantedAuthority(theType))){//check if client 
			toReturn = true;
		}
		return toReturn;
	}
	
	
	@RequestMapping("/documents/loadDocuments")
	 public @ResponseBody HashMap<String, List<Documents>> loadDocuments(@RequestParam(value="type", required = false) String type, @RequestParam(value="days", required = false) String days, HttpSession session ) {
		UserDetails account = getAuthenticatedAccount(session);
		boolean isClientUser = false;
		ClientUsers aClient = null;
		 
		if(hasAuthority(account, new SimpleGrantedAuthority("ClientUsers"))){
			aClient = (ClientUsers) account;
			account = this.repository.findOne(((ClientUsers) account).getAccountid());
			isClientUser = true;
		}
		List<Documents> allDocuments = null;
		BaseUser theAccount = (BaseUser) account;
		HashMap<String, List<Documents>> toReturn = new HashMap<String, List<Documents>>();
		if(account != null){
			
			allDocuments = getFiles(theAccount, documentRepository.findByAccountIdAndIsDeletedIsFalse(theAccount.getId()));
			
			List<Documents> myDocuments = new ArrayList<Documents>();
			List<Documents> recentDocuments = new ArrayList<Documents>();
			List<Documents> favoriteDocuments = new ArrayList<Documents>();
			Calendar calendar = Calendar.getInstance(); 
			
			if(days != null){
				calendar.add(Calendar.DAY_OF_MONTH, -1 * Integer.parseInt(days) - 1);
			}else {
				calendar.add(Calendar.DAY_OF_MONTH, -1 * this.defaultDays - 1);
			}
			Date lastDate = calendar.getTime();
			Documents aDocument;
			List<String> favoriteIds = new ArrayList<String>();
			List<String> recentIds = new ArrayList<String>();
			if(allDocuments != null){
				if(isClientUser){
					List<ClientUsersDocumentFavorites> list = this.clientUsersDocumentsFavoriteRepository.findByClientUsersId(aClient.getId());
					for(ClientUsersDocumentFavorites anItem: list){
						favoriteIds.add(anItem.getDocumentId());
					}
					favoriteDocuments = this.documentRepository.findByIdIn(favoriteIds);
					 
					List<ClientUsersDocumentRecent> rlist = this.clientUsersDocumentsRecentRepository.findByClientUsersId(aClient.getId());
					for(ClientUsersDocumentRecent anItem: rlist){
						if(anItem.getLastOpened() != null && anItem.getLastOpened().after(lastDate)){
							recentIds.add(anItem.getDocumentId());
						}else {
							clientUsersDocumentsRecentRepository.delete(anItem);
						}
					}
					recentDocuments = this.documentRepository.findByIdIn(recentIds); 
				}else {
					List<AccountsDocumentFavorites> list = this.accountsDocumentsFavoriteRepository.findByAccountId(theAccount.getId());
					for(AccountsDocumentFavorites anItem: list){
						favoriteIds.add(anItem.getDocumentId());
					}
					favoriteDocuments = this.documentRepository.findByIdIn(favoriteIds);
					 
					List<AccountsDocumentRecent> rlist = this.accountsDocumentsRecentRepository.findByAccountId(theAccount.getId());
					for(AccountsDocumentRecent anItem: rlist){
						if(anItem.getLastOpened() != null && anItem.getLastOpened().after(lastDate)){
							recentIds.add(anItem.getDocumentId());
						}else {
							accountsDocumentsRecentRepository.delete(anItem);
						}
					} 
					recentDocuments = this.documentRepository.findByIdIn(recentIds); 
				}
				
				for(int i = 0; i < allDocuments.size(); i++){
					aDocument = allDocuments.get(i);
					if(!aDocument.isDeleted()){ 
						if(favoriteIds.contains(aDocument.getId())){
							aDocument.setFavorite(true);
						}
						
						if(isClientUser && aClient.getId().equals(aDocument.getCreatorId())){//add to favorites
							myDocuments.add(aDocument);
						}else if(!isClientUser && theAccount.getId().equals(aDocument.getCreatorId())) {
							myDocuments.add(aDocument);
						} 
					}
				}
			}
			
			toReturn.put("my_documents", myDocuments);
			toReturn.put("recent_documents", recentDocuments);
			toReturn.put("favorite_documents", favoriteDocuments);
			toReturn.put("search_documents", allDocuments);
			 
	    }
    	return toReturn; 
    	 
	 }
	 
	private List<Documents> getFiles(BaseUser theAccount, List<Documents> allDocuments){
		Documents tempDoc;
    	for(int i = 0; i < allDocuments.size(); i ++){
    		List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(allDocuments.get(i).getId());
    		allDocuments.get(i).setFiles(allFiles);;
    		tempDoc = allDocuments.get(i);
    		if(StringUtils.isEmpty(tempDoc.getCreatorId()) || StringUtils.isEmpty(tempDoc.getCreatorType())){
	    		allDocuments.get(i).setCreatedBy(theAccount.getFirstname() + " " + theAccount.getLastname());
    		}else {
    			if(tempDoc.getCreatorType().equals("User")){
    				allDocuments.get(i).setCreatedBy(theAccount.getFirstname() + " " + theAccount.getLastname());
    			}else {
    				ClientUsers clientUsers = clientUsersRepository.findOne(tempDoc.getCreatorId());
    				if(clientUsers != null ){
    					allDocuments.get(i).setCreatedBy(clientUsers.getFirstname() + " " + clientUsers.getLastname());
    				}else {
    					allDocuments.get(i).setCreatedBy(theAccount.getFirstname() + " " + theAccount.getLastname());
    				}
    			}
    			
    		}
    	}
    	return allDocuments;
	}
	
	@RequestMapping("/documents/new")
	public @ResponseBody Documents newDocument(String type, HttpSession session) {
      BaseUser account = getAuthenticatedAccount(session);
      Documents aDocument = null;
      if(account != null){
	       aDocument = new Documents();
	       
	   	   long maxCount = documentRepository.count();
	   	   aDocument.setMasterDocumentNo("" + maxCount);
	   	   aDocument.setDocumentNumber("" + maxCount);
      }
	  return aDocument; 
   	 
	} 
	@RequestMapping("/member/upload")
	 public String index() {
       return "member/upload";
	 }
	
	
	private void deleteDocumentFiles(Documents aDocument){
		List<Files> allFiles = aDocument.getFiles();
		File tempFile;
		Files aFile;
		if(allFiles != null && allFiles.size() > 0){//delete the files
			for(int i = 0 ; i < allFiles.size(); i ++){
				aFile = allFiles.get(i);
				tempFile = new File(filePath + "/" + aFile.getInternalName());
				if(tempFile.exists()){
					//tempFile.delete();
				}
				aFile.setDeleted(true);
				fileRepository.save(aFile);
			}
		}
	}
	@RequestMapping(method = RequestMethod.GET, value = "/documents/delete/{id}"  )
	public @ResponseBody HashMap<String, String> delete(@PathVariable String id, HttpSession session){
		Documents aDocument = documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aDocument != null && canUserEditDocument(account, aDocument)){
			//List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(aDocument.getId());
			aDocument.setDeleted(true);
			deleteDocumentFiles(aDocument);
			documentRepository.save(aDocument);
			toReturn.put("result" , "success");
			toReturn.put("description", "Document was deleted");
		}else {
			toReturn.put("result" , "fail");
			toReturn.put("description", "Document was not found");
		}
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/toggleFavorite/{id}"  )
	public @ResponseBody HashMap<String, String> toggleFavorite(@PathVariable String id, HttpSession session){
		Documents aDocument = documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aDocument != null && canUserEditDocument(account, aDocument)){
			if(isUser(account, "ClientUsers")){//if we have a client user
				List<ClientUsersDocumentFavorites> docs = this.clientUsersDocumentsFavoriteRepository.findByDocumentId(id);
				if(docs == null || docs.size() == 0){
					ClientUsersDocumentFavorites fav = new ClientUsersDocumentFavorites(id, account.getId());
					clientUsersDocumentsFavoriteRepository.save(fav);
				}else if(docs.size() > 0){
					clientUsersDocumentsFavoriteRepository.delete(docs.get(0));
				}
			}else {
				List<AccountsDocumentFavorites> docs = this.accountsDocumentsFavoriteRepository.findByDocumentId(id);
				if(docs == null || docs.size() == 0){
					AccountsDocumentFavorites fav = new AccountsDocumentFavorites(id, account.getId());
					accountsDocumentsFavoriteRepository.save(fav);
				}else if(docs.size() > 0){
					accountsDocumentsFavoriteRepository.delete(docs.get(0));
				}
			}
			toReturn.put("result" , "success");
			toReturn.put("description", "Document Saved");
		}else {
			toReturn.put("result" , "fail");
			toReturn.put("description", "Document was saved");
		}
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/markAsOpened/{id}"  )
	public @ResponseBody HashMap<String, String> markAsOpened(@PathVariable String id, HttpSession session){
		Documents aDocument = documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aDocument != null && this.canUserEditDocument(account,  aDocument)){
			if(isUser(account, "ClientUsers")){
				List<ClientUsersDocumentRecent> rList = 
						this.clientUsersDocumentsRecentRepository.findByClientUsersIdAndDocumentId(account.getId(), id);
				if(rList == null || rList.size() == 0){
					ClientUsersDocumentRecent recent = new ClientUsersDocumentRecent(id, account.getId());
					this.clientUsersDocumentsRecentRepository.save(recent);
				}
			}else {
				List<AccountsDocumentRecent> rList = 
						this.accountsDocumentsRecentRepository.findByAccountIdAndDocumentId(account.getId(), id);
				if(rList == null || rList.size() == 0){
					AccountsDocumentRecent recent = new AccountsDocumentRecent(id, account.getId());
					this.accountsDocumentsRecentRepository.save(recent);
				} 
			}
			aDocument.setLastOpened(new Date()); 
			documentRepository.save(aDocument);
			toReturn.put("result" , "success");
			toReturn.put("description", "Document Saved");
		}else {
			toReturn.put("result" , "fail");
			toReturn.put("description", "Document was saved");
		}
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/canDownload/{id}"  )
	public @ResponseBody HashMap<String, String> canDownload(@PathVariable String id, HttpSession session){
		Documents aDocument = documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(aDocument.getId());
			if(allFiles == null || allFiles.size() == 0){
				toReturn.put("can_download", "false");
				toReturn.put("description", "There are no files to download");
				toReturn.put("id", id); 
			}else {
				File tempFile;
				for(int i = 0; i < allFiles.size(); i++){
					tempFile = new File(filePath + "/" + allFiles.get(i).getInternalName());
					if(!tempFile.exists()){
						toReturn.put("can_download", "false");
						toReturn.put("description", "There are missing files."); 
					}
				}
				if(!toReturn.containsKey("can_download")){
					toReturn.put("can_download", "true");
					toReturn.put("description", "Document can be downloaded");
					toReturn.put("id", id); 
					
				}
			}
		}else {
			toReturn.put("can_download", "false");
			toReturn.put("description", "Document was not found");
			toReturn.put("id", id);
		}
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/downloadDocument/{id}" ,  produces = "application/zip" )
	public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String id, HttpSession session, HttpServletResponse response){
		String allIds[] = id.split(",");
		Documents aDocument = documentRepository.findById(allIds[0]);
		BaseUser account = getAuthenticatedAccount(session);
		 
		if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(aDocument.getId());

			if(allFiles == null || allFiles.size() == 0){
				return null; 
			}else {
				 
				try {
	            byte[] buffer = new byte[1024];
	            String zipFile = filePath + UUID.randomUUID().toString() + ".zip";
	            FileOutputStream fos = new FileOutputStream(zipFile);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	            for (int i=0; i < allFiles.size(); i++) {
	                File srcFile = new File(filePath + "/" + allFiles.get(i).getInternalName());
	                FileInputStream fis = new FileInputStream(srcFile);
	                // begin writing a new ZIP entry, positions the stream to the start of the entry data
	                zos.putNextEntry(new ZipEntry(allFiles.get(i).getFileName()));
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
				headers.add("Content-Disposition", "attachment; filename='" + aDocument.getName()  + ".zip'");
	            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	            headers.add("Pragma", "no-cache");
	            headers.add("Expires", "0");
	            
	            return ResponseEntity
	                    .ok()
	                    .headers(headers)
	                    .contentType(
	                            MediaType.parseMediaType("application/zip"))
	                    .body(new InputStreamResource(new FileInputStream(zipFile)));
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		 return null;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/documents/getDocument"  )
	public @ResponseBody HashMap<String, Object>  getDocument(@PathVariable String id, HttpSession session, HttpServletResponse response){
		 
		Documents aDocument = this.documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, Object> toReturn = new HashMap<String, Object>();
		if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			toReturn.put("result",  true);
			toReturn.put("description",  "Document Not found");
			toReturn.put("document",  aDocument);
		}else {
			toReturn.put("result",  false);
			toReturn.put("description",  "Document Not found");
		}
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/documents/copyDocument"  )
	public @ResponseBody HashMap<String, String>  copyDocument(@RequestBody HashMap copyFiles, HttpSession session, HttpServletResponse response){
		String id = (String) copyFiles.get("id");
		Documents aDocument = this.documentRepository.findById(id);
		BaseUser account = getAuthenticatedAccount(session);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			ArrayList<String> copyTheseFiles = (ArrayList<String>) copyFiles.get("copyFiles");
			ArrayList<Files> allFiles = new ArrayList<Files>();
			Files newFile;
			File sourceFile, targetFile;
			if(copyTheseFiles != null && copyTheseFiles.size() > 0){
				for(int i = 0; i < copyTheseFiles.size(); i++){
					Files aFile = fileRepository.findById(copyTheseFiles.get(i));
					if(aFile != null){
						allFiles.add(aFile);
						sourceFile = new File(filePath + "/" +  aFile.getInternalName());
						if(sourceFile.exists()){
							targetFile = new File(filePath + "/" +  UUID.randomUUID().toString() +  aFile.getInternalName());
							try { 
								FileCopyUtils.copy(sourceFile, targetFile);
								newFile = new Files(aFile);
								newFile.setInternalName(targetFile.getName());
								allFiles.add(newFile);
							}catch(Exception e){
								e.printStackTrace();
								toReturn.put("result", "false");
								toReturn.put("description", "An error has occurred. The files could not be copied.");
								return toReturn;
							}
						}
					}
				}
				
 
			}
			Documents newDocument = new Documents(aDocument);
			newDocument.setDocumentName(newDocument.getDocumentName() + " (Copy)");
			newDocument.setCreationDate(new Date());
			newDocument = this.documentRepository.save(newDocument);
			
			for(int i = 0; i < allFiles.size(); i++){
				newFile = allFiles.get(i);
				newFile.setDocumentId(newDocument.getId());
				this.fileRepository.save(newFile);
			}
			toReturn.put("result", "false");
			toReturn.put("description", "Files successfully copied."); 
			 
	           
		}else {
			toReturn.put("result", "false");
			toReturn.put("description", "Files not found."); 	
		}
	 
		return toReturn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/download/{id}"  )
	public ResponseEntity<InputStreamResource> download(@PathVariable String id, HttpSession session, HttpServletResponse response){
		Files aFile = fileRepository.findById(id);
		if(aFile != null){
			Documents aDocument = this.documentRepository.findById(aFile.getDocumentId());
			BaseUser account = getAuthenticatedAccount(session);
			if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			 	File tempFile;
				tempFile = new File(filePath + "/" + aFile.getInternalName());
				if(tempFile.exists()){
					try { 
						HttpHeaders headers = new HttpHeaders();
			            headers.add("Content-Description", "File Transfer");
			            headers.add("Content-Type", "application/octet-stream");
						headers.add("Content-Disposition", "attachment; filename=" + aFile.getFileName()  );
			            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			            headers.add("Pragma", "no-cache");
			            headers.add("Expires", "0");
			            
			            return ResponseEntity
			                    .ok()
			                    .headers(headers)
			                    .contentType(
			                            MediaType.parseMediaType("application/octet-stream"))
			                    .body(new InputStreamResource(new FileInputStream(filePath + "/" + aFile.getInternalName())));
						
			            
					}catch(Exception e){
						e.printStackTrace();
					}
		          }
			}
		}
		 return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents/canDownloadFile/{id}"  )
	public @ResponseBody HashMap<String, String> canDownloadFile(@PathVariable String id, HttpSession session){
		Files aFile = fileRepository.findById(id);
		HashMap<String, String> toReturn = new HashMap<String, String>();
		if(aFile != null){
			Documents aDocument = this.documentRepository.findById(aFile.getDocumentId());
			BaseUser account = getAuthenticatedAccount(session);
			if(aDocument != null && this.canUserEditDocument(account, aDocument)){
			 	File tempFile;
				tempFile = new File(filePath + "/" + aFile.getInternalName());
				if(!tempFile.exists()){
					toReturn.put("can_download", "false");
					toReturn.put("description", "File cannot be found."); 
				}else {
					toReturn.put("can_download", "true");
					toReturn.put("description", "File can be downloaded");
					toReturn.put("id", id); 
				}
			}else {
				toReturn.put("can_download", "false");
				toReturn.put("description", "File was not found");
				toReturn.put("id", id);
			}
		}else {
			toReturn.put("can_download", "false");
			toReturn.put("description", "File was not found");
			toReturn.put("id", id); 
			
		}
		return toReturn;
	}
	
	 
	@RequestMapping(method = RequestMethod.POST, value = "/documents/save", consumes = "multipart/form-data"  )
	public @ResponseBody HashMap<String, String> save(@RequestParam(value="id", required = false) String id, @RequestParam(value="classificationId", required = false) String classificationId,
													  @RequestParam(value="classificationName", required = false) String classificationName, @RequestParam(value="tags", required = false) String tags,
													  @RequestParam(value="documentName", required = false) String documentName, @RequestParam(value="documentName2", required = false) String documentName2,
													  @RequestParam(value="documentName3", required = false) String documentName3,
													  @RequestParam(value="masterDocumentNo", required = false) String masterDocumentNo, @RequestParam(value="documentNumber", required = false) String documentNumber,
													  @RequestParam(value="internalFileName", required = false) String internalFileName, @RequestParam(value="documentNo", required = false) String documentNo,
													  @RequestParam(value="documentType", required = false) String documentType, @RequestParam(value="plannedIssueDate", required = false) String plannedIssueDate, @RequestParam(value="internalExternal", required = false) String internalExternal,
													  @RequestParam(name="files[]", required=true) MultipartFile[]  files, Model model, HttpSession session, HttpServletRequest request) throws Exception {
      BaseUser account = getAuthenticatedAccount(session);
       String accountId = account.getId();
       Documents aDocument = this.documentRepository.findById(id); 
       
       DateFormatter dateFormat = new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
   	   aDocument.populate(classificationId, classificationName, documentName, documentName2, documentName3, masterDocumentNo, documentNumber, internalFileName, documentNo, documentType, internalExternal, dateFormat.parse(plannedIssueDate,  Locale.UK), tags);
   	   aDocument.setAccountId(accountId);
   	   if(StringUtils.isEmpty(aDocument.getCreatedBy()) || StringUtils.isEmpty(aDocument.getCreatorId())){
	   	   if(account instanceof Accounts){
	   		   aDocument.setCreatorType("Users");
	   		   aDocument.setCreatorId(accountId);
	   	   }else {
	   		   aDocument.setCreatorType("ClientUsers");
	   		   aDocument.setCreatorId(accountId);
	   		   aDocument.setAccountId(clientUsersRepository.findOne(accountId).getAccountid());
			}
   	   }
   	   Documents savedDocuments = documentRepository.save(aDocument);
	   
   	   HashMap<String, String> mp = new HashMap<String, String>();
   	   String aFile;
   	 try {
	   	Collection<Part> fileParts =  request.getParts(); // Retrieves <input type="file" name="file">
	    Iterator<Part> it = fileParts.iterator();
	    List<Files> allFiles = this.fileRepository.findByDocumentIdAndIsDeletedIsFalse(aDocument.getId());
	    
	    while(it.hasNext()){
	    	Part tempPart = it.next();
	    	if(tempPart.getName().matches("files.*id.*")){
	    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    		FileCopyUtils.copy(tempPart.getInputStream(), stream);
	    		stream.close();
	    		mp.put(stream.toString(), "");
	    	}
	    	
	    	if(tempPart.getContentType() != null){
	    		 aFile = UUID.randomUUID().toString() +   tempPart.getSubmittedFileName();
	    		 BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(new File(filePath + "/" + aFile)));
	    		 FileCopyUtils.copy(tempPart.getInputStream(), stream);
	    		 Files tempFile = new Files(tempPart.getSubmittedFileName(), aFile, new Date(), aDocument.getId()); 
	    		 this.fileRepository.save(tempFile);
	    	}
	    	
	    }
	    
	    for(int i = 0; i < allFiles.size(); i++){
	    	if(!mp.containsKey(allFiles.get(i).getId())){
	    		this.fileRepository.delete(allFiles.get(i));
	    	}
	    }
    
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }
   	   if(savedDocuments != null){
   		   mp.put("result", "success");
   		   mp.put("description", "Document(s) saved successfully");
   	   }else {
   		   mp.put("result", "fail");
   		   mp.put("description", "Save failed");
   	   }
       return mp; 
   	 
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/documents/create", consumes = "multipart/form-data"  )
	public @ResponseBody HashMap<String, String> create(@RequestParam(value="classificationId", required = false) String classificationId,
														@RequestParam(value="classificationName", required = false) String classificationName, @RequestParam(value="tags", required = false) String tags,
														@RequestParam(value="documentName", required = false) String documentName, @RequestParam(value="documentName2", required = false) String documentName2,
														@RequestParam(value="documentName3", required = false) String documentName3,
														@RequestParam(value="masterDocumentNo", required = false) String masterDocumentNo, @RequestParam(value="documentNumber", required = false) String documentNumber,
														@RequestParam(value="internalFileName", required = false) String internalFileName, @RequestParam(value="documentNo", required = false) String documentNo,
														@RequestParam(value="documentType", required = false) String documentType, @RequestParam(value="plannedIssueDate", required = false) String plannedIssueDate, @RequestParam(value="internalExternal", required = false) String internalExternal,
														@RequestParam(name="files[]", required=true) MultipartFile  []files, Model model, HttpSession session, HttpServletRequest request) throws Exception {
      BaseUser account = getAuthenticatedAccount(session);
       String accountId = account.getId();
       Documents aDocument = new Documents();
      
       DateFormatter dateFormat = new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
   	   aDocument.populate(classificationId, classificationName, documentName, documentName2, documentName3, masterDocumentNo, documentNumber, internalFileName, documentNo, documentType, internalExternal, dateFormat.parse(plannedIssueDate,  Locale.UK), tags);
   	   aDocument.setAccountId(accountId);
   	   aDocument.setCreationDate(new Date());
   	   aDocument.setDeleted(false);
   	   if(StringUtils.isEmpty(aDocument.getCreatedBy()) || StringUtils.isEmpty(aDocument.getCreatorId())){
	   	   if(account instanceof Accounts){
	   		   aDocument.setCreatorType("Users");
	   		   aDocument.setCreatorId(accountId);
	   	   }else {
	   		   aDocument.setCreatorType("ClientUsers");
	   		   aDocument.setCreatorId(accountId);
	   		   aDocument.setAccountId(clientUsersRepository.findOne(accountId).getAccountid());
			}
	   }
   	   Documents savedDocuments = documentRepository.save(aDocument);
   	   HashMap<String, String> mp = new HashMap<String, String>();
   	   String aFile;
   	 try {
	   	Collection<Part> fileParts =  request.getParts(); // Retrieves <input type="file" name="file">
	    
	    Iterator<Part> it = fileParts.iterator();
    	 while(it.hasNext()){
 	    	Part tempPart = it.next();
 	    	if(tempPart.getName().matches("files.*id.*")){
 	    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
 	    		FileCopyUtils.copy(tempPart.getInputStream(), stream);
 	    		stream.close();
 	    		mp.put(stream.toString(), "");
 	    	}
 	    	
 	    	if(tempPart.getContentType() != null){
 	    		 aFile = UUID.randomUUID().toString() +   tempPart.getSubmittedFileName();
 	    		 BufferedOutputStream stream = new BufferedOutputStream(
 							new FileOutputStream(new File(filePath + "/" + aFile)));
 	    		 FileCopyUtils.copy(tempPart.getInputStream(), stream);
 	    		 Files tempFile = new Files(tempPart.getSubmittedFileName(), aFile, new Date(), aDocument.getId()); 
 	    		 this.fileRepository.save(tempFile);
 	    	}
 	    	
 	    
	    	 
	    }
    
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }
   	   if(savedDocuments != null){
   		   mp.put("result", "success");
   		   mp.put("description", "Document(s) saved successfully");
   	   }else {
   		   mp.put("result", "fail");
   		   mp.put("description", "Save failed");
   	   }
       return mp; 
   	 
	} 
 

}
