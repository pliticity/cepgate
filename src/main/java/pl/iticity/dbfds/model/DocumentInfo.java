package pl.iticity.dbfds.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/5/16.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class DocumentInfo {

    public enum Kind {
        INTERNAL,EXTERNAL;
    }

    public enum Type {
        DRAWING, DOCUMENT, MOM, PICTURE
    }

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    private Classification classification;

    @Max(99999999l)
    private Long masterDocumentNumber;

    @Size(min=1,max=25)
    private String documentNumber;

    private Kind kind;

    private Type type;

    private Date creationDate;

    private Principal createBy;

    private List<DocumentActivity> activityList;

    @OneToMany(mappedBy = "documentInfo")
    private List<FileInfo> files;

}
