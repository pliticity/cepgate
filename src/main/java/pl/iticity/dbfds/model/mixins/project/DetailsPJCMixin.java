package pl.iticity.dbfds.model.mixins.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"domain","principal"})
public abstract class DetailsPJCMixin {

}
