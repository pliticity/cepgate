package pl.iticity.dbfds.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DefaultConfig {

    private String dataPath;

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
