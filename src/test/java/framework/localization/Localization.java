package framework.localization;

import framework.driversettings.PropertiesHandler;

import java.io.IOException;

public class Localization {

    private static PropertiesHandler property;

    public static PropertiesHandler setupLoggerLocale(String language)  throws IOException {
        String convertString = language.toLowerCase();

        property = new PropertiesHandler(String.format("logger/log_english.properties", convertString));
        return property;
    }

    public String getLocalizedElementProperty(String string) {
        String betaString = string.toLowerCase();
        return property.getProperty(betaString);
    }
}