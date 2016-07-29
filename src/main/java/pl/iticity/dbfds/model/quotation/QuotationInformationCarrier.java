package pl.iticity.dbfds.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@org.springframework.data.mongodb.core.mapping.Document(collection = "quotations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationInformationCarrier extends Scoped {

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    @NotNull
    @DBRef
    private Classification classification;

    //referred to as number
    private String symbol;

    private String name;

    private String createdBy;

    private String manager;

    private String clientNumber;

    private String clientName;

    private String clientCountry;

    private String clientVatRegNo;

    private String clientContactPerson;

    private String clientAddress;

    private String startDate;

    private String endDate;

    private String customerInspectionDate;

    private String value;

    private String cost;

    private String grossMargin;

    private String endCustomer;

    private String endCustomerCountry;

    private String endCustomerPlant;

    private String shipmentDate;

    private String deliveryDate;

    private String creationDate;

    private String tba;

    private String deliveryAddress;

    private String comments;

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

    public String getManager() {
        return manager;
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

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCountry() {
        return clientCountry;
    }

    public void setClientCountry(String clientCountry) {
        this.clientCountry = clientCountry;
    }

    public String getClientVatRegNo() {
        return clientVatRegNo;
    }

    public void setClientVatRegNo(String clientVatRegNo) {
        this.clientVatRegNo = clientVatRegNo;
    }

    public String getClientContactPerson() {
        return clientContactPerson;
    }

    public void setClientContactPerson(String clientContactPerson) {
        this.clientContactPerson = clientContactPerson;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getEndCustomer() {
        return endCustomer;
    }

    public void setEndCustomer(String endCustomer) {
        this.endCustomer = endCustomer;
    }

    public String getTba() {
        return tba;
    }

    public void setTba(String tba) {
        this.tba = tba;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCustomerInspectionDate() {
        return customerInspectionDate;
    }

    public void setCustomerInspectionDate(String customerInspectionDate) {
        this.customerInspectionDate = customerInspectionDate;
    }

    public String getEndCustomerPlant() {
        return endCustomerPlant;
    }

    public void setEndCustomerPlant(String endCustomerPlant) {
        this.endCustomerPlant = endCustomerPlant;
    }

    public String getEndCustomerCountry() {
        return endCustomerCountry;
    }

    public void setEndCustomerCountry(String endCustomerCountry) {
        this.endCustomerCountry = endCustomerCountry;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }
}
