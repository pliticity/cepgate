package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.File;

/**
 * Created by pmajchrz on 4/11/16.
 */
public interface FileRepository extends MongoRepository<File,String>{



}
