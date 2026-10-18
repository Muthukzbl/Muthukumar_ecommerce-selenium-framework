package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader centralizes reading properties from configuration files.
 * It loads config.properties and testdata.properties from the classpath.
 * It also supports System Property overrides (e.g., mvn test -Dbrowser=chrome).
 */
public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        loadProperties("config.properties");
        loadProperties("testdata.properties");
    }

    private static void loadProperties(String fileName) {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("Configuration file not found in classpath: " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Failed to load configuration file: " + fileName + ". Reason: " + e.getMessage());
        }
    }

    /**
     * Retrieves the property value for a given key.
     * System properties take precedence over properties files, allowing CLI overrides.
     *
     * @param key the property key to look up
     * @return the property value, or null if not found
     */
    public static String getProperty(String key) {
        // Priority 1: Check System property (command-line override)
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        // Priority 2: Check properties file
        String fileValue = properties.getProperty(key);
        return fileValue != null ? fileValue.trim() : null;
    }

    /**
     * Retrieves an integer property value (e.g., timeouts).
     *
     * @param key the property key
     * @param defaultValue default value to return if key is missing or invalid
     * @return integer property value
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid integer format for key: " + key + ". Using default: " + defaultValue);
            }
        }
        return defaultValue;
    }
}

