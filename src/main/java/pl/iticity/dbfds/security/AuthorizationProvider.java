package pl.iticity.dbfds.security;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.util.PrincipalUtils;

public class AuthorizationProvider {

    public static void hasRole(Role role, Domain domain) {
        if (SecurityUtils.getSubject().hasRole(Role.GLOBAL_ADMIN.name())) {
            return;
        } else if (!SecurityUtils.getSubject().hasRole(role.name())) {
            throw new UnauthorizedException();
        }else{
            String domainId = domain.getId();
            String principalDomainId = PrincipalUtils.getCurrentPrincipal().getDomain().getId();
            if(!StringUtils.equals(domainId,principalDomainId)){
                throw new UnauthorizedException();
            }
        }
    }

    public static void isInDomain(Domain domain){
        if(!domain.getId().equals(PrincipalUtils.getCurrentDomain().getId())){
            throw new UnauthorizedException();
        }
    }

}
