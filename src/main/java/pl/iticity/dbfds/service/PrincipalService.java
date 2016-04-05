package pl.iticity.dbfds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;

/**
 * Created by pmajchrz on 4/5/16.
 */
@Service
public class PrincipalService {

    @Autowired
    PrincipalRepository principalRepository;

    public Principal registerPrincipal(Principal p){
        return principalRepository.save(p);
    }

}
