package pl.iticity.dbfds.model.mixins.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"domain","removed"})
public abstract class DetailsPICMixin {

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain","url","active","acronym","creationDate"})
    abstract Principal getPrincipal();

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain","url","active","acronym","creationDate"})
    abstract Principal getResponsibleUser();

    @JsonIgnoreProperties(value = {"password", "lastName", "firstName", "country", "phone", "company", "role", "domain","url","active","acronym","creationDate"})
    abstract Principal getOwner();

    @JsonIgnoreProperties(value = {"domain","active","type","parentsIds","defaultValue","removed"})
    abstract Classification getClassification();

}
