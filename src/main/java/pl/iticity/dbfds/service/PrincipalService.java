package pl.iticity.dbfds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.NewDocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.PrincipalSelectMixin;
import pl.iticity.dbfds.repository.DomainRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class PrincipalService extends AbstractService<Principal,PrincipalRepository>{

    @Autowired
    private DomainRepository domainRepository;

    public String principalsSelectToJson(List<Principal> principals) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Principal.class, PrincipalSelectMixin.class);
        return objectMapper.writeValueAsString(principals);
    }

    public void authenticate(final Principal principal) throws AuthenticationException{
        SecurityUtils.getSubject().login(new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return principal;
            }

            @Override
            public Object getCredentials() {
                return principal.getPassword();
            }
        });
    }

    public void unAuthenticate(){
        SecurityUtils.getSubject().logout();
    }

    public void registerPrincipal(Principal principal){
        Principal existingPrincipal = repo.findByEmail(principal.getEmail());
        Domain existingDomain = domainRepository.findByName(principal.getEmail());

        if(existingPrincipal !=null || existingDomain !=null){
            throw new IllegalArgumentException("Principal or domain already exist");
        }

        Domain domain = new Domain();
        domain.setActive(true);
        domain.setName(principal.getEmail());
        domainRepository.save(domain);

        principal.setDomain(domain);
        principal.setActive(true);
        principal.setRole(Role.ADMIN);

        repo.save(principal);

        authenticate(principal);
    }

    public List<Principal> changeActive(String id, boolean active){
        Principal principal = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,principal.getDomain());
        principal.setActive(active);
        repo.save(principal);
        return findByDomain(principal.getDomain());
    }

    public List<Principal> changeRole(String id, Role role){
        Principal principal = repo.findOne(id);
        if(Role.GLOBAL_ADMIN.equals(role)){
            AuthorizationProvider.hasRole(Role.GLOBAL_ADMIN,null);
        }else{
            AuthorizationProvider.hasRole(Role.ADMIN,principal.getDomain());
        }
        principal.setRole(role);
        repo.save(principal);
        return findByDomain(principal.getDomain());
    }

    public Principal findByEmail(String email){
        return repo.findByEmail(email);
    }

    public List<Principal> findByDomain(Domain domain){
        return repo.findByDomain(domain);
    }

}
