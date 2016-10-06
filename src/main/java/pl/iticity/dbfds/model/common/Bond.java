package pl.iticity.dbfds.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Scoped;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(value = {"principal.password"})
@org.springframework.data.mongodb.core.mapping.Document
public class Bond extends Scoped {

    @NotNull
    private ObjectType firstType;

    @NotNull
    private String firstId;

    private String firstRevision;

    @NotNull
    private ObjectType secondType;

    @NotNull
    private String secondId;

    private String secondRevision;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creationDate;

    @NotNull
    private BondType bondType;

    public ObjectType getFirstType() {
        return firstType;
    }

    public void setFirstType(ObjectType firstType) {
        this.firstType = firstType;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getFirstRevision() {
        return firstRevision;
    }

    public void setFirstRevision(String firstRevision) {
        this.firstRevision = firstRevision;
    }

    public ObjectType getSecondType() {
        return secondType;
    }

    public void setSecondType(ObjectType secondType) {
        this.secondType = secondType;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getSecondRevision() {
        return secondRevision;
    }

    public void setSecondRevision(String secondRevision) {
        this.secondRevision = secondRevision;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BondType getBondType() {
        return bondType;
    }

    public void setBondType(BondType bondType) {
        this.bondType = bondType;
    }
}
