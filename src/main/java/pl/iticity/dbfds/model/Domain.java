package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
public class Domain{

    @Id
    @GeneratedValue
    private String id;

    @Indexed(unique = true)
    private String name;

    private boolean active;

    private long lastMasterDocumentNumber;

    @Transient
    private List<Principal> principals;

    @Transient
    private long noOfUsers;

    @Transient
    private long noOfFiles;

    @Transient
    private double memory;

    private String company;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date creationDate;

    public long getLastMasterDocumentNumber() {
        return lastMasterDocumentNumber;
    }

    public void setLastMasterDocumentNumber(long lastMasterDocumentNumber) {
        this.lastMasterDocumentNumber = lastMasterDocumentNumber;
    }

    public List<Principal> getPrincipals() {
        return principals;
    }

    public void setPrincipals(List<Principal> principals) {
        this.principals = principals;
    }

    public long getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(long noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNoOfFiles() {
        return noOfFiles;
    }

    public void setNoOfFiles(long noOfFiles) {
        this.noOfFiles = noOfFiles;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
