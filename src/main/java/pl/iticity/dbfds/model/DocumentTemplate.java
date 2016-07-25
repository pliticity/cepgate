package pl.iticity.dbfds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@org.springframework.data.mongodb.core.mapping.Document
public class DocumentTemplate extends Scoped{

    @Id
    @GeneratedValue
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date date;

    @DBRef
    @NotNull
    private FileInfo file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FileInfo getFile() {
        return file;
    }

    public void setFile(FileInfo file) {
        this.file = file;
    }
}
