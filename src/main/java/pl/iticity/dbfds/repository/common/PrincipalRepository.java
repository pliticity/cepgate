package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.security.Principal;

import java.util.List;

/**
 * Created by pmajchrz on 3/25/16.
 */
public interface PrincipalRepository extends MongoRepository<Principal,String>{

    public Principal findByEmail(String email);

    public Principal findByEmailAndActiveIsTrue(String email);

    public List<Principal> findByDomain(Domain domain);

    public Principal findByDomainAndAcronym(Domain domain, String acronym);
}
