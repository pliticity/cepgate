package pl.iticity.dbfds.service;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.ClassificationRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class ClassificationService extends AbstractService<Classification,ClassificationRepository>{

    public List<Classification> findByDomain(Domain domain, boolean onlyActive){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        if(onlyActive){
            return repo.findByDomainAndActiveIsTrueAndRemovedIsFalse(domain);
        }
        return repo.findByDomainAndRemovedIsFalse(domain);
    }

    public List<Classification> findByDomain(Domain domain, boolean onlyActive, String without){
        if(domain==null){
            domain = PrincipalUtils.getCurrentDomain();
        }
        return repo.findByDomainAndIdNotAndRemovedIsFalse(domain,without);
    }

    public List<Classification> addClassification(Classification classification,Domain domain){
        if(repo.findOne(classification.getId())==null){
           classification.setId(null);
        }
        classification.setDomain(domain);
        classification.setActive(true);
        repo.save(classification);
        return findByDomain(domain,false);
    }

    private List<Classification> transform(List<Classification> classifications){
        return Lists.newArrayList(Iterables.transform(classifications, new Function<Classification, Classification>() {
            @Nullable
            @Override
            public Classification apply(@Nullable Classification classification) {
                return repo.findByDomainAndClassificationId(PrincipalUtils.getCurrentDomain(),classification.getClassificationId());
            }
        }));
    }

    public List<Classification> toggleClassification(String id, boolean toggle){
        Classification classification = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,classification.getDomain());
        classification.setActive(toggle);
        repo.save(classification);
        return findByDomain(classification.getDomain(),false);
    }

    public boolean exists(String clId, String id){
        Classification c = repo.findByDomainAndClassificationId(PrincipalUtils.getCurrentDomain(),clId);
        if(c==null){
            return false;
        }
        if(id!=null && id.equals(c.getId())){
            return false;
        }
        return true;
    }

    public List<Classification> deleteClassification(String id){
        Classification classification = repo.findOne(id);
        AuthorizationProvider.hasRole(Role.ADMIN,classification.getDomain());
        classification.setRemoved(true);
        classification.setActive(false);
        repo.save(classification);
        return findByDomain(classification.getDomain(),false);
    }

}
