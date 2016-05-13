package pl.iticity.dbfds.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;

public class BaseController {

	boolean hasAuthority(Role role){
		return SecurityUtils.getSubject().hasRole(role.name());
/*		boolean toReturn = false;
		if(check != null && check.getAuthorities() != null && check.getAuthorities().contains(checkAuthority)){
			toReturn = true;
		}
		return toReturn;*/
	}
	
	Principal getAuthenticatedAccount(HttpSession session){
		if(SecurityUtils.getSubject().isAuthenticated()){
			return (Principal) SecurityUtils.getSubject().getPrincipal();
		}else{
			return null;
		}
/*		BaseUser toReturn = null;
		SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
		if (sci != null) {
			UserDetails cud = (UserDetails) sci.getAuthentication().getPrincipal();
			if(cud.getAuthorities().contains(new SimpleGrantedAuthority("User"))){
		        List<Accounts> allAccounts = repository.findByEmail(cud.getUsername());
		        if(allAccounts != null && allAccounts.size() > 0){
		        	 toReturn = allAccounts.get(0);
		        }
				
			}else if(cud.getAuthorities().contains(new SimpleGrantedAuthority("ClientUsers"))){
		        List<ClientUsers> allAccounts = clientRepository.findByEmail(cud.getUsername());
		        if(allAccounts != null && allAccounts.size() > 0){
		        	 toReturn = allAccounts.get(0);
		        }	
			}
	        toReturn.setAuthorities(cud.getAuthorities());;

		}
		return toReturn;*/
	}
}
