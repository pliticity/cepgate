package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;

import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Service
public class DocumentService {

    @Autowired
    DocumentInfoRepository documentInfoRepository;

    public Long getNextMasterDocumentNumber(){
        List<DocumentInfo> docs = documentInfoRepository.findAll();
        if(docs==null || docs.isEmpty()){
            return 1l;
        }else{
            return new Long(docs.size()+1);
        }
    }

}
