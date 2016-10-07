package pl.iticity.dbfds.repository.document;

import pl.iticity.dbfds.model.document.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface DocumentTypeRepository extends ScopedRepository<DocumentType, String> {

    public List<DocumentType> findByDomainAndRemovedIsFalse(Domain domain);

    public List<DocumentType> findByDomainAndActiveIsTrueAndRemovedIsFalse(Domain domain);

}
