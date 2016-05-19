package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    @Indexed(unique = true)
    private Long masterDocumentNumber;

    @Size(min=1,max=25)
    @NotNull
    @Indexed(unique = true)
    private String documentNumber;

    @Size(min=1,max=100)
    @NotNull
    private String documentName;

    @NotNull
    private Kind kind;

    @NotNull
    private Type type;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creationDate;

    @DBRef
    private Principal createdBy;

    private DocumentActivity lastActivity;

    @DBRef
    private List<FileInfo> files;

    @NotNull
    @DBRef
    private Domain domain;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

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

    public List<FileInfo> getFiles() {
        if(files==null){
            files= Lists.newArrayList();
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

    public int getNoOfFiles(){
        return getFiles().size();
    }

    public DocumentActivity getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(DocumentActivity lastActivity) {
        this.lastActivity = lastActivity;
    }
}
