package pl.iticity.dbfds.service;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.DocumentTypeRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class DocumentTypeService extends AbstractService<DocumentType,DocumentTypeRepository>{

    public List<DocumentType> findByDomain(Domain domain){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        return repo.findByDomain(domain);
    }

}
