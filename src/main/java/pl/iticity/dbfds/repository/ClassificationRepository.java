package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;

import java.util.List;

public interface ClassificationRepository extends MongoRepository<Classification,String> {

    public List<Classification> findByDomainAndRemovedIsFalse(Domain domain);

    public List<Classification> findByDomainAndActiveIsTrueAndRemovedIsFalse(Domain domain);

    public List<Classification> findByDomainAndIdNotAndRemovedIsFalse(Domain domain, String id);

    public Classification findByDomainAndClassificationId(Domain domain, String clId);

}
