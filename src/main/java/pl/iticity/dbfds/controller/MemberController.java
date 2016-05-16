package pl.iticity.dbfds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class MemberController  {

	@RequestMapping("/member/index")
	 public String index() {
        return "member/index";
	 }
	 
	 
 

}
