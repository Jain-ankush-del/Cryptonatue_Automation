package framework.driversettings;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    private static final Properties property = new Properties();

    public PropertiesHandler(final String resourceName) throws IOException {
        setupFileReader(resourceName);
    }

    @SneakyThrows
    private Properties setupFileReader(final String resourceName) throws IOException {
        try (InputStream data = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
            property.load(data);
        } catch (NullPointerException | IOException exc) {
            throw new IOException(String.format("%s file not found or cannot be read", resourceName), exc);
        }
        return property;
    }

    public String getProperty(final String key) {
        if (key!= null) {
            return property.getProperty(key);
        } else {
            throw new NullPointerException("Browser name is not specified in the config.properties file");
        }
    }
}