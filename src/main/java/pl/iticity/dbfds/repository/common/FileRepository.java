package pl.iticity.dbfds.repository.common;

import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.document.FileInfo;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface FileRepository extends ScopedRepository<FileInfo,String> {

    public FileInfo findBySymbol(String symbol);

    public List<FileInfo> findByDomain(Domain domain);

}
