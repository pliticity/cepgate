package pl.iticity.dbfds.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DefaultConfig {

    public static final String DATA_PATH = "dataPath";

    private Properties properties;

    public DefaultConfig(String configPath) throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(configPath));
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }

}
