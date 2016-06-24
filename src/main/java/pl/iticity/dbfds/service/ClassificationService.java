package pl.iticity.dbfds.service;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ClassificationRepository;
import pl.iticity.dbfds.repository.DocumentTypeRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class ClassificationService extends AbstractService<Classification,ClassificationRepository>{

    public List<Classification> findByDomain(Domain domain, boolean onlyActive){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        if(onlyActive){
            return repo.findByDomainAndActiveIsTrue(domain);
        }
        return repo.findByDomain(domain);
    }

    public List<Classification> addClassification(Classification documentType,Domain domain){
        documentType.setDomain(domain);
        documentType.setActive(true);
        repo.save(documentType);
        return findByDomain(domain,false);
    }

    public List<Classification> toggleClassification(String id, boolean toggle){
        Classification classification = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,classification.getDomain());
        classification.setActive(toggle);
        repo.save(classification);
        return findByDomain(classification.getDomain(),false);
    }

}
