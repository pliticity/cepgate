package pl.iticity.dbfds.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DomainService domainService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResponse postAuthenticate(@RequestBody final Principal principal) {
        principalService.authenticate(principal);
        return JsonResponse.success("authenticated");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Principal postSignUp(@RequestBody final Principal principal, @RequestParam(value = "signin") boolean signin) {
        return principalService.registerPrincipal(principal, signin);
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public boolean getExists(@RequestParam(name = "email") String email) {
        Principal principal = principalService.findByEmail(email);
        Domain domain = domainService.findByName(email);
        return principal != null || domain != null;
    }

    @RequestMapping(value = "/desktopExists", method = RequestMethod.GET)
    public boolean getDesktopExists() {
        return principalService.desktopTokenExists();
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public JsonResponse conflict(AuthenticationException e) {
        return JsonResponse.success("Wrong credentials");
    }

}
