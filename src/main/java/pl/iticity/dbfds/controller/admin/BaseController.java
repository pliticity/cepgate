package pl.iticity.dbfds.controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.erling.managers.AccountRespository;
import com.erling.models.Accounts;

public class BaseController {
	@Autowired
	private AccountRespository repository;
	
	Accounts getAuthenticatedAccount(HttpSession session){
		Accounts toReturn = null;
		SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
		if (sci != null) {
			UserDetails cud = (UserDetails) sci.getAuthentication().getPrincipal();
	        List<Accounts> allAccounts = repository.findByEmail(cud.getUsername());
	        if(allAccounts != null && allAccounts.size() > 0){
	        	 toReturn = allAccounts.get(0);
	        }
		}
		return toReturn;
	}
}
