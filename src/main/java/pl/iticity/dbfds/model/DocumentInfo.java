package pl.iticity.dbfds.model;

import org.hibernate.annotations.Type;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/5/16.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class DocumentInfo {

    public enum Kind {
        INTERNAL,EXTERNAL;
    }

    public enum Type {
        DRAWING, DOCUMENT, MOM, PICTURE
    }

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    @NotNull
    private Classification classification;

    @Max(99999999l)
    @NotNull
    private Long masterDocumentNumber;

    @Size(min=1,max=25)
    @NotNull
    private String documentNumber;

    @Size(min=1,max=100)
    @NotNull
    private String documentName;

    @NotNull
    private Kind kind;

    @NotNull
    private Type type;

    private Date creationDate;

    private Principal createdBy;

    private List<DocumentActivity> activityList;

    private List<FileInfo> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public List<DocumentActivity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<DocumentActivity> activityList) {
        this.activityList = activityList;
    }

    public List<FileInfo> getFiles() {
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
}
