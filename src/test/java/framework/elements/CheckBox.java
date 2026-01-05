package framework.elements;

import org.openqa.selenium.By;

public class CheckBox extends BaseElement {

    private static final String LOCALIZED_CHECKBOX = "locale.checkbox";

    public CheckBox(final By locator, String elementName) {
        super(locator, elementName);
    }

    public boolean isSelected(){
        assertElementIsVisible();
        return this.getElement().isSelected();
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_CHECKBOX);
    }
}