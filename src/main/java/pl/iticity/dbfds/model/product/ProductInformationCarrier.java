package pl.iticity.dbfds.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@org.springframework.data.mongodb.core.mapping.Document(collection = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInformationCarrier {

    @Id
    @GeneratedValue
    private String id;

    @DBRef
    private Domain domain;

    @ManyToOne
    @NotNull
    @DBRef
    private Classification classification;

    private String name;

    private String productId;

    private String weight;

    private String codeA;

    private String codeB;

    private String color;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
