package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"domain","password","country","phone","company"})
public abstract class PrincipalMixin {
}
