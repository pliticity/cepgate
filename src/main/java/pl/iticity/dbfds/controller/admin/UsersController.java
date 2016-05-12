package pl.iticity.dbfds.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erling.managers.UsersRespository;
import com.erling.models.Users;

@Controller
public class UsersController {
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UsersRespository repository;
	@RequestMapping("/admin/users/save")
	public @ResponseBody HashMap save(@RequestBody Users account) {
		 HashMap toReturn = new HashMap();
		 if(account.getId() != null && (account.getPasswd() == null || account.getPasswd().length() == 0)){
			 Users savedAccount = repository.findOne(account.getId());
			 savedAccount.setPasswd(account.getPasswd());
		 }else if(account.getPasswd() != null && account.getPasswd().length() > 0){
			 account.setPasswd(passwordEncoder.encode(account.getPasswd()));
		 }
		 repository.save(account);
		 return toReturn;
	}
	 
	@RequestMapping(method = RequestMethod.POST, value="/admin/users/delete")
	public @ResponseBody HashMap deleteUser(@RequestBody Users account) {
		 HashMap toReturn = new HashMap();
		 if(account != null){
			 Users savedAccount = repository.findOne(account.getId());
			 if(savedAccount != null){
				 repository.delete(savedAccount);
			 }
		 }
		 return toReturn;
	}

	@RequestMapping("/admin/users/edit")
	public @ResponseBody HashMap edit(String id) {
		 HashMap toReturn = new HashMap();
		 return toReturn;
	}

	@RequestMapping("/admin/users/list")
	public @ResponseBody List<Users> list() {
		 List<Users> toReturn = repository.findAll();
		 return toReturn;
	}

	 @RequestMapping("/admin/users/index")
	 public String index() {
		 
		 return "admin/users/index";
	 }
}
