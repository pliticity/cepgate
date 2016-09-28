package pl.iticity.dbfds.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.util.PrincipalUtils;

@Component
public class DomainHelper {

    @Autowired
    private DomainService domainService;

    public Domain fetchDomain(String domainId){
        Domain domain = null;
        if(StringUtils.isEmpty(domainId)){
            domain = PrincipalUtils.getCurrentDomain();
        }else{
            domain = domainService.findById(domainId);
        }
        if(domain==null){
            throw new IllegalArgumentException();
        }
        return domain;
    }

}
