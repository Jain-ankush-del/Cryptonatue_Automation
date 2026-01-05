package framework.elements;

import framework.elements.attributes.Attributes;
import org.openqa.selenium.By;

import java.util.Arrays;

public class TextBox extends BaseElement {

    private static final String LOCALIZED_TEXTBOX = "locale.text.box";
    private static final String LOCALIZED_TYPING = "locale.text.typing";
    private static final String LOCALIZED_TYPING_CREDS = "locale.text.creds";

    public TextBox(final By locator, String elementName) {
        super(locator, elementName);
    }

    public void sendKeys(CharSequence... keys) {
        assertElementIsVisible();
        scrollToElement();
        logger.info(String.format(getLogProperty(LOCALIZED_TYPING), Arrays.toString(keys), getElementName()));
        clear();
        getElement().sendKeys(keys);
    }

    public void sendKeysAndSubmit(CharSequence... keys) {
        sendKeys(keys);
        getElement().submit();
        browser.waitForPageToLoad();
    }

    public void sendCreds(CharSequence... keys) {
        assertElementIsVisible();
        scrollToElement();
        logger.info(String.format(getLogProperty(LOCALIZED_TYPING_CREDS), getElementName()));
        clear();
        getElement().sendKeys(keys);
    }

    public String getValue() {
        assertElementIsVisible();
        scrollToElementRaw();
        return getElement().getAttribute(Attributes.VALUE.getValue());
    }

    public void clear() {
        assertElementIsVisible();
        scrollToElementRaw();
        getElement().clear();
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_TEXTBOX);
    }
}