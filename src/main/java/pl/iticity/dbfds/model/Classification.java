package pl.iticity.dbfds.model;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.data.annotation.Transient;
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
public class Classification {

    @Id
    @GeneratedValue
    private String id;

    @Size(min=1,max=25)
    @NotNull
    private String classificationId;

    @NotNull
    private String name;

    @DBRef
    private List<Classification> children;

    @DBRef
    private List<Classification> parents;

    @NotNull
    @DBRef
    private Domain domain;

    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Classification> getChildren() {
        if(children==null){
            children = Lists.newArrayList();
        }
        return children;
    }

    public void setChildren(List<Classification> children) {
        this.children = children;
    }

    public List<Classification> getParents() {
        if(parents==null){
            parents = Lists.newArrayList();
        }
        return parents;
    }

    public void setParents(List<Classification> parents) {
        this.parents = parents;
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

    @Transient
    public String getChildrenIds(){
        return Joiner.on(",").join(Iterables.transform(getChildren(), new Function<Classification, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Classification classification) {
                return classification.getClassificationId();
            }
        }));
    }

    public void setChildrenIds(String ids){
        //do nothing
    }

    @Transient
    public String getParentsIds(){
        return Joiner.on(",").join(Iterables.transform(getParents(), new Function<Classification, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Classification classification) {
                return classification.getClassificationId();
            }
        }));
    }

    public void setParentsIds(String ids){
        //do nothing
    }
}
