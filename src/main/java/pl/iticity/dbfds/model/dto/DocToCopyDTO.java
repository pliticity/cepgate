package pl.iticity.dbfds.model.dto;

import java.util.List;

public class DocToCopyDTO {

    private String id;

    private List<String> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
