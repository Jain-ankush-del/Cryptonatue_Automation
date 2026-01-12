package framework.browser;

import framework.appium.AppiumDeviceFactory;
import framework.driversettings.PropertiesHandler;
import framework.elements.js.JavaScript;
import framework.logger.Logger;
import framework.utils.VarargsUtils;
import groovy.util.MapEntry;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.naming.NamingException;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static framework.utils.WebElementUtils.checkForParasiticElementsAndHide;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.*;

public final class Browser {

    public static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static final ThreadLocal<Browser> instance = new ThreadLocal<>();

    public static final String SCROLL_TOP = "top";
    public static final String SCROLL_BOTTOM = "bottom";

    private static final String PROPERTIES_FILE = "config.properties";
    private static final String BROWSER_KEY = "browser";
    private static final String PLATFORM_KEY = "platform";
    private static final String WINDOW_SIZE_KEY = "window.size";
    private static final String DOWNLOAD_TIMEOUT = "timeoutForDownload";
    private static final String DEFAULT_TIMEOUT = "timeoutForExplicitWait";
    private static final String GET_REQUEST_TIMEOUT_SHORT = "timeoutForGetRequestShort";
    private static final String GET_REQUEST_TIMEOUT_LONG = "timeoutForGetRequestLong";
    private static final String APPIUM_DEVICE = "appium.device";
    private static final String APPIUM_PLATFORM = "appium.platform";
    private static final String APPIUM_DEVICE_OS_VERSION = "appium.device.os.version";

    private static final String DEFAULT_ATTRIBUTE_VALUE_TIMEOUT = "timeoutForAttributeToContains";
    private static final String DEFAULT_PAGE_LOAD_TIMEOUT = "timeoutForPageLoad";
    private static final String DEFAULT_REFRESHED_STALENESS_TIMEOUT = "timeoutForRefreshStaleness";
    private static final String DEFAULT_SCROLL_TIMEOUT = "timeoutForScrollAction";
    private static final String DEFAULT_JS_TIMEOUT = "timeoutForJavaScript";
    private static final String VALID_RESPONSE_CODE = "response.code.200";

    private static String platform;
    private static String windowSize;
    private static String chosenBrowser;
    private static String scrollTimeout;
    private static String pageLoadTimeout;
    private static String downloadTimeout;
    private static String conditionTimeout;
    private static String javaScriptTimeout;
    private static String requestTimeoutShort;
    private static String requestTimeoutLong;
    private static String appiumDevice;
    private static String appiumPlatform;
    private static String appiumDeviceOsVersion;
    private static String validResponseCode;
    private static String attributeValueTimeout;
    private static String refreshedStalenessTimeout;

    private static Map.Entry<String, String> basicAuth = new MapEntry("", "");
    private static Map.Entry<String, String> basicAuthReplacements = new MapEntry("", "");

    public static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final PropertiesHandler configProperty;

