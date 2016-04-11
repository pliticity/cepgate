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
public class Classification {

    @Size(min=1,max=25)
    @NotNull
    private String classificationId;

    @Size(min=1,max=100)
    @NotNull
    private String name;

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
