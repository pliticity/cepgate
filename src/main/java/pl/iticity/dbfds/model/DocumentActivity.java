package pl.iticity.dbfds.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class DocumentActivity {

    public enum ActivityType {
        OPENED, SAVED, CREATED
    }

    @NotNull
    private ActivityType type;

    @DBRef
    private Principal principal;

    @NotNull
    private Date date;

    public DocumentActivity(ActivityType type, Principal principal, Date date) {
        this.type = type;
        this.principal = principal;
        this.date = date;
    }

    public DocumentActivity() {
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
