package pl.iticity.dbfds.service.document.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.document.DocumentTypeRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.document.DocumentTypeService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class DocumentTypeServiceImpl extends AbstractScopedService<DocumentType,String,DocumentTypeRepository> implements DocumentTypeService {

    @Autowired
    private DomainService domainService;

    public List<DocumentType> findByDomain(String domainId, boolean onlyActive){
        Domain domain = null;
        if(StringUtils.isEmpty(domainId)){
            domain = PrincipalUtils.getCurrentDomain();
        }else{
            domain = domainService.findById(domainId);
            if(domain==null){
                throw new IllegalArgumentException();
            }
        }
        AuthorizationProvider.assertRole(Role.ADMIN,domain);
        if(onlyActive){
            return repo.findByDomainAndActiveIsTrueAndRemovedIsFalse(domain);
        }
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    public List<DocumentType> addDocType(DocumentType documentType,String domainId){
        Domain domain = null;
        if(StringUtils.isEmpty(domainId)){
            domain = PrincipalUtils.getCurrentDomain();
        }else{
            domain = domainService.findById(domainId);
        }
        if(domain==null){
            throw new IllegalArgumentException();
        }
        AuthorizationProvider.assertRole(Role.ADMIN,domain);
        if(repo.findOne(documentType.getId())==null){
            documentType.setId(null);
        }
        documentType.setDomain(domain);
        documentType.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        documentType.setActive(true);
        repo.save(documentType);
        return findByDomain(domain.getId(),false);
    }

    public List<DocumentType> toggleDocType(String id, boolean toggle){
        DocumentType type = repo.findOne(id);
        AuthorizationProvider.assertRole(Role.ADMIN,type.getDomain());
        type.setActive(toggle);
        type.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(type);
        return findByDomain(type.getDomain().getId(),false);
    }

    public List<DocumentType> deleteDocType(String id){
        DocumentType docType = repo.findOne(id);
        AuthorizationProvider.assertRole(Role.ADMIN,docType.getDomain());
        docType.setRemoved(true);
        docType.setActive(false);
        docType.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        repo.save(docType);
        return findByDomain(docType.getDomain().getId(),false);
    }

}
