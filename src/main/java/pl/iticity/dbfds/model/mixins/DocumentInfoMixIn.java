package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "masterDocumentNumber", "files", "domain", "securityGroup", "removed", "favourite"})
public abstract class DocumentInfoMixIn {

    @JsonIgnoreProperties(value = {"tags"})
    abstract Classification getClassification();

    @JsonIgnoreProperties(value = {"id", "password", "lastName", "firstName", "country", "phone", "company", "role", "domain"})
    abstract Principal getCreatedBy();

}
