package pl.iticity.dbfds.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DefaultConfig {

    private String dataPath;

    private String smtpFrom;

    private String desktopFileName;

    public String getSmtpFrom() {
        return smtpFrom;
    }

    public void setSmtpFrom(String smtpFrom) {
        this.smtpFrom = smtpFrom;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        System.setProperty("pdfbox.fontcache", dataPath);
        this.dataPath = dataPath;
    }

    public String getDesktopFileName() {
        return desktopFileName;
    }

    public void setDesktopFileName(String desktopFileName) {
        this.desktopFileName = desktopFileName;
    }
}
