package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;

import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
public interface DocumentInfoRepository extends MongoRepository<DocumentInfo,String> , QueryDslPredicateExecutor<DocumentInfo> {

    public List<DocumentInfo> findByCreatedBy(Principal principal);

    public List<DocumentInfo> findByDomain(Domain domain);

}
