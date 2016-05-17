package pl.iticity.dbfds.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.DomainRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class PrincipalService extends AbstractService<Principal,PrincipalRepository>{

    @Autowired
    private DomainRepository domainRepository;

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
        principal.setRole(Role.ADMIN);

        repo.save(principal);

        authenticate(principal);
    }

    public Principal findByEmail(String email){
        return repo.findByEmail(email);
    }

    public List<Principal> findByDomain(Domain domain){
        return repo.findByDomain(domain);
    }

}
