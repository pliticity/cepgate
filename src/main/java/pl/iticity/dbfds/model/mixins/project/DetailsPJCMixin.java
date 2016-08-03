package pl.iticity.dbfds.model.mixins.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"domain"})
public abstract class DetailsPJCMixin {

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain","creationDate","url","active"})
    abstract Principal getPrincipal();

}
