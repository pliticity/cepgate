package pl.iticity.dbfds.model.mixins.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.security.Principal;

@JsonIgnoreProperties(value = {"domain","principal"})
public abstract class ListPJCMixin {

}
