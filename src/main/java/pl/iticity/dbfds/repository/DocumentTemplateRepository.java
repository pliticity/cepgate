package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentTemplate;
import pl.iticity.dbfds.model.Domain;

import java.util.List;

public interface DocumentTemplateRepository extends MongoRepository<DocumentTemplate, String> {

    public List<DocumentTemplate> findByDomain(Domain domain);

}
