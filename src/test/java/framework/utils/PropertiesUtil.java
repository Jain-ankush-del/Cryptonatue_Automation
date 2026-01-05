package framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesUtil() {
    }

    private static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static String getUserName() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static String getBrowserType() {
        return getProperty("browser");
    }
}
