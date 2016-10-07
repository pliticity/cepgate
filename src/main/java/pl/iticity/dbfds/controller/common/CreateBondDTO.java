package pl.iticity.dbfds.controller.common;

public class CreateBondDTO {

    private String firstId;

    private String firstType;

    private boolean firstRevision;

    private String secondId;

    private String secondType;

    private boolean secondRevision;

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

    public boolean isFirstRevision() {
        return firstRevision;
    }

    public void setFirstRevision(boolean firstRevision) {
        this.firstRevision = firstRevision;
    }

    public boolean isSecondRevision() {
        return secondRevision;
    }

    public void setSecondRevision(boolean secondRevision) {
        this.secondRevision = secondRevision;
    }
}
