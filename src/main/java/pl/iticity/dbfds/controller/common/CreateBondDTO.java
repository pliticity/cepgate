package pl.iticity.dbfds.controller.common;

public class CreateBondDTO {

    private String firstId;

    private String firstType;

    private String firstRevision;

    private String secondId;

    private String secondType;

    private String secondRevision;

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getFirstRevision() {
        return firstRevision;
    }

    public void setFirstRevision(String firstRevision) {
        this.firstRevision = firstRevision;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getSecondRevision() {
        return secondRevision;
    }

    public void setSecondRevision(String secondRevision) {
        this.secondRevision = secondRevision;
    }
}
