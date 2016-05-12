package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

/**
 * Created by pmajchrz on 4/5/16.
 */
@Service
public class PrincipalService extends AbstractService<Principal,PrincipalRepository>{

    public Principal registerPrincipal(Principal p){
        //p.setPassword(PrincipalUtils.hashPassword(p.getPassword(),null));
        return repo.save(p);
    }

    public Principal findByEmail(String email){
        return repo.findByEmail(email);
    }

}
