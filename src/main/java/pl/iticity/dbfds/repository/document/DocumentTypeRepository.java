package pl.iticity.dbfds.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface DocumentTypeRepository extends MongoRepository<DocumentType,String>,ScopedRepository<DocumentType> {

    public List<DocumentType> findByDomainAndRemovedIsFalse(Domain domain);

    public List<DocumentType> findByDomainAndActiveIsTrueAndRemovedIsFalse(Domain domain);

}
