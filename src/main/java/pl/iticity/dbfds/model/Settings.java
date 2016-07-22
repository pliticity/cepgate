package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@org.springframework.data.mongodb.core.mapping.Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings extends Scoped {

    @Id
    @GeneratedValue
    private String id;

    private int itemsPerPage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
