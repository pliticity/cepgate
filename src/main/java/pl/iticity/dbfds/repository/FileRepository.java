package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.FileInfo;

import java.util.List;

public interface FileRepository extends MongoRepository<FileInfo, String> {

    public FileInfo findBySymbol(String symbol);

    public List<FileInfo> findByDomain(Domain domain);

}
