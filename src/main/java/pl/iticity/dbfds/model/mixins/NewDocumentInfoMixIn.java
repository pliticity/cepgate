package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "files", "domain", "securityGroup", "removed", "favourite","id","classification","documentName","type","noOfFiles","activities","comments","revisions"})
public abstract class NewDocumentInfoMixIn {

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain"})
    abstract Principal getCreatedBy();

}
