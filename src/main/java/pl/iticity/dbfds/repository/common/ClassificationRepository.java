package pl.iticity.dbfds.repository.common;

import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface ClassificationRepository extends ScopedRepository<Classification,String> {

    public List<Classification> findByDomainAndActiveIsTrueAndRemovedIsFalse(Domain domain);

    public List<Classification> findByDomainAndIdNotAndRemovedIsFalse(Domain domain, String id);

    public Classification findByDomainAndClassificationId(Domain domain, String clId);

    public List<Classification> findByDomainAndActiveIsTrueAndRemovedIsFalseAndTypeIn(Domain domain, List<ClassificationType> type);
}
