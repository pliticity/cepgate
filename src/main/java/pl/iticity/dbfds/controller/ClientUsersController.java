package pl.iticity.dbfds.controller;

import com.erling.models.BaseUser;
import com.erling.models.ClientUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class ClientUsersController extends BaseController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/member/clientusers/save")
    public
    @ResponseBody
    HashMap save(@RequestBody Principal principal, HttpSession session) {
        HashMap toReturn = new HashMap();
        BaseUser account = getAuthenticatedAccount(session);
        if (account != null && (account = accountRepository.findOne(account.getId())) != null) {
            //make sure they have access
            boolean hasError = false;
            if (clientUser.getEmail() != null) {
                if (accountRepository.findByEmail(clientUser.getEmail()).size() > 0) {
                    toReturn.put("message", "Duplicate email address already exists.");
                    toReturn.put("result", "fail");
                    hasError = true;
                } else {
                    List<ClientUsers> testUser = repository.findByEmail(clientUser.getEmail());
                    if (testUser.size() > 0 && !testUser.get(0).getId().equals(clientUser.getId())) {//there should never be more than one account
                        toReturn.put("message", "Duplicate email address already exists.");//with this problem unless the data gets into the database another way
                        toReturn.put("result", "fail");
                        hasError = true;
                    }
                }
            } else {
                hasError = true;
                toReturn.put("message", "Email is required");
                toReturn.put("result", "fail");
            }
            if (!hasError) {
                if (clientUser.getId() == null) {
                    clientUser.setPasswd(passwordEncoder.encode(clientUser.getPasswd()));
                    clientUser.setAccountid(account.getId());
                    repository.save(clientUser);
                    toReturn.put("result", "success");
                } else {
                    ClientUsers savedAccount = repository.findOne(clientUser.getId());
                    if (savedAccount != null && savedAccount.getAccountid() != null && savedAccount.getAccountid().equals(account.getId())) {
                        if (account.getId() != null && (clientUser.getPasswd() == null || clientUser.getPasswd().length() == 0)) {//no password
                            ClientUsers savedUser = repository.findOne(account.getId());//save the old one
                            clientUser.setPasswd(savedUser.getPasswd());
                        } else if (clientUser.getPasswd() != null && clientUser.getPasswd().length() > 0) {
                            clientUser.setPasswd(passwordEncoder.encode(clientUser.getPasswd()));
                        }
                        clientUser.setAccountid(account.getId());
                        repository.save(clientUser);
                        toReturn.put("result", "success");
                    } else {
                        toReturn.put("result", "fail");
                        toReturn.put("message", "User Account not found");
                    }
                }
            }
        } else {
            toReturn.put("result", "fail");
            toReturn.put("message", "You do not have permissions for this area");
        }
        return toReturn;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/clientusers/delete")
    public
    @ResponseBody
    HashMap deleteUser(@RequestBody ClientUsers clientUser, HttpSession session) {
        HashMap toReturn = new HashMap();
        BaseUser account = getAuthenticatedAccount(session);

        if (account != null && (account = accountRepository.findOne(account.getId())) != null) {
            //make sure they have access
            ClientUsers savedAccount = repository.findOne(clientUser.getId());
            if (savedAccount != null && savedAccount.getAccountid() != null && savedAccount.getAccountid().equals(account.getId())) {
                toReturn.put("result", "success");
                repository.delete(clientUser);
            } else {
                toReturn.put("result", "success");
                toReturn.put("message", "User Account not found");
            }
        } else {
            toReturn.put("result", "fail");
            toReturn.put("message", "You do not have permissions for this area");
        }
        return toReturn;
    }

    @RequestMapping("/member/clientusers/edit")
    public
    @ResponseBody
    HashMap edit(String id) {
        HashMap toReturn = new HashMap();
        return toReturn;
    }

    @RequestMapping("/member/clientusers/list")
    public
    @ResponseBody
    HashMap<String, Object> list(HttpSession session) {
        BaseUser account = getAuthenticatedAccount(session);
        HashMap<String, Object> toReturn = new HashMap<String, Object>();
        if (account != null && (account = accountRepository.findOne(account.getId())) != null) {
            //make sure they have access
            toReturn.put("result", "success");
            toReturn.put("data", repository.findByAccountid(account.getId()));
        } else {
            toReturn.put("result", "fail");
            toReturn.put("message", "You do not have permissions for this area");

        }

        return toReturn;
    }

    @RequestMapping("/member/clientusers/index")
    public String index() {
        return "member/clientusers/index";
    }
}
