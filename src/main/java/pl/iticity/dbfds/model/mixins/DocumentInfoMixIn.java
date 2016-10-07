package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.document.DocumentType;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.model.document.RevisionSymbol;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "masterDocumentNumber", "domain", "securityGroup", "removed", "favourite","provider","comments","revisions","activities","links","state","archivedDate"})
public abstract class DocumentInfoMixIn {

    @JsonIgnoreProperties(value = {"id", "password", "lastName", "firstName", "country", "phone", "company", "role", "domain","url","creationDate","active"})
    abstract Principal getCreatedBy();

    @JsonIgnoreProperties(value = {"path","uploadDate","type","domain","size"})
    abstract List<FileInfo> getFiles();

    @JsonIgnoreProperties(value = {"children","id","parents","active","domain","childrenIds","parentsIds"})
    abstract Classification getClassification();

    @JsonIgnoreProperties(value = {"id","active","domain"})
    abstract DocumentType getDocType();

    @JsonIgnoreProperties(value = {"number","prefix"})
    abstract RevisionSymbol getRevision();

}
