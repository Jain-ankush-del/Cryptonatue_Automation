package framework.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import javax.naming.NamingException;

import static framework.driversettings.ChromeSettings.returnChromeOptions;
import static framework.driversettings.FirefoxSettings.returnFirefoxOptions;

public class BrowserFactory {

    private BrowserFactory() {}

    public static WebDriver setup(final String browser, final String windowSize) throws NamingException {

        WebDriver driver;

        ChromeOptions optionsChrome = returnChromeOptions();
        FirefoxOptions optionsFirefox = returnFirefoxOptions();

        switch (browser.trim().toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(optionsChrome);
                driver.manage().window().setSize(returnScreenDimension(windowSize));
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(optionsFirefox);
                driver.manage().window().setSize(returnScreenDimension(windowSize));
                break;
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