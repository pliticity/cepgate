package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;

import java.util.List;

public interface DocumentTypeRepository extends MongoRepository<DocumentType,String> {

    public List<DocumentType> findByDomain(Domain domain);

}
