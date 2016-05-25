package pl.iticity.dbfds.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document
public class DocumentActivity {

    public enum ActivityType {
        OPENED, SAVED, CREATED
    }

    @Id
    @GeneratedValue
    private String id;

    @DBRef
    @NotNull
    private DocumentInfo documentInfo;

    @NotNull
    private ActivityType type;

    @DBRef
    private Principal principal;

    @NotNull
    private Date date;

    public DocumentActivity(DocumentInfo documentInfo, ActivityType type, Principal principal, Date date) {
        this.documentInfo = documentInfo;
        this.type = type;
        this.principal = principal;
        this.date = date;
    }

    public DocumentActivity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
