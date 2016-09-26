package pl.iticity.dbfds.service.common.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.common.DomainRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.common.DomainService;
import pl.iticity.dbfds.service.common.PrincipalService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

@Service
public class DomainServiceImpl extends AbstractService<Domain,String,DomainRepository> implements DomainService {

    @Autowired
    private PrincipalService principalService;

    public Domain findByName(String name){
        return repo.findByName(name);
    }

    @Override
    public Domain patch(Domain domain) {
        if(domain.getId()==null){
            throw new IllegalArgumentException();
        }
        Domain existing = repo.findOne(domain.getId());
        if(existing==null){
            throw new IllegalArgumentException();
        }

        AuthorizationProvider.assertRole(Role.ADMIN,existing);

        String[] fields = {"company","phone","url","country"};

        for(String field : fields){
            try {
                Method setM = Domain.class.getMethod(MessageFormat.format("set{0}", StringUtils.capitalize(field)),String.class);
                Method getM = Domain.class.getMethod(MessageFormat.format("get{0}", StringUtils.capitalize(field)));
                String value = (String) getM.invoke(domain);
                setM.invoke(existing,value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return repo.save(existing);
    }

    @Override
    public Principal changeSU(String domainId, String principalId) {
        if(StringUtils.isEmpty(domainId) || StringUtils.isEmpty(principalId)){
            throw new IllegalArgumentException();
        }
        Domain domain = repo.findOne(domainId);
        Principal su = principalService.findById(principalId);

        if(domain==null || su==null){
            throw new IllegalArgumentException();
        }

        AuthorizationProvider.assertRole(Role.ADMIN,domain);

        if(!su.getDomain().getId().equals(domain.getId())){
            throw new IllegalArgumentException();
        }

        domain.setName(su.getEmail());
        repo.save(domain);
        su.setRole(Role.ADMIN);
        return principalService.save(su);
    }

}
