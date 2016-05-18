package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ExceptionHandlingController implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ObjectMapper mapper = new ObjectMapper();
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView(mapper));
        modelAndView.addObject(JsonResponse.fromException(e));
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return modelAndView;
    }

}
