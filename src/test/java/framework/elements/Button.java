package framework.elements;

import org.openqa.selenium.By;

public class Button extends BaseElement {

    private static final String LOCALIZED_BUTTON = "locale.button";

    public Button(final By locator, String elementName) {
        super(locator, elementName);
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_BUTTON);
    }
}