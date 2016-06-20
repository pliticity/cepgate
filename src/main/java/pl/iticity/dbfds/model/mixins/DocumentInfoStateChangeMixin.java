package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"plannedIssueDate", "kind", "responsibleUser", "files", "domain", "securityGroup", "removed", "favourite","classification","type","noOfFiles","activities","createdBy","provider","links","tags","masterDocumentNumber","documentNumber","creationDate","comments","revisions","revision","id","documentName"})
public class DocumentInfoStateChangeMixin {
}
