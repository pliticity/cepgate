package pl.iticity.dbfds.security;


import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by dacho on 23.03.2016.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Principal {

    @Id
    @GeneratedValue
    private String id;

    @NotNull(message = "Cannot be empty")
    @javax.validation.constraints.Size(min=1)
    @Column(unique = true)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message = "Wrong email format")
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

    private String company;

    @NotNull
    private Role role;

    public Principal() {
        /*email = StringUtils.EMPTY;
        password = StringUtils.EMPTY;
        firstName = StringUtils.EMPTY;
        lastName = StringUtils.EMPTY;
        country = StringUtils.EMPTY;
        phone = StringUtils.EMPTY;
        company = StringUtils.EMPTY;*/
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

    @Override
    public String toString() {
        return getEmail();
    }
}
