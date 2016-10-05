package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.iticity.dbfds.model.Settings;
import pl.iticity.dbfds.service.common.SettingsService;
import pl.iticity.dbfds.util.PrincipalUtils;

@RestController
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Settings getPrincipalSettings() throws JsonProcessingException {
        return settingsService.findOneByDomainAndPrincipal(PrincipalUtils.getCurrentDomain(), PrincipalUtils.getCurrentPrincipal());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean postSavePrincipalSettings(@RequestBody Settings settings) throws JsonProcessingException {
        return settingsService.updateSettings(settings);
    }

}
