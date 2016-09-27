package pl.iticity.dbfds.model.common;

import pl.iticity.dbfds.model.BaseModel;

import javax.validation.constraints.NotNull;

@org.springframework.data.mongodb.core.mapping.Document
public class Link extends BaseModel {

    @NotNull
    private String objectId;

    @NotNull
    private LinkedObjectType objectType;

    @NotNull
    private LinkType linkType;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public LinkedObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(LinkedObjectType objectType) {
        this.objectType = objectType;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (!objectId.equals(link.objectId)) return false;
        if (objectType != link.objectType) return false;
        return linkType == link.linkType;

    }

    @Override
    public int hashCode() {
        int result = objectId.hashCode();
        result = 31 * result + objectType.hashCode();
        result = 31 * result + linkType.hashCode();
        return result;
    }
}
