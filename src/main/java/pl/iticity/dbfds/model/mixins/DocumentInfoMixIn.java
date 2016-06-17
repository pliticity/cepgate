package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "masterDocumentNumber", "domain", "securityGroup", "removed", "favourite","provider","comments","revisions"})
public abstract class DocumentInfoMixIn {

    @JsonIgnoreProperties(value = {"id", "password", "lastName", "firstName", "country", "phone", "company", "role", "domain"})
    abstract Principal getCreatedBy();

    @JsonIgnoreProperties(value = {"path","symbol","uploadDate","type"})
    abstract List<FileInfo> getFiles();
}
