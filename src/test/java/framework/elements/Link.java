package framework.elements;

import framework.elements.attributes.Attributes;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static framework.browser.Browser.*;
import static framework.utils.RestAssuredUtils.getResponseStatusCode;

public class Link extends BaseElement {

    private static final String LOCALIZED_LINK = "locale.link";
    private static final String RESPONSE_TIMEOUT = "locale.response.timeout";
    private static final String LINK_INVALID = "locale.link.invalid";

    private String invalidLinkMessage;

    private Thread verifyLinks;

    private int testedLinks = 0;

    private String linkType = "'" + getElementName() + "'";

    private final String mainThreadName = Thread.currentThread().getName();

    private static final int VALID_RESPONSE_CODE = getValidResponseCode();

    public Link(final By locator, String elementName) {
        super(locator, elementName);
    }

    public void launchThreadToVerifyAllLinksOnPageAreLoaded() {
        logger.info("Checking whether all links on the page are valid...");

        linkType = "page";
        WebDriver mainThreadDriver = getDriver();

        verifyLinks = new Thread(() -> {
            setDriver(mainThreadDriver);
            Thread.currentThread().setName(mainThreadName.concat("~link"));
            verifyLinksAreValid();
            driver.remove();
        });

        logger.info("<VerifyLinks> thread on page '".concat(getTitle()).concat("' has started"));
        verifyLinks.start();
    }

    public void waitForLinksValidationThreadToStop() {
        if (verifyLinks == null) {
            logger.info("<VerifyLinks> thread was not launched on page '".concat(getTitle()));
            return;
        }

        try {
            verifyLinks.join();
            logger.info("<VerifyLinks> thread on page '".concat(getTitle()).concat("' is completed"));
        } catch (InterruptedException exc) {
            //logger.warn("<VerifyLinks> thread is interrupted", exc.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Step("Checking whether {this.linkType} links are valid...")
    public void verifyLinksAreValid() {
        logger.info("Checking whether " + linkType + " links are valid...");

        int invalidLinkCounter = 0;
        List<WebElement> linksList = findElementsOrReturnEmptyList();

        logger.info("Number of specified links: " + linksList.size());

        for (WebElement link: linksList) {
            String hrefValue;

            try {
                hrefValue = link.getAttribute(Attributes.HREF.getValue());
            } catch (WebDriverException exc) {
                continue;
            }

            try {
                invalidLinkCounter = !isResponseFromLinkCorrect(hrefValue, link) ? invalidLinkCounter + 1 : invalidLinkCounter;
                logger.info("Number of tested links: ".concat(String.valueOf(++testedLinks)));
            } catch (Exception exc) {
                logger.info("Caught exception while testing link '" + hrefValue + "': " + exc.getMessage());
            }
        }

        logger.info("Total number of invalid links: " + invalidLinkCounter);
    }

    public boolean isLinkValid() throws UnsupportedEncodingException {
        int getRequestTimeout = getRequestTimeoutLong();
        String href = getAttribute(Attributes.HREF.getValue());

        int actualStatusCode = getResponseStatusCode(href, getRequestTimeout);
        boolean isLinkValid = actualStatusCode == VALID_RESPONSE_CODE;

        if (actualStatusCode == 408) {
            invalidLinkMessage = formatLog(": " + String.format(getLogProperty(RESPONSE_TIMEOUT), getRequestTimeout));
        } else if (!isLinkValid) {
            invalidLinkMessage = formatLog(String.format(getLogProperty(LINK_INVALID), VALID_RESPONSE_CODE, actualStatusCode));
        }

        return isLinkValid;
    }

    private boolean isResponseFromLinkCorrect(String url, WebElement element)throws UnsupportedEncodingException {
        int getRequestTimeout = getRequestTimeoutShort();
        int actualStatusCode = getResponseStatusCode(url, getRequestTimeout);
        String elementName = element.getText();

        if (actualStatusCode == VALID_RESPONSE_CODE) return true;

        if (actualStatusCode >= 600 || actualStatusCode == 429) {
            logger.info("Error '".concat(String.valueOf(actualStatusCode)).concat("' when trying to reach the link ").concat(url)
                    .concat(" from element '").concat(elementName).concat("'"));
            return true;
        } else if (actualStatusCode == 408) {
            logger.info("GET request timeout (" + getRequestTimeout + " ms) when trying to reach the link ".concat(url));
            return true;
        }

        logger.warn("Link ".concat(url).concat(" from element '").concat(elementName).concat("'").
                concat(". Expected status code <".concat(String.valueOf(VALID_RESPONSE_CODE)).
                        concat("> but got <").concat(String.valueOf(actualStatusCode)).concat(">")));
        return false;
    }

    public String getInvalidLinkMessage() {
        return invalidLinkMessage;
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_LINK);
    }
}