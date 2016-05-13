package pl.iticity.dbfds.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.omg.CORBA.UnknownUserException;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController extends BaseController  {

	@RequestMapping("/admin/index")
	 public String index() {
       return "redirect:/admin/accounts/index";
	 }
	 
  
	  

}
