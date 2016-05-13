package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.File;
import pl.iticity.dbfds.model.FileInfo;

public interface FileRepository extends MongoRepository<FileInfo, String> {

    public FileInfo findBySymbol(String symbol);

}
