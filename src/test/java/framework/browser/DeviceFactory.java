package framework.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import javax.naming.NamingException;
import java.util.HashMap;

import static framework.browser.Browser.getConfigProperty;
import static framework.driversettings.ChromeSettings.returnChromeOptions;
import static framework.driversettings.FirefoxSettings.returnFirefoxOptions;

public class DeviceFactory {

    private static final String USER_AGENT = "user.agent.mobile";

    private DeviceFactory() {}

    public static WebDriver setup(final String browser, final String screenSize) throws NamingException {

        WebDriver driver;

        var browserPrefs = new HashMap<>();
        var deviceMetrics = new HashMap<>();

        ChromeOptions optionsChrome = returnChromeOptions();
        FirefoxOptions optionsFirefox = returnFirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();

        int width = Integer.parseInt(StringUtils.substringBefore(screenSize, 'x'));
        int height = Integer.parseInt(StringUtils.substringAfter(screenSize, 'x'));

        switch (browser.trim().toLowerCase()) {
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                deviceMetrics.put("width", width);
                deviceMetrics.put("height", height);
                deviceMetrics.put("pixelRatio", 3.0);
                browserPrefs.put("deviceMetrics", deviceMetrics);
                optionsChrome.setExperimentalOption("mobileEmulation", browserPrefs);
                optionsChrome.addArguments("--user-agent=".concat(getConfigProperty(USER_AGENT)));
                driver = new ChromeDriver(optionsChrome);
                //driver.manage().window().maximize();
                break;
            }
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                optionsFirefox.addPreference("general.useragent.override", getConfigProperty(USER_AGENT));
                optionsFirefox.setProfile(firefoxProfile);
                driver = new FirefoxDriver(optionsFirefox);
                driver.manage().window().setSize(returnScreenDimension(screenSize));
                break;
            }
            default:
                throw new NamingException();
        }
        return driver;
    }

    private static Dimension returnScreenDimension(String size) {
        String[] screenResolution = size.split("x");
        int screenWidth = Integer.parseInt(screenResolution[0]);
        int screenHeight = Integer.parseInt(screenResolution[1]);
        return new Dimension(screenWidth, screenHeight);
    }
}