package pl.iticity.dbfds.validator;

import com.vaadin.data.Validator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.repository.PrincipalRepository;

/**
 * Created by pmajchrz on 4/5/16.
 */
@Component
public class UniquePrincipalEmailValidator implements Validator {

    @Autowired
    PrincipalRepository principalRepository;

    @Override
    public void validate(Object o) throws InvalidValueException {
            String string = (String) o;
            if(!StringUtils.isEmpty(string)){
                if(principalRepository.countByEmail(string)>0){
                    throw new InvalidValueException("Already exists");
                }
            }
    }
}
