package pl.iticity.dbfds.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.DomainService;
import pl.iticity.dbfds.service.PrincipalService;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private DomainService domainService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAuthView() {
        return "auth";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postAuthenticate(@RequestBody final Principal principal) {
        principalService.authenticate(principal);
        return JsonResponse.success("authenticated");
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public String getSignOut() {
        principalService.unAuthenticate();
        return "redirect:/document";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postSignUp(@RequestBody final Principal principal) {
        principalService.registerPrincipal(principal);
        return JsonResponse.success("principal created");
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean getExists(@RequestParam(name = "email") String email) {
        Principal principal = principalService.findByEmail(email);
        Domain domain = domainService.findByName(email);
        return principal != null || domain != null;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public
    @ResponseBody
    JsonResponse conflict(AuthenticationException e) {
        return JsonResponse.success("Wrong credentials");
    }

}
