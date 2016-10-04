package pl.iticity.dbfds.service.common;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.model.product.ProductInformationCarrier;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.model.quotation.QuotationInformationCarrier;
import pl.iticity.dbfds.security.Principal;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;

@Component
public class ClassificationHelper {

    private static final Logger logger = Logger.getLogger(ClassificationHelper.class);

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createModelClassification(Domain domain, Principal principal, ClassificationType type, String name, String classificationId, String modelId, Class modelClass, Classification modelClassification) {
        Classification existing = getExistingClassification(modelId, modelClass);
        if (existing != null) {
            existing.setName(name);
            existing.setClassificationId(classificationId);
            existing.setParents(Lists.<Classification>newArrayList(modelClassification));
            classificationService.save(existing);
        } else {
            if (domain != null && principal != null && classificationId != null && !classificationService.exists(classificationId, null, null)) {
                Classification classification = new Classification();
                if (modelClassificationExists(modelClassification)) {
                    modelClassification = classificationService.findById(modelClassification.getId());
                    if (modelClassification != null) {
                        classification.setParents(Lists.newArrayList(modelClassification));
                    }
                }
                classification.setId("-1");
                classification.setDomain(domain);
                classification.setActive(true);
                classification.setPrincipal(principal);
                classification.setRemoved(false);
                classification.setType(type);
                classification.setName(name);
                classification.setClassificationId(classificationId);
                classification.setModelId(modelId);
                classification.setModelClazz(modelClass.getName());
                classificationService.addClassification(classification, domain.getId());
            } else {
                logger.info(MessageFormat.format("Could not create classification for {0} : {1}", modelClass.getSimpleName(), modelId));
            }
        }
    }

    public void updateModelClass(Classification classification) {
        Object o = getModel(classification);
        if(o instanceof ProductInformationCarrier){
            ProductInformationCarrier pic = (ProductInformationCarrier) o;
            pic.setName(classification.getName());
            pic.setProductId(classification.getClassificationId());
            pic.setClassification(classification.getParents() != null && !classification.getParents().isEmpty() ? classification.getParents().get(0) : null);
        }else if(o instanceof ProjectInformationCarrier){
            ProjectInformationCarrier pjc = (ProjectInformationCarrier) o;
            pjc.setName(classification.getName());
            pjc.setSymbol(classification.getClassificationId());
            pjc.setClassification(classification.getParents() != null && !classification.getParents().isEmpty() ? classification.getParents().get(0) : null);
        }else if(o instanceof QuotationInformationCarrier){
            QuotationInformationCarrier qic = (QuotationInformationCarrier) o;
            qic.setName(classification.getName());
            qic.setSymbol(classification.getClassificationId());
            qic.setClassification(classification.getParents() != null && !classification.getParents().isEmpty() ? classification.getParents().get(0) : null);
        }
        mongoTemplate.save(o);
    }

    public Object getModel(Classification classification) {
        try {
            Class modelClass = Class.forName(classification.getModelClazz());
            Object o = mongoTemplate.findOne(Query.query(Criteria.where("id").is(classification.getModelId())), modelClass);
            return o;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private boolean modelClassificationExists(Classification modelClassification) {
        return modelClassification != null && modelClassification.getId() != null;
    }

    private Classification getExistingClassification(String modelId, Class modelClass) {
        if (modelId != null && modelClass != null) {
            Classification classification = mongoTemplate.findOne(Query.query(Criteria.where("modelId").is(modelId).and("modelClazz").is(modelClass.getName())), Classification.class);
            return classification;
        }
        return null;
    }
}
