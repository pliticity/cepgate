package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.MessageFormat;

public abstract class BaseController {

    protected static final Logger logger = Logger.getLogger(BaseController.class);

    protected String convertToString(Class type, Class mixin, Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.addMixIn(type, mixin);
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String message = MessageFormat.format("Could not serialize object of class {0} with {1} mixin", type, mixin);
            logger.error(message, e);
        }
        return StringUtils.EMPTY;
    }

}
