package pl.iticity.dbfds.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.authc.AuthenticationException;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.Service;

import java.util.List;

public interface PrincipalService extends Service<Principal>{

    public String principalsSelectToJson(List<Principal> principals) throws JsonProcessingException;

    public void authenticate(final Principal principal) throws AuthenticationException;

    public void unAuthenticate();

    public Principal registerPrincipal(Principal principal,boolean signin);

    public List<Principal> changeActive(String id, boolean active);

    public List<Principal> changeRole(String id, Role role);

    public Principal findByEmail(String email);

    public List<Principal> findByDomain(Domain domain);

    public boolean acronymExistsInDomain(String id, String acronym, Domain domain);

    public Principal addPrincipal(Principal principal,String domainId);

    public void changePassword(Principal principal);

}
