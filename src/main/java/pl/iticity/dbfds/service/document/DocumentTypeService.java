package pl.iticity.dbfds.service.document;

import pl.iticity.dbfds.model.document.DocumentType;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.service.ScopedService;

import java.util.List;

public interface DocumentTypeService extends ScopedService<DocumentType> {

    public List<DocumentType> findByDomain(String domainId, boolean onlyActive);

    public List<DocumentType> addDocType(DocumentType documentType,String domainId);

    public List<DocumentType> toggleDocType(String id, boolean toggle);

    public List<DocumentType> deleteDocType(String id);
}
