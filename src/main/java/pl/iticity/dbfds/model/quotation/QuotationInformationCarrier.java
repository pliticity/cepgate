package pl.iticity.dbfds.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@org.springframework.data.mongodb.core.mapping.Document(collection = "quotations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationInformationCarrier extends Scoped {

    @Id
    @GeneratedValue
    private String id;

    //referred to as number
    private String symbol;

    private String name;

    private Principal manager;

    private String client;

    private Date startDate;

    private Date endDate;

    private String value;

    private String cost;

    private String grossMargin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Principal getManager() {
        return manager;
    }

    public void setManager(Principal manager) {
        this.manager = manager;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(String grossMargin) {
        this.grossMargin = grossMargin;
    }
}
