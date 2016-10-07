package pl.iticity.dbfds.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class BaseModel {

    @Id
    @GeneratedValue
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
