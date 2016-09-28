package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.common.Link;

import java.util.List;

public abstract class Linkable extends Scoped {

    @JsonIgnoreProperties(value = {"objectId","objectType"})
    @DBRef
    protected List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
