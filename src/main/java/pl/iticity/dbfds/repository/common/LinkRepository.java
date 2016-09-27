package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.common.Link;

public interface LinkRepository extends MongoRepository<Link,String> {
}
