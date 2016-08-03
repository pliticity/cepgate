package pl.iticity.dbfds.model.mixins.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"domain"})
public abstract class DetailsQICMixin {

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain","creationDate","url","active"})
    abstract Principal getPrincipal();

}