    static {
        try {
            configProperty = new PropertiesHandler(PROPERTIES_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Browser() {}

    private static synchronized void initProperties() {
        conditionTimeout = configProperty.getProperty(DEFAULT_TIMEOUT);
        scrollTimeout = configProperty.getProperty(DEFAULT_SCROLL_TIMEOUT);
        requestTimeoutShort = configProperty.getProperty(GET_REQUEST_TIMEOUT_SHORT);
        requestTimeoutLong = configProperty.getProperty(GET_REQUEST_TIMEOUT_LONG);
        refreshedStalenessTimeout = configProperty.getProperty(DEFAULT_REFRESHED_STALENESS_TIMEOUT);
        pageLoadTimeout = configProperty.getProperty(DEFAULT_PAGE_LOAD_TIMEOUT);
        platform = configProperty.getProperty(PLATFORM_KEY);
        chosenBrowser = configProperty.getProperty(BROWSER_KEY);
        windowSize = configProperty.getProperty(WINDOW_SIZE_KEY);
        downloadTimeout = configProperty.getProperty(DOWNLOAD_TIMEOUT);
        javaScriptTimeout = configProperty.getProperty(DEFAULT_JS_TIMEOUT);
        validResponseCode = configProperty.getProperty(VALID_RESPONSE_CODE);
        attributeValueTimeout = configProperty.getProperty(DEFAULT_ATTRIBUTE_VALUE_TIMEOUT);
        appiumDevice = configProperty.getProperty(APPIUM_DEVICE);
        appiumPlatform = configProperty.getProperty(APPIUM_PLATFORM);
        appiumDeviceOsVersion = configProperty.getProperty(APPIUM_DEVICE_OS_VERSION);
    }

    private static synchronized void initDriver() {
        switch (platform) {
            case "desktop":
                try {
                    driver.set(BrowserFactory.setup(chosenBrowser, windowSize));
                } catch (NamingException exc) {
                    logger.fatal(Logger.getLogMessage("locale.browser.name.wrong").concat(" chrome, firefox"));
                }
                logger.info(getLogProperty("locale.browser.configured"));
                break;
            case "mobile":
                try {
                    driver.set(DeviceFactory.setup(chosenBrowser, windowSize));
                } catch (NamingException exc) {
                    logger.fatal(Logger.getLogMessage("locale.browser.name.wrong").concat(" chrome, firefox"));
                }
                logger.info(getLogProperty("locale.browser.configured"));
                break;
            case "appium":
                try {
                    driver.set(AppiumDeviceFactory.setup(appiumPlatform, appiumDevice, appiumDeviceOsVersion));
                } catch (NamingException exc) {
                    logger.fatal(Logger.getLogMessage("locale.appium.platform.wrong").concat(" android, ios"));
                }
                logger.info(getLogProperty("locale.appium.configured"));
                break;
            default:
                logger.fatal("Platform value is incorrect. Acceptable values are: desktop, mobile, appium");
        }
    }

    public static synchronized Browser getInstance() {
        if (instance.get() == null) {
            initProperties();
            initDriver();
            instance.set(new Browser());
            logger.info(String.format(getLogProperty("locale.browser.ready"), chosenBrowser));
        }
        return instance.get();
    }

    public static synchronized void extractBasicAuthCredentialsFromUrl(String url) {
        if (basicAuth != null || !url.contains("@")) return;

        String credentialsPart = substringBetween(url, "//", "@");
        String username = substringBefore(credentialsPart, ":");
        String password = substringAfter(credentialsPart, ":");

        String charsetToReplace = substringBetween(url, "@", ".") + '.';
        String replacement = substringBetween(url, "://", ".") + '.';

        basicAuth = new MapEntry(username, password);
        basicAuthReplacements = new MapEntry(charsetToReplace, replacement);
    }

    public static Map.Entry<String, String> getBasicAuth() {
        return requireNonNull(basicAuth);
    }

    public static Map.Entry<String, String> getBasicAuthReplacements() {
        return basicAuthReplacements;
    }

    public static long getScrollTimeout() { return Long.parseLong(scrollTimeout); }

    public static long getTimeoutForPageLoad() {
        return Long.parseLong(pageLoadTimeout);
    }

    public static long getJavaScriptTimeout() { return Long.parseLong(javaScriptTimeout); }

    public static long getTimeoutForCondition() {
        return Long.parseLong(conditionTimeout);
    }

    public static long getDownloadTimeout() {
        return Long.parseLong(downloadTimeout);
    }

    public static long getAttributeValueTimeout() {
        return Long.parseLong(attributeValueTimeout);
    }

    public static int getRequestTimeoutShort() {
        return Integer.parseInt(requestTimeoutShort);
    }

    public static int getRequestTimeoutLong() {
        return Integer.parseInt(requestTimeoutLong);
    }

    public static long getRefreshedStalenessTimeout() {
        return Long.parseLong(refreshedStalenessTimeout);
    }

    public static String getChosenBrowser() {
        return chosenBrowser;
    }

    public static String getPlatform() {
        return platform;
    }

    public static String getUserAgent() {
        return (String) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_USER_AGENT.getScript());
    }

    public static boolean isMobile() {
        return platform.equals("mobile");
    }

    public static int getValidResponseCode() {
        return Integer.parseInt(validResponseCode);
    }

    private static String getLogProperty(final String key) {
        return Logger.getLogMessage(key);
    }

    public static void setDriver(WebDriver localDriver) {
        driver.set(localDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public void navigate(final String url, boolean... appendNoCacheGetParam) {
        String urlToNavigate = getUrlWithPassedBasicAuth(url);

        //String urlToNavigate = url;
        if (VarargsUtils.isTrue(appendNoCacheGetParam)) {
            urlToNavigate = getUrlWithNoCacheParam(urlToNavigate);
        }

        getDriver().get(urlToNavigate);

        waitForPageToLoad();
    }

    public static String getTitle() {
        return getDriver().getTitle();
    }

    public static String getCurrentUrl(boolean... removeBasicAuthPart) {
        String currentURL = substringBefore(getDriver().getCurrentUrl(), "?");

        if (VarargsUtils.isFalse(removeBasicAuthPart) || !currentURL.contains("@")) {
            return currentURL;
        }

        String domainPart = currentURL.split("@")[1];
        String protocolPart = currentURL.substring(0, currentURL.indexOf("://") + 3);

        return protocolPart + domainPart;
    }

    public static String getConfigProperty(String key) {
        return configProperty.getProperty(key);
    }

        // Fallback to config.properties


    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(getTimeoutForPageLoad()));
        try {
            wait.until((ExpectedCondition<Boolean>) condition -> {
                triggerPageScripts();

                Object result = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.IS_PAGE_LOADED.getScript());

                return result instanceof Boolean && (Boolean) result;
            });
        } catch (Exception e) {
            logger.fatal(Logger.getLogMessage("locale.browser.page.timeout"));
        }
    }

    public void waitForJQueryToLoad() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(getJavaScriptTimeout()));
        try {
            wait.until((ExpectedCondition<Boolean>) condition -> {
                waitUntilJQueryIsReady();
                Object result = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.IS_JQUERY_LOADED.getScript());

                return result instanceof Boolean && (Boolean) result;
            });
        } catch (Exception e) {
            logger.fatal(Logger.getLogMessage("locale.jquery.load.timeout"));
        }
    }

    public void waitUntilJQueryIsReady() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(getJavaScriptTimeout()));
        try {
            wait.until((ExpectedCondition<Boolean>) condition -> {
                Object result = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.IS_JQUERY_READY.getScript());

                return result instanceof Boolean && (Boolean) result;
            });
        } catch (Exception e) {
            logger.fatal(Logger.getLogMessage("locale.jquery.ready.timeout"));
        }
    }

   /* public static void waitForScrollToComplete(String target) {
        AtomicReference<Float> interimScrollPosition = new AtomicReference<>(-1.0f);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(Long.parseLong(scrollTimeout)));
        wait.pollingEvery(Duration.ofMillis(100));

        switch (target) {
            case SCROLL_TOP:
                wait.until((ExpectedCondition<Boolean>) condition -> {
                    checkForParasiticElementsAndHide();
                    return isPageAtTop();
                });
                break;
            case SCROLL_BOTTOM:
                wait.until((ExpectedCondition<Boolean>) condition -> {
                    checkForParasiticElementsAndHide();

                    float currentScrollPosition = getPageYOffset();
                    if (interimScrollPosition.get() == currentScrollPosition) {
                        return true;
                    } else {
                        interimScrollPosition.set(currentScrollPosition);
                    }

                    return isPageAtBottom();
                });
                break;
            default:
                logger.fatal("Unknown scroll target. Acceptable values are: top, bottom");
        }
    }*/

    public static void waitForScrollToComplete(String target) {
        AtomicReference<Float> interimScrollPosition = new AtomicReference<>(-1.0f);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(Long.parseLong(scrollTimeout)));
        wait.pollingEvery(Duration.ofMillis(100));

        switch (target) {
            case SCROLL_TOP:
                wait.until((ExpectedCondition<Boolean>) condition -> {
                    checkForParasiticElementsAndHide();
                    return isPageAtTop();
                });
                break;
            case SCROLL_BOTTOM:
                wait.until((ExpectedCondition<Boolean>) condition -> {
                    checkForParasiticElementsAndHide();

                    float currentScrollPosition = getPageYOffset();
                    if (interimScrollPosition.get() == currentScrollPosition) {
                        return true;
                    } else {
                        interimScrollPosition.set(currentScrollPosition);
                    }

                    return isPageAtBottom();
                });
                break;
            default:
                logger.fatal("Unknown scroll target. Acceptable values are: top, bottom");
        }
    }

    public void moveByOffset(final int xOffset, final int yOffset) {
        Actions actions = new Actions(getDriver());
        actions.moveByOffset(xOffset, yOffset).perform();
    }

    public void refresh() {
        getDriver().navigate().refresh();
        waitForPageToLoad();
    }

    public String getUrlWithPassedBasicAuth(String url) {
        if (getBasicAuthReplacements() == null || url.contains("@")) return url;

        return url.replace(getBasicAuthReplacements().getKey(), getBasicAuthReplacements().getValue());
    }

    public String getUrlWithNoCacheParam(String url) {
        String getParamToAppend = url.contains("?") ? "&no-cache" : "?no-cache";

        return url + getParamToAppend;
    }

    public static void navigateBack() {
       // getDriver().navigate().back();
        try {
            long historyLength = (Long) ((JavascriptExecutor) driver)
                    .executeScript("return window.history.length");

            if (historyLength > 1) {
                getDriver().navigate().back();
            } else {
                System.out.println("No previous page to navigate back");
            }
        } catch (Exception e) {
            System.out.println("Navigate back failed: " + e.getMessage());
        }
    }

    public static List<String> getWindowHandles() {
        return new ArrayList<>(getDriver().getWindowHandles());
    }

    public static int getWindowHandlesAmount() {
        return getWindowHandles().size();
    }

    /**
     * Returns desired part of current URI.
     * @param partToGet acceptable values: scheme, authority, path, query, fragment
     */
    public static String parseUrlAndReturnString(String partToGet) {
        URI currentUri;
        String seekingPart = null;

        try {
            currentUri = new URI(getCurrentUrl());
            switch (partToGet.toLowerCase()) {
                case "scheme":
                    seekingPart = currentUri.getScheme();
                    break;
                case "authority":
                    seekingPart = currentUri.getAuthority();
                    break;
                case "path":
                    seekingPart = currentUri.getPath();
                    break;
                case "query":
                    seekingPart = currentUri.getQuery();
                    break;
                case "fragment":
                    seekingPart = currentUri.getFragment();
                    break;
                default:
                    logger.fatal("Unknown URI part");
            }
        } catch (Exception exc) {
            logger.fatal(exc.getMessage());
        }
        return seekingPart;
    }

    public static void switchToUpperWindow() {
        List<String> windows = getWindowHandles();
        Dimension currentWindowSize = getDriver().manage().window().getSize();

        getDriver().switchTo().window(windows.get(windows.size() - 1));
        getDriver().manage().window().setSize(currentWindowSize);
    }

    public static void closeUpperWindow() {
        logger.info("Closing upper window...");

        List<String> windows = getWindowHandles();
        if (windows.size() == 1) {
            return;
        }
        getDriver().switchTo().window(windows.get(windows.size() - 1)).close();
        getDriver().switchTo().window(windows.get(windows.size() - 2));
    }

    public static void closeAllUpperWindows() {
        logger.info("Closing all upper windows...");

        while (getWindowHandlesAmount() > 1) {
            closeUpperWindow();
        }
    }

    public static void switchRootWindow() {
        List<String> windows = new ArrayList<>(getDriver().getWindowHandles());
        getDriver().switchTo().window(windows.get(0));
    }

    public static void scrollToBottom() {
       /* ((JavascriptExecutor) getDriver()).executeScript(JavaScript.SCROLL_TO_BOTTOM.getScript());

        waitForScrollToComplete(SCROLL_BOTTOM);*/
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));

        long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                // Wait up to 5 seconds for height to change
                long finalLastHeight = lastHeight;
                wait.until(driver -> {
                    long currentHeight = (Long) js.executeScript("return document.body.scrollHeight");
                    return currentHeight > finalLastHeight;
                });
            } catch (TimeoutException e) {
                // No change in height → we're done
                break;
            }

            lastHeight = (Long) js.executeScript("return document.body.scrollHeight");
        }

        // Final scroll to bottom
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }


    public static void scrollToCallLazyLoad() {
        Actions action = new Actions(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(Long.parseLong(scrollTimeout)));
        wait.pollingEvery(Duration.ofMillis(100));

        wait.until((ExpectedCondition<Boolean>) condition -> {
            checkForParasiticElementsAndHide();

            if (!isPageAtBottom()) {
                action.sendKeys(Keys.PAGE_DOWN).perform();
            } else {
                return true;
            }

            return false;
        });
    }

    public static void triggerPageScripts() {
        new Actions(getDriver()).sendKeys(Keys.CONTROL).perform();
    }

    public static void scrollOyBy(int pixels) {
        ((JavascriptExecutor) getDriver()).executeScript(String.format("window.scrollBy(0, %s)", pixels), "");
    }

    public static void scrollToTop() {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.SCROLL_TO_TOP.getScript());
        waitForScrollToComplete(SCROLL_TOP);
    }

    public static void makePageScrollable() {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.MAKE_PAGE_SCROLLABLE.getScript());
    }

    public static boolean isPageAtTop() {
        return getPageYOffset() == 0;
    }

    public static boolean isPageAtBottom() {
        int windowInnerHeight = getWindowInnerHeight();
        int documentHeight = getDocumentHeight();
        int currentPosition = (int) Math.ceil(getPageYOffset() + windowInnerHeight);
        return currentPosition >= documentHeight;
    }

    public static float getPageYOffset() {
        return ((Number) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_PAGE_Y_OFFSET.getScript())).floatValue();
    }

    public static int getDocumentHeight() {
        int documentHeight = 0;
        Object scriptResult = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_DOCUMENT_HEIGHT.getScript());

        if (scriptResult instanceof Number) {
            documentHeight = (int) (long) scriptResult;
        }
        return documentHeight;
    }

    public static int getWindowInnerHeight() {
        int windowInnerHeight = 0;
        Object scriptResult = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_WINDOW_INNER_HEIGHT.getScript());

        if (scriptResult instanceof Number) {
            windowInnerHeight = (int) (long) scriptResult;
        }
        return windowInnerHeight;
    }

    public static void scaleDocument(String scale) {
        ((JavascriptExecutor) getDriver()).executeScript(String.format("document.body.style.transform='scale(%s)'", scale));
    }

    public static List<String> getListOfConsoleLogs() {
        List<String> browserLogs = new ArrayList<>();

        LogEntries logEntries = getDriver().manage().logs().get(LogType.BROWSER);
        for (LogEntry entry: Objects.requireNonNull(logEntries)) {
            browserLogs.add(entry.getMessage());
        }
        return browserLogs;
    }

    public void quit() {
        if (instance.get() != null) {
            try {
                getDriver().quit();
                if (((RemoteWebDriver) getDriver()).getSessionId() != null) {
                    throw new WebDriverException();
                }
                logger.info("");
                logger.info(getLogProperty("locale.browser.driver.quit"));
            } catch (Exception exc) {
                logger.fatal(getLogProperty("locale.browser.driver.quit.fail" + exc.getMessage()));
            } finally {
                instance.remove();
                driver.remove();
                if (driver.get() == null && instance.get() == null) {
                    logger.info("Browser is closed. All instances are removed");
                } else {
                    logger.fatal(getLogProperty("locale.browser.driver.quit.fail"));
                }
            }
        }
    }

}