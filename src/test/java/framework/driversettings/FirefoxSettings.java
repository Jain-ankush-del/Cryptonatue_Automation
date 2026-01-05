package framework.driversettings;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxSettings {

    private FirefoxSettings() {}

    public static FirefoxOptions returnFirefoxOptions() {

        FirefoxOptions optionsFirefox = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();

        final String[] arguments = {
                "--disable-extensions",
                "--proxy-server='direct://'",
                "--proxy-bypass-list=*",
                //"--headless",
                "--disable-gpu",
                //"--incognito",
                //"--disable-dev-shm-usage",
                //"--no-sandbox",
                "--disable-browser-side-navigation",
                "--ignore-certificate-errors",
                "--disable-popup-blocking",
                //"--disable-blink-features",
                //"--remote-allow-origins=*"
        };

        optionsFirefox.addArguments(arguments);
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
//       firefoxProfile.setPreference("browser.download.dir", getPathToDownloads());
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, text/plain,application/octet-stream doc xls pdf txt");
        optionsFirefox.setProfile(firefoxProfile);
        return optionsFirefox;
    }
}