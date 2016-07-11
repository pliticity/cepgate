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

    public static Classification EMAIL = new Classification("E-Mail","EM");

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

    public Classification() {
    }

    public Classification(String name, String classificationId) {
        this.name = name;
        this.classificationId = classificationId;
    }

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
    public String[] getChildrenIds(){
        if(getChildren().size()<1){
            return new String[0];
        }
        List<String> list = Lists.newArrayList(Iterables.transform(getChildren(), new Function<Classification, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Classification classification) {
                return classification.getClassificationId();
            }
        }));
        return list.toArray(new String[list.size()]);
    }

    public void setChildrenIds(String[] ids){
        for(final String id : Sets.newHashSet(ids)){
            Classification cl = Iterables.find(getChildren(), new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification classification) {
                    return id.equals(classification.getClassificationId());
                }
            }, null);
            if(cl==null) {
                Classification c = new Classification();
                c.setClassificationId(id);
                getChildren().add(c);
            }
        }
    }

    @Transient
    public String[] getParentsIds() {
        if (getParents().size() < 1) {
            return new String[0];
        }
        List<String> list = Lists.newArrayList(Iterables.transform(getParents(), new Function<Classification, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Classification classification) {
                return classification.getClassificationId();
            }
        }));
        return list.toArray(new String[list.size()]);
    }

    public void setParentsIds(String[] ids) {
        for (final String id : Sets.newHashSet(ids)) {
            Classification cl = Iterables.find(getParents(), new Predicate<Classification>() {
                @Override
                public boolean apply(@Nullable Classification classification) {
                    return id.equals(classification.getClassificationId());
                }
            }, null);
            if (cl == null) {
                Classification c = new Classification();
                c.setClassificationId(id);
                getParents().add(c);
            }
        }
    }

    @JsonIgnore
    @Transient
    public static List<Classification> getDefault(){
        return Lists.newArrayList(EMAIL);
    }
}
