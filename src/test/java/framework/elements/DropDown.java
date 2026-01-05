package framework.elements;

import framework.elements.attributes.Attributes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class DropDown extends BaseElement {

    private static final String LOCALIZED_DROPDOWN = "locale.dropdown";

    public DropDown(final By locator, String elementName) {
        super(locator, elementName);
    }

    public void selectValue(String value) {
        assertElementIsVisible();
        scrollToElement();
        select = new Select(getElement());
        select.selectByValue(value);
    }

    public void selectValue(int index) {
        assertElementIsVisible();
        scrollToElement();
        select = new Select(getElement());
        select.selectByIndex(index);
    }

    public boolean isValueSelected(String value) {
        assertElementIsVisible();
        scrollToElement();
        String option = select.getFirstSelectedOption().getAttribute(Attributes.TEXT.getValue());
        return option.equals(value);
    }

    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        assertElementIsVisible();
        scrollToElement();
        select = new Select(getElement());
        List<WebElement> allOptions = select.getOptions();
        allOptions.forEach(obj -> options.add(obj.getAttribute(Attributes.TEXT.getValue())));
        return options;
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_DROPDOWN);
    }
}