package pl.iticity.dbfds.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Service
public class DocumentService extends AbstractService<DocumentInfo,DocumentInfoRepository>{

    public Long getNextMasterDocumentNumber(){
        List<DocumentInfo> docs = repo.findAll();
        if(docs==null || docs.isEmpty()){
            return 1l;
        }else{
            return new Long(docs.size()+1);
        }
    }

    public List<DocumentInfo> findByCreatedBy(Principal principal){
        return repo.findByCreatedBy(principal);
    }

    public List<DocumentInfo> findByPredicate(Predicate predicate){
        return Lists.newArrayList(repo.findAll(predicate));
    }
}
