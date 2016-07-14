package pl.iticity.dbfds.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
@CompoundIndexes(value =
        {
                @CompoundIndex(def = "{'classificationId' : 1,'domain' :1}", unique = true)
        }
)
public class Classification {

    public static Classification EMAIL = new Classification("E-Mail","EM",true);

    @Id
    @GeneratedValue
    private String id;

    @Size(min=1,max=25)
    @NotNull
    private String classificationId;

    @NotNull
    private String name;

    @NotNull
    @DBRef
    private Domain domain;

    private boolean active;

    private String type;

    private String[] parentsIds;

    private boolean defaultValue;

    private boolean removed;

    public Classification() {
    }

    public Classification(String name, String classificationId, boolean defaultValue) {
        this.name = name;
        this.classificationId = classificationId;
        this.defaultValue = defaultValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getParentsIds() {
        return parentsIds;
    }

    public void setParentsIds(String[] parentsIds) {
        this.parentsIds = parentsIds;
    }

    @JsonIgnore
    @Transient
    public static List<Classification> getDefault(){
        return Lists.newArrayList(EMAIL);
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
