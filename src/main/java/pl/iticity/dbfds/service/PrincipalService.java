package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.util.PrincipalUtils;

/**
 * Created by pmajchrz on 4/5/16.
 */
@Service
public class PrincipalService {

    @Autowired
    PrincipalRepository principalRepository;

    public Principal registerPrincipal(Principal p){
        p.setPassword(PrincipalUtils.hashPassword(p.getPassword(),null));
        return principalRepository.save(p);
    }

}
