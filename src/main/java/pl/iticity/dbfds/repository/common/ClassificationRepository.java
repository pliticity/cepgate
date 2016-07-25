package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface ClassificationRepository extends MongoRepository<Classification,String>, ScopedRepository<Classification> {

    public List<Classification> findByDomainAndActiveIsTrueAndRemovedIsFalse(Domain domain);

    public List<Classification> findByDomainAndIdNotAndRemovedIsFalse(Domain domain, String id);

    public Classification findByDomainAndClassificationId(Domain domain, String clId);

}
