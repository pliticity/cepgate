package pl.iticity.dbfds.model;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@org.springframework.data.mongodb.core.mapping.Document
public class Domain {

    @Id
    @GeneratedValue
    private String id;

    @Indexed(unique = true)
    private String name;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
