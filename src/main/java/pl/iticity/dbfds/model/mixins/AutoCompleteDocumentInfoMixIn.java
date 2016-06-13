package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "files", "domain", "securityGroup", "removed", "favourite","classification","type","noOfFiles","activities","createdBy","provider","links","tags","masterDocumentNumber","documentNumber","creationDate"})
public abstract class AutoCompleteDocumentInfoMixIn {

}
