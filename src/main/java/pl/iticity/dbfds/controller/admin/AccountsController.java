package pl.iticity.dbfds.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erling.managers.AccountRespository;
import com.erling.managers.ClientUsersRespository;
import com.erling.managers.DocumentRespository;
import com.erling.managers.FileRespository;
import com.erling.models.Accounts;
import com.erling.models.Documents;
import com.erling.models.Files;

@Controller
public class AccountsController {
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private DocumentRespository documentRepository;
	@Autowired
	private FileRespository fileRepository;

	@Autowired
	private ClientUsersRespository clientUserRepository;

	@Value("${file.base}")
	private String filePath;
	
	@Autowired
	private AccountRespository repository;
	@RequestMapping("/admin/accounts/save")
	public @ResponseBody HashMap save(@RequestBody Accounts account) {
		 HashMap toReturn = new HashMap();
		 if(account.getId() != null && (account.getPasswd() == null || account.getPasswd().length() == 0)){
			 Accounts savedAccount = repository.findOne(account.getId());
			 savedAccount.setPasswd(account.getPasswd());
		 }else if(account.getPasswd() != null && account.getPasswd().length() > 0){
			 account.setPasswd(passwordEncoder.encode(account.getPasswd()));
		 }
		 repository.save(account);
		 return toReturn;
	}
	 
	@RequestMapping(method = RequestMethod.POST, value="/admin/accounts/delete")
	public @ResponseBody HashMap deleteAccount(@RequestBody Accounts account) {
		 HashMap toReturn = new HashMap();
		 if(account != null){
			 Accounts savedAccount = repository.findOne(account.getId());
			 if(savedAccount != null){
				 repository.delete(savedAccount);
			 }
		 }
		 return toReturn;
	}

	@RequestMapping(method = RequestMethod.POST, value="/admin/accounts/toggle")
	public @ResponseBody HashMap toggleAccount(@RequestBody Accounts account) {
		 HashMap toReturn = new HashMap();
		 if(account != null){
			 Accounts savedAccount = repository.findOne(account.getId());
			 if(savedAccount != null){
				 savedAccount.setActive(account.getActive());
				 repository.save(savedAccount);
			 }
		 }
		 return toReturn;
	}
	
	@RequestMapping("/admin/accounts/edit")
	public @ResponseBody HashMap editAccount(String id) {
		 HashMap toReturn = new HashMap();
		 return toReturn;
	}

	@RequestMapping("/admin/accounts/list")
	public @ResponseBody List<Accounts> list() {
		 List<Accounts> toReturn = repository.findAll();
		 Accounts tempAccount;
		 List<Files> files;
		 Documents aDocument;
		 Files aFile;
		 File tempFile;
		 for(int i = 0; i < toReturn.size(); i++){
			 tempAccount = toReturn.get(i);
			 tempAccount.setUserCount(this.clientUserRepository.findByAccountid(tempAccount.getId()).size());
			 List<Documents> allDocs = this.documentRepository.findByAccountId(tempAccount.getId());
			 tempAccount.setDocumentCount(allDocs.size());
			 double fileSize = 0;
			 for(int j = 0; j < allDocs.size(); j++){
				 aDocument = allDocs.get(j);
				 files = fileRepository.findByDocumentId(aDocument.getId());
				 for(int k = 0; k < files.size(); k++){
					 aFile = files.get(k);
					 
					 tempFile = new File(filePath + "/" + aFile.getInternalName());
					 if(tempFile.exists()){
						 fileSize += tempFile.length();
					 }
				 }
			 }
			 if(fileSize == 0){
				 tempAccount.setStorageSize(0);
				 
			 }else {
				 tempAccount.setStorageSize(fileSize/1000000);
			 }
			 toReturn.set(i, tempAccount);
		 }
		 return toReturn;
	}

	 @RequestMapping("/admin/accounts/index")
	 public String index() {
		 
		 return "admin/accounts/index";
	 }
}
