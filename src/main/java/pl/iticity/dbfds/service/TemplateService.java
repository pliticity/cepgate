package pl.iticity.dbfds.service;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentTemplate;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.repository.DocumentTemplateRepository;
import pl.iticity.dbfds.repository.DocumentTypeRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.Date;
import java.util.List;

@Service
public class TemplateService extends AbstractService<DocumentTemplate,DocumentTemplateRepository> {

    public List<DocumentTemplate> findByDomain(Domain domain){
        return repo.findByDomain(domain);
    }

    public DocumentTemplate create(FileInfo fileInfo){
        DocumentTemplate template = new DocumentTemplate();
        template.setDate(new Date());
        template.setDomain(PrincipalUtils.getCurrentDomain());
        template.setFile(fileInfo);
        return repo.save(template);
    }

}
