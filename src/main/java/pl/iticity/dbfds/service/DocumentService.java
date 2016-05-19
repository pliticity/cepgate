package pl.iticity.dbfds.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Service
public class DocumentService extends AbstractService<DocumentInfo,DocumentInfoRepository>{

    public DocumentInfo createNewDocumentInfo(){
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setMasterDocumentNumber(getNextMasterDocumentNumber());
        documentInfo.setDocumentNumber(String.valueOf(documentInfo.getMasterDocumentNumber()));
        return documentInfo;
    }

    public Long getNextMasterDocumentNumber(){
        List<DocumentInfo> docs = repo.findByDomain(PrincipalUtils.getCurrentDomain());
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
