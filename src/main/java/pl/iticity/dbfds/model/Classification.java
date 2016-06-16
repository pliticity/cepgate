package pl.iticity.dbfds.model;


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

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public Classification clone(){
        Classification classification = new Classification();
        classification.setName(getName());
        classification.setClassificationId(getClassificationId());
        return classification;
    }
}
