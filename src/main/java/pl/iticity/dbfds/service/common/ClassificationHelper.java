package pl.iticity.dbfds.service.common;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.common.ClassificationType;
import pl.iticity.dbfds.security.Principal;

import java.text.MessageFormat;

@Component
public class ClassificationHelper {

    private static final Logger logger = Logger.getLogger(ClassificationHelper.class);

    @Autowired
    private ClassificationService classificationService;

    public void createModelClassification(Domain domain, Principal principal, ClassificationType type, String name, String classificationId, String modelId, Class modelClass, Classification modelClassification) {
        if (domain != null && principal != null && classificationId != null && !classificationService.exists(classificationId, null, null)) {
            Classification classification = new Classification();
            if(modelClassification!=null && modelClassification.getId()!=null){
                modelClassification = classificationService.findById(modelClassification.getId());
                if(modelClassification!=null && type.equals(modelClassification.getType())){
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
        }else{
            logger.info(MessageFormat.format("Could not create classification for {0} : {1}",modelClass.getSimpleName(), modelId));
        }
    }

}
