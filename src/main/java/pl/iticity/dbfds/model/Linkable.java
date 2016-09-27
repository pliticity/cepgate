package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.common.Link;

import java.util.Set;

public abstract class Linkable extends Scoped {

    @JsonIgnoreProperties(value = {"objectId","objectType"})
    @DBRef
    protected Set<Link> links;

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }
}
