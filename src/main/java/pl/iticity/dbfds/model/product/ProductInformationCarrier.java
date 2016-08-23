package pl.iticity.dbfds.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.security.Principal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
@CompoundIndexes(value =
        {
                @CompoundIndex(def = "{'masterProductNumber' : 1,'domain' :1}", unique = true)
        }
)
public class ProductInformationCarrier extends Scoped implements Linkable{

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    @NotNull
    @DBRef
    private Classification classification;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 1)
    private String productId;

    private String weight;

    private String codeA;

    private String codeB;

    private String color;

    private String serialNumber;

    @NotNull
    private String masterProductNumber;

    private String hsCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private Date creationDate;

    @DBRef
    private Principal responsibleUser;

    @DBRef
    private Principal owner;

    private String country;

    private String deliveryLocation;

    private String manufacturingLocation;

    private ProductLifeCycle productLifeCycle;

    private ProductState state;

    private String deliveryTime;

    private String internalComments;

    private String oneLineDescription;

    private String salesText;

    private ProductOwner productOwner;

    private List<Link> links;

    @Override
    public List<Link> getLinks() {
        if(links==null){
            links = Lists.newArrayList();
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public String getCodeB() {
        return codeB;
    }

    public void setCodeB(String codeB) {
        this.codeB = codeB;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMasterProductNumber() {
        return masterProductNumber;
    }

    public void setMasterProductNumber(String masterProductNumber) {
        this.masterProductNumber = masterProductNumber;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Principal getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(Principal responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public Principal getOwner() {
        return owner;
    }

    public void setOwner(Principal owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getManufacturingLocation() {
        return manufacturingLocation;
    }

    public void setManufacturingLocation(String manufacturingLocation) {
        this.manufacturingLocation = manufacturingLocation;
    }

    public ProductLifeCycle getProductLifeCycle() {
        return productLifeCycle;
    }

    public void setProductLifeCycle(ProductLifeCycle productLifeCycle) {
        this.productLifeCycle = productLifeCycle;
    }

    public ProductState getState() {
        return state;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getInternalComments() {
        return internalComments;
    }

    public void setInternalComments(String internalComments) {
        this.internalComments = internalComments;
    }

    public String getOneLineDescription() {
        return oneLineDescription;
    }

    public void setOneLineDescription(String oneLineDescription) {
        this.oneLineDescription = oneLineDescription;
    }

    public String getSalesText() {
        return salesText;
    }

    public void setSalesText(String salesText) {
        this.salesText = salesText;
    }

    public ProductOwner getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(ProductOwner productOwner) {
        this.productOwner = productOwner;
    }

    /**
     *
     */

    public String getProductLine(){
        return getClassificationLevelByType("Product Line");
    }

    public String getProductFamily(){
        return getClassificationLevelByType("Product Family");
    }

    public String getProductGroup(){
        return getClassificationLevelByType("Product Group");
    }

    private String getClassificationLevelByType(String type){
        String level = "missing";
        if(getClassification()!=null && type != null){
            Classification c = recursive(getClassification(),type);
            if(c!=null){
                level = c.getName();
            }
        }
        return level;
    }

    private Classification recursive(Classification c, String type){
        if(type.equals(c.getType())){
            return c;
        }else if(c.getParents() == null || c.getParents().isEmpty()){
            return null;
        }else{
            for(Classification cl : c.getParents()){
                Classification cla = recursive(cl,type);
                if(cla != null){
                    return cla;
                }
            }
            return null;
        }
    }
}
