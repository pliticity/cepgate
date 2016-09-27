package pl.iticity.dbfds.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.model.Tag;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "documentInfo")
@CompoundIndexes(value =
        {
                @CompoundIndex(def = "{'masterDocumentNumber' : 1,'domain' :1}", unique = true),
                @CompoundIndex(def = "{'documentNumber':1,'domain':1}", unique = true)
        }
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentInformationCarrier extends Linkable {

    public enum Kind {
        INTERNAL, EXTERNAL;
    }

    public enum Provider {
        COMPANY,SUPPLIER,CUSTOMER
    }

    @ManyToOne
    @NotNull
    @DBRef
    private Classification classification;

    @Max(99999999l)
    @NotNull
    private Long masterDocumentNumber;

    @Size(min = 1, max = 25)
    @NotNull
    private String documentNumber;

    @Size(min = 1, max = 100)
    @NotNull
    private String documentName;

    @NotNull
    private Kind kind;

    @NotNull
    @DBRef
    private DocumentType docType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date plannedIssueDate;

    @DBRef
    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain"})
    private Principal createdBy;

    @DBRef
    private Principal responsibleUser;

    @DBRef
    private List<FileInfo> files;

    private String securityGroup;

    private boolean favourite;

    private Provider provider;

    private List<Tag> tags;

    private List<DocumentActivity> activities;

    private List<Comment> comments;

    private List<Revision> revisions;

    private RevisionSymbol revision;

    @JsonIgnore
    private List<DocumentFavourite> favourites;

    private DocumentState state;

    private Date archivedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public DocumentState getState() {
        return state;
    }

    public void setState(DocumentState state) {
        this.state = state;
    }

    public List<DocumentActivity> getActivities() {
        if(activities==null){
            activities = Lists.newArrayList();
        }
        return activities;
    }

    public List<Comment> getComments() {
        if(comments==null){
            comments = Lists.newArrayList();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setActivities(List<DocumentActivity> activities) {
        this.activities = activities;
    }

    public List<DocumentFavourite> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<DocumentFavourite> favourites) {
        this.favourites = favourites;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(String securityGroup) {
        this.securityGroup = securityGroup;
    }

    public Principal getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(Principal responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public Date getPlannedIssueDate() {
        return plannedIssueDate;
    }

    public void setPlannedIssueDate(Date plannedIssueDate) {
        this.plannedIssueDate = plannedIssueDate;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Long getMasterDocumentNumber() {
        return masterDocumentNumber;
    }

    public void setMasterDocumentNumber(Long masterDocumentNumber) {
        this.masterDocumentNumber = masterDocumentNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Principal getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Principal createdBy) {
        this.createdBy = createdBy;
    }

    public List<FileInfo> getFiles() {
        if (files == null) {
            files = Lists.newArrayList();
        }
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getNoOfFiles() {
        return getFiles().size();
    }

    public List<Revision> getRevisions() {
        if(revisions==null){
            revisions = Lists.newArrayList();
        }
        return revisions;
    }

    public RevisionSymbol getRevision() {
        return revision;
    }

    public void setRevision(RevisionSymbol revision) {
        this.revision = revision;
    }

    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }

    public DocumentInformationCarrier clone(){
        DocumentInformationCarrier documentInformationCarrier = new DocumentInformationCarrier();
        documentInformationCarrier.setCreatedBy(PrincipalUtils.getCurrentPrincipal());
        documentInformationCarrier.setKind(getKind());
        documentInformationCarrier.setRemoved(false);
        documentInformationCarrier.setCreationDate(new Date());
        documentInformationCarrier.setClassification(getClassification());
        documentInformationCarrier.setDocumentName(getDocumentName());
        documentInformationCarrier.setDomain(getDomain());
        documentInformationCarrier.setPlannedIssueDate(getPlannedIssueDate());
        documentInformationCarrier.setResponsibleUser(getResponsibleUser());
        documentInformationCarrier.setSecurityGroup(getSecurityGroup());
        documentInformationCarrier.setDocType(getDocType());
        documentInformationCarrier.setProvider(getProvider());
        documentInformationCarrier.setTags(getTags());
        documentInformationCarrier.setRevision(new RevisionSymbol());
        documentInformationCarrier.setState(DocumentState.IN_PROGRESS);
        return documentInformationCarrier;
    }

}
