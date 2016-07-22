package pl.iticity.dbfds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Settings;

public interface SettingsRepository extends MongoRepository<Settings,String>, ScopedRepository<Settings> {

}
