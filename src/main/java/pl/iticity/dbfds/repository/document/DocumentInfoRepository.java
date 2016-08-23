package pl.iticity.dbfds.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import pl.iticity.dbfds.model.DocumentActivity;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.security.Principal;

import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
public interface DocumentInfoRepository extends MongoRepository<DocumentInfo, String>, QueryDslPredicateExecutor<DocumentInfo>, ClassifiableRepository<DocumentInfo> {

    public List<DocumentInfo> findByCreatedByAndRemovedIsFalseOrderByCreationDateAsc(Principal principal);

    public List<DocumentInfo> findByDomainAndRemovedIsFalseOrderByCreationDateAsc(Domain domain);

    public List<DocumentInfo> findByFavourites_PrincipalOrderByCreationDateAsc(Principal principal);

    public DocumentInfo findByFiles_Id(String id);

    public List<DocumentInfo> findByActivities_PrincipalAndActivities_DateGreaterThanAndActivities_TypeAndRemovedIsFalseOrderByActivities_DateDesc(Principal principal, Date date, DocumentActivity.ActivityType type);

    public List<DocumentInfo> findByDomainAndRemovedIsFalseAndDocumentNumberLike(Domain domain, String docName);

}
