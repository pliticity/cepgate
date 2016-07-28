package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
public class Domain{

    @Id
    @GeneratedValue
    private String id;

    @Indexed(unique = true)
    private String name;

    private String accountNo;

    private boolean active;

    private long lastMasterDocumentNumber;

    private long lastMasterProductNumber;

    @Transient
    private Principal owner;

    @Transient
    private List<Principal> principals;

    @Transient
    private long noOfUsers;

    @Transient
    private long noOfFiles;

    @Transient
    private String memory;

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

    public String getMemory() {
        return memory;
    }

    public void setMemory(BigDecimal bigDecimal) {
        this.memory = bigDecimal.toPlainString();
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Principal getOwner() {
        return owner;
    }

    public void setOwner(Principal owner) {
        this.owner = owner;
    }

    public long getLastMasterProductNumber() {
        return lastMasterProductNumber;
    }

    public void setLastMasterProductNumber(long lastMasterProductNumber) {
        this.lastMasterProductNumber = lastMasterProductNumber;
    }
}
