package pl.iticity.dbfds.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentTemplate;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface DocumentTemplateRepository extends ScopedRepository<DocumentTemplate,String> {

    public List<DocumentTemplate> findByDomain(Domain domain);

}
