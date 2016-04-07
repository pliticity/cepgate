package pl.iticity.dbfds.model;

import com.vaadin.data.fieldgroup.PropertyId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by pmajchrz on 4/5/16.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Classification {

    @Id
    @GeneratedValue
    private String id;

    @Size(min=1,max=25)
    @NotNull
    @PropertyId("classification.classificationId")
    private String classificationId;

    @Size(min=1,max=100)
    @NotNull
    @PropertyId("classification.name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
