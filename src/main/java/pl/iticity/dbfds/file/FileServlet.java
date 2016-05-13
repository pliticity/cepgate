package pl.iticity.dbfds.file;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.service.FileService;

import javax.servlet.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pmajchrz on 4/6/16.
 */
public class FileServlet implements Servlet {

    private static final String BASE_PATH = "/file/";

    private static final Logger logger = Logger.getLogger(FileServlet.class.getName());

    private String dataDir = "/";

    private ServletConfig servletConfig;

    private FileService fileService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig=servletConfig;
        dataDir = FileService.DATA_DIR;
        fileService = getApplicationContext().getBean(FileService.class);
    }

    private ApplicationContext getApplicationContext(){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletConfig.getServletContext());
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        String symbol = getRequestedSymbol(servletRequest);
        FileInfo fileInfo = fileService.findBySymbol(symbol);
        if (fileInfo != null) {
            String filePath = FileService.DATA_DIR + fileInfo.getPath() + fileInfo.getSymbol();
            FileInputStream fis = new FileInputStream(new File(filePath));
            servletResponse.setContentType(fileInfo.getType());
            write(servletResponse,fis);
        }else{
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO,MessageFormat.format("Content for symbol '{0}' does not exist", fileInfo.getSymbol()));
            }
        }
    }

    private void write(ServletResponse servletResponse, FileInputStream fis) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        OutputStream outputStream = servletResponse.getOutputStream();
        while ((bytesRead = fis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    private String getRequestedSymbol(ServletRequest servletRequest) {
        try {
            return ((ShiroHttpServletRequest) servletRequest).getRequestURI().split("/")[2];
        } catch (IndexOutOfBoundsException e) {
                logger.throwing(FileServlet.class.getName(), "getRequestedSymbol", e);
        }
        return null;
    }

    public static String getSymbolPath(String symbol){
        return MessageFormat.format(BASE_PATH+"{0}",symbol);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
