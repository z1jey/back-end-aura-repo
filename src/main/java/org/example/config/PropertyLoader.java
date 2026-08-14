package org.example.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader {
    private static final Logger LOGGER = Logger.getLogger(PropertyLoader.class.getName());
    private static final String PROPERTIES_FILE = "application.properties";

    private static volatile PropertyLoader instance;

    private final Properties properties;

    private PropertyLoader() {
        properties = new Properties();
        loadProperties();
    }

    public static PropertyLoader getInstance() {
        if (instance == null) {
            synchronized (PropertyLoader.class) {
                if (instance == null) {
                    instance = new PropertyLoader();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) {
                throw new RuntimeException(
                        "Configuration file '" + PROPERTIES_FILE + "' not found on the classpath.\n" +
                                "Copy 'application.properties.example' to " +
                                "'src/main/resources/application.properties' and fill in your credentials."
                );
            }
            properties.load(in);
            LOGGER.info("Application properties loaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read '" + PROPERTIES_FILE + "': " + e.getMessage(), e);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException(
                    "Required property '" + key + "' not found in " + PROPERTIES_FILE
            );
        }
        return value.trim();
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : defaultValue;
    }
}
