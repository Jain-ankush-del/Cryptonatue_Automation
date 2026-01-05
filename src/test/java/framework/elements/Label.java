package framework.elements;

import org.openqa.selenium.By;

public class Label extends BaseElement {

    private static final String LOCALIZED_LABEL = "locale.label";

    public Label(final By locator, String elementName) {
        super(locator, elementName);
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_LABEL);
    }
}