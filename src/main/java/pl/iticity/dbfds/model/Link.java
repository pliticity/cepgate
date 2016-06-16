package pl.iticity.dbfds.model;


public class Link {

    private String documentId;

    private String documentName;

    private LinkType linkType;

    public Link() {
    }

    public Link(String documentId, String documentName, LinkType linkType) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.linkType = linkType;
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
}
