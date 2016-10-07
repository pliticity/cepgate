package pl.iticity.dbfds.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import pl.iticity.dbfds.model.document.DocumentActivity;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ClassifiableRepository;
import pl.iticity.dbfds.security.Principal;

import java.util.Date;
import java.util.List;

/**
 * Created by pmajchrz on 4/7/16.
 */
public interface DocumentInfoRepository extends MongoRepository<DocumentInformationCarrier, String>, QueryDslPredicateExecutor<DocumentInformationCarrier>, ClassifiableRepository<DocumentInformationCarrier> {

    public List<DocumentInformationCarrier> findByCreatedByAndRemovedIsFalseOrderByCreationDateAsc(Principal principal);

    public List<DocumentInformationCarrier> findByDomainAndRemovedIsFalseOrderByCreationDateAsc(Domain domain);

    public List<DocumentInformationCarrier> findByFavourites_PrincipalOrderByCreationDateAsc(Principal principal);

    public DocumentInformationCarrier findByFiles_Id(String id);

    public List<DocumentInformationCarrier> findByActivities_PrincipalAndActivities_DateGreaterThanAndActivities_TypeAndRemovedIsFalseOrderByActivities_DateDesc(Principal principal, Date date, DocumentActivity.ActivityType type);

    public List<DocumentInformationCarrier> findByDomainAndRemovedIsFalseAndDocumentNumberLike(Domain domain, String docName);

}
