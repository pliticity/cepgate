package pl.iticity.dbfds.model.mixins.classification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

@JsonIgnoreProperties(value = {"removed","domain","principal"})
public abstract class ListClassificationMixin {

    @JsonIgnoreProperties(value = {"parents","removed","defaultValue","type","active","domain","parentIds"})
    abstract List<Classification> getParents();

}
