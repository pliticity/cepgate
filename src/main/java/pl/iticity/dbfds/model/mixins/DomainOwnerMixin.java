package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"id","email","password","role","domain","active","creationDate"})
public class DomainOwnerMixin {
}
