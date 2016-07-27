package pl.iticity.dbfds.service.common.impl;

import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Settings;
import pl.iticity.dbfds.repository.common.SettingsRepository;
import pl.iticity.dbfds.service.AbstractScopedService;
import pl.iticity.dbfds.service.common.SettingsService;
import pl.iticity.dbfds.util.PrincipalUtils;

@Service
public class SettingsServiceImpl extends AbstractScopedService<Settings,String,SettingsRepository> implements SettingsService{
    @Override
    public boolean updateSettings(Settings settings) {
        Settings sets = repo.findOneByDomainAndPrincipalAndRemovedIsFalse(PrincipalUtils.getCurrentDomain(),PrincipalUtils.getCurrentPrincipal());
        if(sets!=null){
            settings.setId(sets.getId());
        }
        settings.setPrincipal(PrincipalUtils.getCurrentPrincipal());
        settings.setDomain(PrincipalUtils.getCurrentDomain());
        repo.save(settings);
        return true;
    }
}
