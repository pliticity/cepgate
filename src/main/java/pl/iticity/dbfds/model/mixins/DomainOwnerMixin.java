package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"id","password","domain"})
public class DomainOwnerMixin {
}
