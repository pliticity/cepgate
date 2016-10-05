package pl.iticity.dbfds.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.project.ListPJCMixin;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.service.project.PJCService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectListController extends BaseController {

    @Autowired
    private PJCService pjcService;

    @RequestMapping(value = "/query", params = {"my"}, method = RequestMethod.GET)
    public String getMyProjectList() {
        List<ProjectInformationCarrier> list = pjcService.findByDomainAndPrincipal(PrincipalUtils.getCurrentDomain(), PrincipalUtils.getCurrentPrincipal());
        return convertToString(ProjectInformationCarrier.class, ListPJCMixin.class, list);
    }

    @RequestMapping(value = "/query", params = {"all"}, method = RequestMethod.GET)
    public String getAllProjectList() {
        List<ProjectInformationCarrier> list = pjcService.findByDomain(PrincipalUtils.getCurrentDomain());
        return convertToString(ProjectInformationCarrier.class, ListPJCMixin.class, list);
    }
}
