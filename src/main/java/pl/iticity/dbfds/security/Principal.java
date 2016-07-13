package pl.iticity.dbfds.security;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by dacho on 23.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@org.springframework.data.mongodb.core.mapping.Document
public class Principal {

    @Id
    @GeneratedValue
    private String id;

    @NotNull(message = "Cannot be empty")
    @javax.validation.constraints.Size(min=1)
    @Column(unique = true)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message = "Wrong email format")
    @Indexed(unique = true)
    private String email;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String password;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String lastName;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String firstName;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String country;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String phone;

    @Size(min = 1)
    @NotNull(message = "Cannot be empty")
    private String company;

    private String url;

    @NotNull
    private Role role;

    @NotNull
    @DBRef
    private Domain domain;

    private boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date creationDate;

    public Principal(String email,String password){
        setEmail(email);
        setPassword(password);
    }

    public Principal() {
        /*email = StringUtils.EMPTY;
        password = StringUtils.EMPTY;
        firstName = StringUtils.EMPTY;
        lastName = StringUtils.EMPTY;
        country = StringUtils.EMPTY;
        phone = StringUtils.EMPTY;
        company = StringUtils.EMPTY;*/
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Principal)){
            return false;
        }else{
            Principal that = (Principal) obj;
            if(StringUtils.isEmpty(this.getId()) || StringUtils.isEmpty(that.getId())){
                return false;
            }
            return this.getId().equals(that.getId());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAcronym(){
        StringBuilder builder = new StringBuilder();
        if(!StringUtils.isEmpty(getFirstName())){
            builder.append(getFirstName().toUpperCase().charAt(0));
        }
        if(!StringUtils.isEmpty(getLastName())) {
            builder.append(getLastName().toUpperCase().charAt(0));
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getEmail();
    }
}
