package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.security.Principal;

import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
public interface DocumentInfoRepository extends MongoRepository<DocumentInfo,String>  {

public List<DocumentInfo> findByCreatedByAndLastActivity_dateAfterOrderByLastActivity_dateDesc(Principal principal, Date date);

   // public List<DocumentInfo> findByCreatedByAndClassification_ClassificationIdLikeOrClassification_NameLikeOrDocumentNumberLikeOrDocumentNameLike(Principal principal,String str,String str2,String str3,String str4);

    public List<DocumentInfo> findByCreatedBy(Principal principal);

}
