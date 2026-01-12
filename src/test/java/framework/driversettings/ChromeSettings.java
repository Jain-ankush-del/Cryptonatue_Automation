package framework.driversettings;

import framework.browser.Browser;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static framework.utils.FileUtils.getPathToDownloads;

public class ChromeSettings {

    private ChromeSettings() {
    }

    public static ChromeOptions returnChromeOptions() {

     ChromeOptions optionsChrome = new ChromeOptions();
        var browserPrefs = new HashMap<String, Object>();

        final String[] arguments = {
                "--disable-extensions",
                "--proxy-server='direct://'",
                "--proxy-bypass-list=*",
                "--headless=new",
                "--window-size=1920,1080",
                "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36",
                "--start-maximized",
                "--disable-gpu",
                "--incognito",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-browser-side-navigation",
                "--ignore-certificate-errors",
                "--disable-popup-blocking",
                "--disable-blink-features",
                "--allow-insecure-localhost",
                "--ignore-ssl-errors=yes",
                "--remote-allow-origins=*",
                //"--disable-blink-features=AutomationControlled",
                "--disable-blink-features=AutomationControlled",
                "--disable-notifications",
                "--disable-infobars",
                "--disable-geolocation",
                "--disable-save-password-bubble"
        };

        optionsChrome.addArguments(arguments);
        browserPrefs.put("download.default_directory", getPathToDownloads());
        browserPrefs.put("safebrowsing.enabled", "false");
        browserPrefs.put("profile.default_content_settings.popups", 0);
        browserPrefs.put("disk-cache-size", 0);
        optionsChrome.setExperimentalOption("prefs", browserPrefs);
        optionsChrome.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        //optionsChrome.setPageLoadStrategy(PageLoadStrategy.EAGER);

        return optionsChrome;

    }
    }




