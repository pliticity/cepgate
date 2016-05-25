package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.DocumentActivity;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.security.Principal;

import java.util.Date;
import java.util.List;

public interface DocumentActivityRepository extends MongoRepository<DocumentActivity,String> {

    public List<DocumentActivity> findByPrincipalAndTypeAndDateGreaterThanOrderByDateAsc(Principal principal, DocumentActivity.ActivityType type, Date date);

    public DocumentActivity findByPrincipalAndType(Principal principal, DocumentActivity.ActivityType type);

    public DocumentActivity findByDocumentInfoAndPrincipalAndType(DocumentInfo info, Principal principal, DocumentActivity.ActivityType type);

}
