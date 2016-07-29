package pl.iticity.dbfds.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.iticity.dbfds.controller.BaseController;
import pl.iticity.dbfds.model.mixins.project.ListPJCMixin;
import pl.iticity.dbfds.model.project.ProjectInformationCarrier;
import pl.iticity.dbfds.service.project.PJCService;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectListController extends BaseController{

    @Autowired
    private PJCService pjcService;

    @RequestMapping(value = "/query",params = {"my"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String getMyProjectList() {
        List<ProjectInformationCarrier> list = pjcService.findByDomainAndPrincipal(PrincipalUtils.getCurrentDomain(),PrincipalUtils.getCurrentPrincipal());
        return convertToString(ProjectInformationCarrier.class, ListPJCMixin.class,list);
    }

    @RequestMapping(value = "/query",params = {"all"}, method = RequestMethod.GET)
    public
    @ResponseBody
    String getAllProjectList() {
        List<ProjectInformationCarrier> list = pjcService.findByDomain(PrincipalUtils.getCurrentDomain());
        return convertToString(ProjectInformationCarrier.class, ListPJCMixin.class,list);
    }
}
