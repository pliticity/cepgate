package pl.iticity.dbfds.service.common.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.document.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.mixins.PrincipalSelectMixin;
import pl.iticity.dbfds.repository.common.DomainRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.repository.common.PrincipalRepository;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.ClassificationService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.Date;
import java.util.List;

@Service
public class PrincipalServiceImpl extends AbstractService<Principal,String,PrincipalRepository> implements PrincipalService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private DomainService domainService;

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
                return PrincipalUtils.hashPassword(principal.getPassword());
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
        domain.setUrl(principal.getUrl());
        domain.setCountry(principal.getCountry());
        domain.setPhone(principal.getPhone());
        domain.setActive(true);
        domain.setCreationDate(new Date());
        domain.setName(principal.getEmail());
        String pass = principal.getPassword();
        principal.setPassword(PrincipalUtils.hashPassword(pass));
        int num = domainRepository.findAll().size()+1;
        String formatted = String.format("%04d", num);
        domain.setAccountNo("CG"+formatted);
        domainRepository.save(domain);

        principal.setDomain(domain);
        principal.setActive(true);
        principal.setRole(Role.ADMIN);
        principal.setCreationDate(new Date());

        repo.save(principal);

        for(DocumentType documentType : DocumentType.getDefault()){
            documentType.setDomain(domain);
            documentType.setActive(true);
            documentType.setPrincipal(principal);
            documentTypeService.save(documentType);
        }

        for(Classification classification : Classification.getDefault()){
            classification.setDomain(domain);
            classification.setActive(true);
            classification.setPrincipal(principal);
            classificationService.save(classification);
        }


        principal.getDomain().setNoOfUsers(findByDomain(domain).size());

        if(signin) {
            principal.setPassword(pass);
            authenticate(principal);
        }
        return principal;
    }

    public List<Principal> changeActive(String id, boolean active){
        Principal principal = repo.findOne(id);
        AuthorizationProvider.assertRole(Role.ADMIN,principal.getDomain());
        principal.setActive(active);
        repo.save(principal);
        return findByDomain(principal.getDomain());
    }

    public List<Principal> changeRole(String id, Role role){
        Principal principal = repo.findOne(id);
        if(Role.GLOBAL_ADMIN.equals(role)){
            AuthorizationProvider.assertRole(Role.GLOBAL_ADMIN,null);
        }else{
            AuthorizationProvider.assertRole(Role.ADMIN,principal.getDomain());
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

    public boolean acronymExistsInDomain(String id, String acronym, Domain domain){
        Principal p = repo.findByDomainAndAcronym(domain,acronym);
        return p !=null && !p.getId().equals(id);
    }

    @Override
    public Principal addPrincipal(Principal principal, String domainId) {
        AuthorizationProvider.assertRole(Role.ADMIN,domainId == null ? PrincipalUtils.getCurrentDomain() : domainService.findById(domainId));
        principal.setRole(Role.USER);
        if (domainId != null) {
            principal.setDomain(domainService.findById(domainId));
        } else {
            principal.setDomain(PrincipalUtils.getCurrentDomain());
        }
        principal.setPassword(PrincipalUtils.hashPassword(principal.getPassword()));
        principal.setCountry(PrincipalUtils.getCurrentPrincipal().getCountry());
        principal.setPhone("12");
        principal.setCreationDate(new Date());
        principal.setCompany(PrincipalUtils.getCurrentPrincipal().getCompany());
        return save(principal);
    }

    @Override
    public void changePassword(Principal principal) {
        if (StringUtils.isNotEmpty(principal.getPassword())) {
            Principal p = PrincipalUtils.getCurrentPrincipal();
            p.setPassword(PrincipalUtils.hashPassword(principal.getPassword()));
            repo.save(p);
        }
    }

}
