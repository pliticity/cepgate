package pl.iticity.dbfds.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by pmajchrz on 4/5/16.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class File {

    @Id
    @GeneratedValue
    private String id;

    @Lob
    private byte[] data;

    @NotNull
    private String fileInfoId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(String fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

}
