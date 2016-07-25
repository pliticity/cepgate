package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;

public interface DomainRepository extends MongoRepository<Domain,String> {

    public Domain findByName(String name);

}
