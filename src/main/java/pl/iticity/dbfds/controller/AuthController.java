package pl.iticity.dbfds.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAuthView() {
        return "auth";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postAuthenticate(@RequestBody final Principal principal) {
        principalService.authenticate(principal);
        return JsonResponse.instance(true, "authenticated");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse postSignUp(@RequestBody final Principal principal) {
        principalService.registerPrincipal(principal);
        return JsonResponse.instance(true, "principal created");
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public
    @ResponseBody
    JsonResponse conflict(AuthenticationException e) {
        return JsonResponse.instance(false, "Wrong credentials");
    }

}
