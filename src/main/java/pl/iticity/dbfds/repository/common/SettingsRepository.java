package pl.iticity.dbfds.repository.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.iticity.dbfds.model.Settings;
import pl.iticity.dbfds.repository.ScopedRepository;

public interface SettingsRepository extends ScopedRepository<Settings,String> {

}
