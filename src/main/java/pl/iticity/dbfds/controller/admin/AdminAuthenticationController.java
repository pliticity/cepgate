package pl.iticity.dbfds.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class AdminAuthenticationController extends BaseController  {

	@RequestMapping(value={"/admin", "/admin/login"})
	public String login() {
		return "admin/login";
	}

	@RequestMapping("/adminAuth/authenticated")
	public @ResponseBody HashMap authenticated( HttpSession session) {
		HashMap toReturn = new HashMap();
		toReturn.put("result", "true");
		toReturn.put("firstName", "");
		toReturn.put("lastName", "");
		return toReturn;
	 }
	 
	  

}
