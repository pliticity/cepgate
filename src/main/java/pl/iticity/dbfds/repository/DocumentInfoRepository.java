package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Principal;

import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
public interface DocumentInfoRepository extends MongoRepository<DocumentInfo,String>  {

public List<DocumentInfo> findByCreatedByAndLastActivity_dateAfter(Principal principal, Date date);

}
