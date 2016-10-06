package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.common.Bond;
import pl.iticity.dbfds.model.common.ObjectType;
import pl.iticity.dbfds.repository.ScopedRepository;

import java.util.List;

public interface BondRepository extends ScopedRepository<Bond,String> {

    public List<Bond> findByFirstTypeAndFirstId(ObjectType firstType,String firstId);

    public List<Bond> findBySecondTypeAndSecondId(ObjectType secondType,String secondId);
}
