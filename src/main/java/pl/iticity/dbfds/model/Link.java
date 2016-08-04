package pl.iticity.dbfds.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Link {

    private String documentId;

    private String documentName;

    @DBRef
    @JsonIgnoreProperties(value = {"masterDocumentNumber","kind","type","creationDate","plannedIssueDate","createdBy","responsibleUser","files","domain","securityGroup","removed","favourite","provider","tags","activities","links","comments","revisions","favourites","state","noOfFiles"})
    private DocumentInfo documentInfo;

    private LinkType linkType;

    public Link() {
    }

    public Link(DocumentInfo documentInfo, LinkType linkType) {
        this.documentId = documentInfo.getId();
        this.documentName = documentInfo.getDocumentName();
        this.documentInfo = documentInfo;
        this.linkType = linkType;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

        if (!documentId.equals(link.documentId)) return false;
        if (!documentName.equals(link.documentName)) return false;
        if (!documentInfo.getId().equals(link.documentInfo.getId())) return false;
        return linkType == link.linkType;

    }

    @Override
    public int hashCode() {
        int result = documentId.hashCode();
        result = 31 * result + documentName.hashCode();
        result = 31 * result + documentInfo.getId().hashCode();
        result = 31 * result + linkType.hashCode();
        return result;
    }
}
