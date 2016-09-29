package pl.iticity.dbfds.model.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.common.ClassificationType;

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
public class Classification extends Scoped {

    public static Classification EMAIL = new Classification("E-Mail", "EM", true);

    @Size(min = 1, max = 25)
    @NotNull
    private String classificationId;

    @NotNull
    private String name;

    private boolean active;

    private ClassificationType type;

    private String modelId;

    private String modelClazz;

    @DBRef
    @JsonIgnoreProperties(value = {"parents","removed","defaultValue","type","active","domain","parentIds"})
    private List<Classification> parents;

    private boolean defaultValue;

    @Transient
    private boolean assigned;

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

    public ClassificationType getType() {
        return type;
    }

    public void setType(ClassificationType type) {
        this.type = type;
    }

    public List<Classification> getParents() {
        return parents;
    }

    public void setParents(List<Classification> parents) {
        this.parents = parents;
    }

    @JsonIgnore
    @Transient
    public static List<Classification> getDefault() {
        return Lists.newArrayList(EMAIL);
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParentIds() {
        if (getParents() != null && !getParents().isEmpty()) {
            return getParents().get(0).getId();
        }
        return null;
    }

    public void setParentIds(String parentIds) {
        if (StringUtils.isNotEmpty(parentIds)) {
            Classification c = new Classification();
            c.setId(parentIds);
            this.parents = Lists.newArrayList(c);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Classification that = (Classification) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelClazz() {
        return modelClazz;
    }

    public void setModelClazz(String modelClazz) {
        this.modelClazz = modelClazz;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
