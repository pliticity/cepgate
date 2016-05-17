package pl.iticity.dbfds.filter;

import com.google.common.collect.Maps;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dacho on 16.05.2016.
 */
public class HeaderEnricherFilter implements Filter {

    private HashMap<String,String> headers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        headers = Maps.newHashMap();
        headers.put("Access-Control-Allow-Origin","*");
        headers.put("Access-Control-Allow-Headers","X-Requested-With,Set-Cookie,Set-Cookie2,Origin,Accept,Content-Type");
        headers.put("Access-Control-Expose-Headers","X-Requested-With,Set-Cookie,Set-Cookie2,Origin,Accept,Content-Type");
        headers.put("Access-Control-Allow-Credentials","true");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        for(String hName : headers.keySet()){
            res.setHeader(hName,headers.get(hName));
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
