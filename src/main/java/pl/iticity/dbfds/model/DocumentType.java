package pl.iticity.dbfds.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
public class DocumentType {

    public static final DocumentType XD = new DocumentType("XX","Drawing",true);
    public static final DocumentType DOC = new DocumentType("DOC","Document",true);
    public static final DocumentType MOM = new DocumentType("MOM","Minutes of Meeting",true);
    public static final DocumentType PI = new DocumentType("PI","Picture",true);
    public static final DocumentType EMAIL = new DocumentType("e-mail","e-mail",true);

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    private String typeId;

    @NotNull
    private String name;

    @NotNull
    @DBRef
    private Domain domain;

    private boolean active;

    private boolean defaultValue;

    private boolean removed;

    public DocumentType() {
    }

    public DocumentType(String id, String name,boolean defaultValue) {
        this.typeId = id;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @JsonIgnore
    @Transient
    public static List<DocumentType> getDefault(){
        return Lists.newArrayList(XD,DOC,MOM,PI,EMAIL);
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
