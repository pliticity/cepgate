package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.security.Principal;

/**
 * Created by pmajchrz on 3/25/16.
 */
public interface PrincipalRepository extends MongoRepository<Principal,String>{

    public Principal findByEmail(String email);

    public Long countByEmail(String email);

}
