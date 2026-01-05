package framework.forms;

import framework.browser.Browser;
import framework.elements.Label;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

import java.io.IOException;
import java.util.Date;

import static framework.browser.Browser.getCurrentUrl;
import static framework.browser.Browser.navigateBack;

public class BasePage {

    private static final String PAGE = "locale.page";
    private static final String LOAD_PAGE = "locale.page.load";
    private static final String PAGE_LOADED = "locale.page.loaded";
    private static final String PAGE_CORRECT = "locale.page.correct";
    private static final String PAGE_INCORRECT = "locale.page.incorrect";

    protected static Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Browser browser = Browser.getInstance();

    protected String pageTitle;
    protected String fullPageName;

    private By pageLocator;

    private final long initTime;

    private final Label lblCloudFlareError = new Label(By.xpath("//div[@id = \"cf-error-details\"]//h1"), "CloudFlare error");
    private final Label lblCloudflareChallenge = new Label(By.xpath("//*[contains(text(),'Just a moment')]"),
                    "Cloudflare challenge page");
    private void waitForCloudflareChallengeIfPresent() {
        long start = System.currentTimeMillis();
        long timeout = 30000; // 30 seconds

        if (lblCloudflareChallenge.isDisplayed(false)) {
            logger.info("Cloudflare challenge detected. Waiting...");

            while (lblCloudflareChallenge.isDisplayed(false)) {
                if (System.currentTimeMillis() - start > timeout) {
                    throw new IncorrectPageException("Cloudflare challenge did not disappear in time");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }

            logger.info("Cloudflare challenge passed.");
        }
    }
    public BasePage(final By pageLocator, final String pageTitle) {

        initTime = new Date().getTime();
        browser.waitForPageToLoad();
        waitForCloudflareChallengeIfPresent();
        initPage(pageLocator, pageTitle);
        assertCorrectPage();
    }

    @Step("Opening '{pageTitle}' page...")
    private void initPage(final By pageLocator, final String pageTitle) {
        this.pageLocator = pageLocator;
        this.pageTitle = StringUtils.normalizeSpace(pageTitle);
        fullPageName = String.format(getLogProperty(PAGE), this.pageTitle);
    }

    @Step("Checking whether opened page is correct...")
    private void assertCorrectPage() {
       /* Label uniqueElement = new Label(pageLocator, pageTitle);

        logger.info(String.format(getLogProperty(LOAD_PAGE), pageTitle));

        if (uniqueElement.isPresent()) {
            long timeToOpen = new Date().getTime() - initTime;
            logger.info(getLogProperty(PAGE_LOADED) + String.format(" %s mills", timeToOpen));
            logger.info(String.format(getLogProperty(PAGE_CORRECT), pageTitle));
        } else if (lblCloudFlareError.isDisplayed(true)) {
            String cfError = "CloudFlare error '" + lblCloudFlareError.getText().replace("\n",":").toUpperCase() + "' at the url: " + getCurrentUrl();
            navigateBack();
            logger.error(cfError);
            throw new IncorrectPageException(cfError);
        } else {
            logger.error(getLogProperty(PAGE_INCORRECT));
            throw new IncorrectPageException(getLogProperty(PAGE_INCORRECT));
        }*/
        Label uniqueElement = new Label(pageLocator, pageTitle);

        logger.info(String.format(getLogProperty(LOAD_PAGE), pageTitle));

        long start = System.currentTimeMillis();
        long timeout = 50000; // 20 seconds

        while (!uniqueElement.isPresent()) {

            if (lblCloudFlareError.isDisplayed(false)) {
                String cfError = "CloudFlare error '" +
                        lblCloudFlareError.getText().replace("\n", ":").toUpperCase() +
                        "' at the url: " + getCurrentUrl();
                navigateBack();
                logger.error(cfError);
                throw new IncorrectPageException(cfError);
            }

            if (System.currentTimeMillis() - start > timeout) {
                logger.error(getLogProperty(PAGE_INCORRECT));
                throw new IncorrectPageException(getLogProperty(PAGE_INCORRECT));
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }

        long timeToOpen = System.currentTimeMillis() - initTime;
        logger.info(getLogProperty(PAGE_LOADED) + String.format(" %s ms", timeToOpen));
        logger.info(String.format(getLogProperty(PAGE_CORRECT), pageTitle));
    }

    protected static String getLogProperty(final String key) {
        return Logger.getLogMessage(key);
    }

    public static class IncorrectPageException extends NotFoundException {
        public IncorrectPageException(String message) {
            super(message);
        }
    }
}