package pl.iticity.dbfds.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erling.managers.ClientUsersRespository;
import com.erling.managers.AccountRespository;
import com.erling.models.Accounts;
import com.erling.models.BaseUser;
import com.erling.models.ClientUsers;

@Controller
public class AdminClientUsersController extends BaseController {
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private ClientUsersRespository repository;

	@Autowired
	private AccountRespository accountRepository;

	@RequestMapping("/admin/clientusers/save")
	public @ResponseBody HashMap save(@RequestBody ClientUsers clientUser, HttpSession session) {
		 HashMap toReturn = new HashMap(); 
		 if(clientUser != null){
			 //make sure they have access
			 boolean hasError = false;
			 if(clientUser.getEmail() != null){
				if(accountRepository.findByEmail(clientUser.getEmail()).size() > 0){
					toReturn.put("message",  "Duplicate email address already exists.");
					toReturn.put("result",  "fail");
					hasError = true;
				}else {
					List<ClientUsers> testUser = repository.findByEmail(clientUser.getEmail());
					if(testUser.size() > 0 && !testUser.get(0).getId().equals(clientUser.getId())){//there should never be more than one account 
						toReturn.put("message",  "Duplicate email address already exists.");//with this problem unless the data gets into the database another way
						toReturn.put("result",  "fail");
						hasError = true;
					}
				}
			 }else {
				hasError = true;
				toReturn.put("message",  "Email is required");
				toReturn.put("result",  "fail");
			 }
			 if(!hasError){
				 if(clientUser.getId() == null){
					 clientUser.setPasswd(passwordEncoder.encode(clientUser.getPasswd())); 
					 repository.save(clientUser);
					 toReturn.put("result",  "success");
				 }else {
					 ClientUsers savedAccount = repository.findOne(clientUser.getId());
					 if(savedAccount != null && savedAccount.getAccountid() != null){
						 if(clientUser.getPasswd() == null || clientUser.getPasswd().length() == 0){
							 clientUser.setPasswd(savedAccount.getPasswd());
						 }else {
							 clientUser.setPasswd(passwordEncoder.encode(clientUser.getPasswd())); 
						 }
						 repository.save(clientUser);
						 toReturn.put("result",  "success");
					}else {
						toReturn.put("result",  "fail");
						toReturn.put("message",  "User Account not found");
					} 
				 }
			 }
		}else {
			toReturn.put("result",  "fail");
			toReturn.put("message",  "You do not have permissions for this area");
		}
		return toReturn;
	}
	 
	@RequestMapping(method = RequestMethod.POST, value="/admin/clientusers/delete")
	public @ResponseBody HashMap deleteUser(@RequestBody ClientUsers clientUser, HttpSession session) {
		HashMap toReturn = new HashMap(); 
		if(clientUser != null){
			//make sure they have access
			 ClientUsers savedAccount = repository.findOne(clientUser.getId());
			 if(savedAccount != null && savedAccount.getAccountid() != null){
				 toReturn.put("result",  "success");
				 repository.delete(clientUser);
			 }else {
				 toReturn.put("result",  "success");
				 toReturn.put("message",  "User Account not found");
			 }
		}else {
			toReturn.put("result",  "fail");
			toReturn.put("message",  "You do not have permissions for this area");
		}
		return toReturn;
	}

	@RequestMapping("/admin/clientusers/edit")
	public @ResponseBody HashMap edit(String id) {
		 HashMap toReturn = new HashMap();
		 return toReturn;
	}

	@RequestMapping("/admin/clientusers/list")
	public @ResponseBody HashMap<String, Object> list(@RequestBody Accounts account, HttpSession session) {
		 
		HashMap<String, Object> toReturn = new HashMap<String, Object>();
		if(account != null && (account = accountRepository.findOne(account.getId())) != null){
			//make sure they have access
			toReturn.put("result",  "success");
			toReturn.put("data", repository.findByAccountid(account.getId()));
		}else {
			toReturn.put("result",  "fail");
			toReturn.put("message",  "You do not have permissions for this area");
			
		}
	
		 return toReturn;
	}

	 @RequestMapping("/admin/clientusers/index")
	 public String index() { 
		 return "member/clientusers/index";
	 }
}
