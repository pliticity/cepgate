package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.Settings;
import pl.iticity.dbfds.service.ScopedService;

public interface SettingsService extends ScopedService<Settings> {

    public boolean updateSettings(Settings settings);

}
