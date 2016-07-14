package pl.iticity.dbfds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.NewDocumentInfoMixIn;
import pl.iticity.dbfds.model.mixins.PrincipalSelectMixin;
import pl.iticity.dbfds.repository.DomainRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Service
public class PrincipalService extends AbstractService<Principal,PrincipalRepository>{

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private ClassificationService classificationService;

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

    public Principal registerPrincipal(Principal principal,boolean signin){
        Principal existingPrincipal = repo.findByEmail(principal.getEmail());
        Domain existingDomain = domainRepository.findByName(principal.getEmail());

        if(existingPrincipal !=null || existingDomain !=null){
            throw new IllegalArgumentException("Principal or domain already exist");
        }

        Domain domain = new Domain();
        domain.setCompany(principal.getCompany());
        domain.setActive(true);
        domain.setCreationDate(new Date());
        domain.setName(principal.getEmail());
        int num = domainRepository.findAll().size()+1;
        String formatted = String.format("%04d", num);
        domain.setAccountNo("CG"+formatted);
        domainRepository.save(domain);

        for(DocumentType documentType : DocumentType.getDefault()){
            documentType.setDomain(domain);
            documentType.setActive(true);
            documentTypeService.save(documentType);
        }

        for(Classification classification : Classification.getDefault()){
            classification.setDomain(domain);
            classification.setActive(true);
            classificationService.save(classification);
        }

        principal.setDomain(domain);
        principal.setActive(true);
        principal.setRole(Role.ADMIN);
        principal.setCreationDate(new Date());

        repo.save(principal);
        principal.getDomain().setNoOfUsers(findByDomain(domain).size());

        if(signin) {
            authenticate(principal);
        }
        return principal;
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

    public void updatePrincipalStringField(String id, String field, String value) {
        Principal principal = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN, principal.getDomain());
        String methodName = "set" + StringUtils.capitalize(field);
        try {
            Method method = Principal.class.getMethod(methodName, String.class);
            method.invoke(principal, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            if(!acronymExistsInDomain(principal.getAcronym(),principal.getDomain())){
                repo.save(principal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean acronymExistsInDomain(String acronym, Domain domain){
        return repo.findByDomainAndAcronym(domain,acronym) != null;
    }

}
