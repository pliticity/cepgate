package pl.iticity.dbfds.controller.desktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.iticity.dbfds.service.common.PrincipalService;

@RestController
@RequestMapping(value = "/desktop")
public class DesktopController {

    @Autowired
    private PrincipalService principalService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean registerToken(@RequestBody DesktopTokenDTO dto) {
        return principalService.setDesktopToken(dto.getToken());
    }

}
