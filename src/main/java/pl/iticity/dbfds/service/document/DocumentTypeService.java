package pl.iticity.dbfds.service.document;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.document.DocumentTypeRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.ScopedService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

public interface DocumentTypeService extends ScopedService<DocumentType> {

    public List<DocumentType> findByDomain(Domain domain, boolean onlyActive);

    public List<DocumentType> addDocType(DocumentType documentType,Domain domain);

    public List<DocumentType> toggleDocType(String id, boolean toggle);

    public List<DocumentType> deleteDocType(String id);
}
