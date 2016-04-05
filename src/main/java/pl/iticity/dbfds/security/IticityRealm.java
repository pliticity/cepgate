package pl.iticity.dbfds.security;

import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dacho on 23.03.2016.
 */
public class IticityRealm extends AuthorizingRealm {


    @Autowired
    PrincipalRepository principalRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Principal tokenPrincipal = (Principal) authenticationToken.getPrincipal();
        Principal principal =null;
            principal = principalRepository.findByEmail(tokenPrincipal.getEmail());
        if(principal == null){
            unauthenticated();
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, principal.getPassword(), getName());
        if (!getCredentialsMatcher().doCredentialsMatch(authenticationToken, info)) {
            unauthenticated();
        }
        return info;
    }

    private void unauthenticated(){
        throw new AuthenticationException();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }
}
