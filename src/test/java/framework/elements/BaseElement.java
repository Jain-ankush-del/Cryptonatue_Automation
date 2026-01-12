package framework.elements;

import framework.browser.Browser;
import framework.elements.attributes.Attributes;
import framework.elements.js.JavaScript;
import framework.listeners.AllureStepListener;
import framework.logger.Logger;
import framework.utils.VarargsUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static framework.browser.Browser.*;
import static framework.utils.WebElementUtils.checkForParasiticElementsAndHide;

public abstract class BaseElement {

   protected By locator;
    protected Select select;
    protected WebDriverWait wait;
    protected WebElement element;

    protected static Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static AllureStepListener allure = new AllureStepListener();

    protected Browser browser = Browser.getInstance();

    private final String elementName;

    private static final String CLICK_ELEMENT = "locale.clicking";
    private static final String ELEMENT_IS_ABSENT = "locale.is.absent";
    private static final String ELEMENT_IS_NOT_INTERACTIVE = "locale.not.interactive";
    private static final String ELEMENT_CLICK_INTERCEPTED = "locale.clicking.exception";
    /// /////////
    public void waitForElementToBeClickable(Integer... timeoutMs) {
        if (VarargsUtils.isNotEmpty(timeoutMs)) {
            new WebDriverWait(getDriver(), Duration.ofMillis(timeoutMs[0]))
                    .until(ExpectedConditions.elementToBeClickable(getLocator()));
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(getLocator()));
        }
        element = getDriver().findElement(getLocator());
    }

    /// //////////////////////////
    protected BaseElement(final By locator, final String elementName) {
      //  this.locator = locator;
        //this.elementName = elementName;
        //wait = new WebDriverWait(getDriver(), Duration.ofMillis(getTimeoutForCondition()));
        this.locator = locator;
        this.elementName = elementName;
        this.wait = new WebDriverWait(
                getDriver(),
                Duration.ofMillis(getTimeoutForCondition())
        );
    }

    protected static String getLogProperty(final String key) {
        return Logger.getLogMessage(key);
    }

    protected String formatLog(final String message) {
        return String.format("%1$s '%2$s' %3$s", getElementType(), getElementName(), message);
    }

    protected abstract String getElementType();

    /*public WebElement getElement() {
        assertElementIsPresent();
        return element;
    }*/
    public WebElement getElement() {
        if (element == null) {
            waitForElementToBePresent();
        }
        return element;
    }

    public String getElementName() {
        return elementName;
    }

    public By getLocator() {
        return locator;
    }

    public String getText() {
        assertElementIsPresent();
        //return getElement().getAttribute(Attributes.TEXT.getValue()).trim().replaceAll("  +", " ");


        String text = getElement().getText();
        if (text != null && !text.trim().isEmpty()) {
            return text.trim().replaceAll("  +", " ");
        }

        String value = getElement().getAttribute("value");
        if (value != null && !value.trim().isEmpty()) {
            return value.trim().replaceAll("  +", " ");
        }

        return "";
    }

    public String getAttribute(String attribute) {
        String attributeValue;

        try {
            attributeValue = getElement().getAttribute(attribute);
        } catch (StaleElementReferenceException exc) {
            waitRefreshedStaleness();
            attributeValue = getElement().getAttribute(attribute);
        }
        return attributeValue;
    }

    public String getCssValue(String value) {
        return getElement().getCssValue(value);
    }

    public boolean isDisplayed(boolean... noWait) {
        try {
            //if (VarargsUtils.isTrue(noWait)) waitForElementToBeDisplayed(0);
            if (VarargsUtils.isTrue(noWait)) {
                return getDriver().findElements(getLocator()).size() > 0;
            }
            else waitForElementToBeDisplayed();
        } catch (Exception exc) {
            checkForParasiticElementsAndHide();
            try {
                waitForElementToBeDisplayed(0);
            } catch (Exception repeatedExc) {
                return false;
            }
        }
        return element.isDisplayed();
    }
  /* public boolean isDisplayed(boolean... noWait) {
       try {
           if (VarargsUtils.isTrue(noWait)) {
               List<WebElement> elements = getDriver().findElements(getLocator());
               return !elements.isEmpty() && elements.get(0).isDisplayed();
           }
           waitForElementToBeDisplayed();
           return element.isDisplayed();
       } catch (Exception e) {
           return false;
       }
   }
*/
    public boolean isPresent(boolean... noWait) {
        try {
            if (VarargsUtils.isTrue(noWait)) waitForElementToBePresent(0);
            else waitForElementToBePresent();
        } catch (Exception exc) {
            checkForParasiticElementsAndHide();
            try {
                waitForElementToBePresent(0);
            } catch (Exception repeatedExc) {
                return false;
            }
        }
        return true;
    }

    public void waitForElementToBeDisplayed(Integer... timeoutMs) {
       /* if (VarargsUtils.isNotEmpty(timeoutMs)) {
            WebDriverWait localWait = new WebDriverWait(getDriver(), Duration.ofMillis(timeoutMs[0]));
            localWait.until(ExpectedConditions.visibilityOfElementLocated(getLocator()));
        } else {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator()));
        }
        element = getDriver().findElement(getLocator());
    }*/
        WebDriverWait effectiveWait = VarargsUtils.isNotEmpty(timeoutMs)
                ? new WebDriverWait(getDriver(), Duration.ofMillis(timeoutMs[0]))
                : wait;

        // Step 1: wait for presence
        effectiveWait.until(ExpectedConditions.presenceOfElementLocated(getLocator()));

        // Step 2: get element
        element = getDriver().findElement(getLocator());

        // Step 3: wait for visibility
        effectiveWait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBePresent(Integer... timeoutMs) {
        if (VarargsUtils.isNotEmpty(timeoutMs)) {
            WebDriverWait localWait = new WebDriverWait(getDriver(), Duration.ofMillis(timeoutMs[0]));
            localWait.until(ExpectedConditions.presenceOfElementLocated(getLocator()));
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(getLocator()));
        }
        element = getDriver().findElement(getLocator());
    }

    public boolean isAttributeContainsValue(String attribute, String value) {
        Browser.triggerPageScripts();

        WebDriverWait attributeWait = new WebDriverWait(getDriver(), Duration.ofMillis(getAttributeValueTimeout()));

        try {
            attributeWait.until(ExpectedConditions.attributeContains(getElement(), attribute, value));
        } catch (Exception exc) {
            return false;
        }
        return true;
    }

    public String getNotDisplayedMessage() {
        return formatLog(getLogProperty(ELEMENT_IS_ABSENT));
    }

    public void assertElementIsVisible() {
        Assert.assertTrue(isDisplayed(), formatLog(getLogProperty(ELEMENT_IS_ABSENT)));
    }

    public void assertElementIsPresent() {
        Assert.assertTrue(isPresent(), formatLog(getLogProperty(ELEMENT_IS_ABSENT)));
    }

    public void waitForElementAttributeToContainValue(String attribute, String value) {
        Assert.assertTrue(isAttributeContainsValue(attribute, value), formatLog(getLogProperty(ELEMENT_IS_ABSENT)));
    }

    public void waitRefreshedStaleness() {
        try {
            new WebDriverWait(getDriver(), Duration.ofMillis(getRefreshedStalenessTimeout()))
                    .until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(getElement())));
        } catch (TimeoutException exc) {
            logger.info("Element '" + getElementName() + "' is not stale. Proceed...");
        }
    }

    public boolean isClickable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(getDriver().findElement(getLocator())));
        } catch (TimeoutException exc) {
            return false;
        }
        return true;
    }

    public boolean isInViewport() {
        try {
            waitForElementInViewport();
        } catch (TimeoutException exc) {
            return false;
        }
        return true;
    }

    public boolean isEnabled() {
        assertElementIsVisible();
        return getElement().isEnabled();
    }

    public List<WebElement> findElements(boolean... noWait) {
        if (VarargsUtils.isFalse(noWait)) {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        }
        return getDriver().findElements(locator);
    }

    public List<WebElement> findElementsOrReturnEmptyList(boolean... noWait) {
        List<WebElement> listOfElements;

        try {
            listOfElements = findElements(noWait);
        } catch (Exception exc) {
            return null;
        }

        return listOfElements;
    }

    public int getNumberOfElements(boolean... noWait) {
        List<WebElement> listOfElements = findElementsOrReturnEmptyList(noWait);

        return listOfElements.size();
    }

    public void scrollToElement() {
        scrollToElementRaw();
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.MAKE_ELEMENT_BORDER_BOLD_AND_RED.getScript(), getElement());
    }

    public void scrollToElementRaw() {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.SCROLL_INTO_VIEW.getScript(), getElement());
        waitForElementInViewport();
    }

    public void waitForElementInViewport() {
        AtomicReference<Float> fixedElementOyPosition = new AtomicReference<>(-1.0f);

        WebDriverWait scrollWait = new WebDriverWait(getDriver(), Duration.ofMillis(getScrollTimeout()));
        scrollWait.pollingEvery(Duration.ofMillis(100));

        scrollWait.until((ExpectedCondition<Boolean>) condition -> {
            if (fixedElementOyPosition.get() == getElementYPosition()) {
                return true;
            }
            Object result = ((JavascriptExecutor) getDriver()).executeScript(JavaScript.IS_ELEMENT_IN_VIEWPORT.getScript(), getElement());
            fixedElementOyPosition.set(getElementYPosition());

            return result instanceof Boolean && (Boolean) result;
        });
    }

    public void moveToElement() {
        Actions action = new Actions(getDriver());
        assertElementIsVisible();

        try {
            scrollToElement();
            action.moveToElement(getElement()).perform();
        } catch (WebDriverException exc) {
            checkForParasiticElementsAndHide();
            try {
                scrollToElement();
                action.moveToElement(getElement()).perform();
            } catch (WebDriverException repeatedExc) {
                logger.fatal(String.format(getLogProperty(ELEMENT_IS_NOT_INTERACTIVE), getElementName(), getElementType().toLowerCase()), repeatedExc.getMessage());
            }
        }
    }


    public void click(String... target) {
        moveToElement();

        if (VarargsUtils.isNotEmpty(target)) {
            ((JavascriptExecutor) getDriver()).executeScript(JavaScript.CHANGE_TARGET_ATTRIBUTE.getScript(), getElement(), target[0]);
        }

        logger.info(String.format(getLogProperty(CLICK_ELEMENT), getElementName(), getElementType().toLowerCase()));
        try {
            getElement().click();
        } catch (WebDriverException exc) {
            checkForParasiticElementsAndHide();
            try {
                getElement().click();
            } catch (WebDriverException repeatedExc) {
                logger.fatal(String.format(getLogProperty(ELEMENT_CLICK_INTERCEPTED), getElementName(), getElementType().toLowerCase()), repeatedExc.getMessage());
            }
        }
    }

    public void makeElementVisible() throws NamingException {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.MAKE_ELEMENT_VISIBLE.getScript(), getJavaScriptElement());
    }

    public void hideElement() {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.HIDE_ELEMENT.getScript(), getElement());
        Browser.makePageScrollable();
    }

    public float getElementYPosition() {
        return ((Number) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENT_POSITION_OY.getScript(), getElement())).floatValue();
    }

    public void setElementAttribute(String attribute, String value) {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.SET_ELEMENT_ATTRIBUTE.getScript(), getElement(), attribute, value);
    }

    public void clickAndWait(boolean... appendNoCacheGetParam) throws UnsupportedEncodingException {
        injectBasicAuthToElementHref();
        if (VarargsUtils.isTrue(appendNoCacheGetParam)) {
            injectNoCacheGetParamToElementHref();
        }

        click();
        browser.waitForPageToLoad();
    }

    public void clickAndWaitOnPage() {
        click();
        browser.waitForJQueryToLoad();
    }

    public void clickWithJSAndWait(boolean... appendNoCacheGetParam) throws UnsupportedEncodingException {
        injectBasicAuthToElementHref();
        if (VarargsUtils.isTrue(appendNoCacheGetParam)) {
            injectNoCacheGetParamToElementHref();
        }

        clickWithJS();
        browser.waitForPageToLoad();
    }

    public void clickWithJS(String... target) {
        moveToElement();
        if (VarargsUtils.isNotEmpty(target)) {
            ((JavascriptExecutor) getDriver()).executeScript(JavaScript.CHANGE_TARGET_ATTRIBUTE.getScript(), getElement(), target[0]);
        }
        try {
            ((JavascriptExecutor) getDriver()).executeScript(JavaScript.CLICK_ELEMENT.getScript(), getJavaScriptElement());
        } catch (WebDriverException | NamingException exc) {
            checkForParasiticElementsAndHide();
            try {
                ((JavascriptExecutor) getDriver()).executeScript(JavaScript.CLICK_ELEMENT.getScript(), getJavaScriptElement());
            } catch (WebDriverException | NamingException repeatedExc) {
                logger.fatal(String.format(getLogProperty(ELEMENT_CLICK_INTERCEPTED), getElementName(), getElementType().toLowerCase()), repeatedExc.getMessage());
            }
        }
    }

    public void openInNewTab(boolean... appendNoCacheGetParam) throws UnsupportedEncodingException {
        moveToElement();

        injectBasicAuthToElementHref();
        if (VarargsUtils.isTrue(appendNoCacheGetParam)) {
            injectNoCacheGetParamToElementHref();
        }

        String href = getElement().getAttribute(Attributes.HREF.getValue());

        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.OPEN_NEW_TAB.getScript());
        Browser.switchToUpperWindow();
        browser.navigate(href);
    }

    public void openInNewTabByDataLink(boolean... appendNoCacheGetParam) throws UnsupportedEncodingException {
        moveToElement();

        injectBasicAuthToElementHref();
        if (VarargsUtils.isTrue(appendNoCacheGetParam)) {
            injectNoCacheGetParamToElementHref();
        }

        String href = getElement().getAttribute(Attributes.DATALINK.getValue());

        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.OPEN_NEW_TAB.getScript());
        Browser.switchToUpperWindow();
        browser.navigate(href);
    }

    @SneakyThrows
    protected Object getJavaScriptElement() throws NamingException {
        String strLocator = getLocator().toString();
        String locatorType = StringUtils.substringBetween(strLocator, ".", ":");
        String selector = StringUtils.substringAfter(strLocator, ": ");

        switch (locatorType) {
            case "id":
                return ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENT_BY_ID.getScript(), selector);
            case "className":
                return ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENT_BY_CLASS_NAME.getScript(), selector);
            case "xpath":
                return ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENT_BY_XPATH.getScript(), selector);
            case "tagName":
                return ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENTS_BY_TAG_NAME.getScript(), selector);
            case "cssSelector":
                return ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENTS_BY_CSS_SELECTOR.getScript(), selector);
            default:
                throw new NamingException("Unknown element parameter");
        }
    }

    /**
     * @param pseudoElement optional. Allows to get styles from pseudo elements like :before, :after
     */
    public String getElementCssValue(String style, String... pseudoElement) {
        String pseudo = VarargsUtils.isNotEmpty(pseudoElement) ? pseudoElement[0] : "";

        if (!isPresent()) return "none";

        return (String) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_CSS_VALUE.getScript(), getElement(), pseudo, style);
    }

    protected String getElementPathWithBasicAuth(String elementPath) throws UnsupportedEncodingException {
        if (elementPath.contains("%") && !elementPath.contains("+")) {
            elementPath = URLDecoder.decode(elementPath, StandardCharsets.UTF_8.toString());
        }

        elementPath = browser.getUrlWithPassedBasicAuth(elementPath);

        return elementPath;
    }

    protected void injectBasicAuthToElementHref() throws UnsupportedEncodingException {
        String hrefAttribute = Attributes.HREF.getValue();
        String href = getAttribute(hrefAttribute);

        if (href != null) {
            setElementAttribute(hrefAttribute, getElementPathWithBasicAuth(href));
        }
    }

    protected void injectNoCacheGetParamToElementHref() {
        String hrefAttribute = Attributes.HREF.getValue();
        String href = getAttribute(hrefAttribute);

        if (href != null) {
            setElementAttribute(hrefAttribute, browser.getUrlWithNoCacheParam(href));
        }
    }
    public void waitForTextToBePresent() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(40)).until(driver -> {
            try {
                WebElement el = getElement();
                String txt = el.getText();
                return txt != null && !txt.trim().isEmpty();
            } catch (Exception e) {
                return false;
            }
        });
    }
    public static void waitForCryptonauteHeader() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));

        wait.until(driver -> {
            try {
                Object exists = ((JavascriptExecutor) driver)
                        .executeScript("return document.querySelector('#header-search') !== null;");
                return Boolean.TRUE.equals(exists);
            } catch (Exception e) {
                return false;
            }
        });

    }

}

