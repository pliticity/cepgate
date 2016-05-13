package pl.iticity.dbfds.controller.admin;

import org.apache.shiro.SecurityUtils;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;

import javax.servlet.http.HttpSession;

public class BaseController {

    Domain getAuthenticatedAccount(HttpSession session) {
        return ((Principal) SecurityUtils.getSubject().getPrincipal()).getDomain();
    }
}
