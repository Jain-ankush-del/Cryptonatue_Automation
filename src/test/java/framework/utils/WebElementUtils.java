package framework.utils;

import framework.browser.Browser;
import framework.elements.js.JavaScript;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import static framework.browser.Browser.getConfigProperty;
import static framework.browser.Browser.getDriver;

public class WebElementUtils {

    private static final String PATH_TO_PARASITIC_ELEMENT_SELECTORS = "path.to.parasitic.element.selectors";

    private static List<String> parasiticCssSelectors;

    private WebElementUtils() {}

    static {
        try (FileReader fileReader = new FileReader(getConfigProperty(PATH_TO_PARASITIC_ELEMENT_SELECTORS))) {
            JSONObject cssSelectors = (JSONObject) new JSONParser().parse(fileReader);
            parasiticCssSelectors = (List<String>) cssSelectors.keySet().stream()
                    .map(key -> cssSelectors.get(key))
                    .collect(Collectors.toList());
        } catch (Exception exc) {
            //parasiticCssSelectors = List.of();
        }
    }

    public static void hideElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.HIDE_ELEMENT.getScript(), element);
        Browser.makePageScrollable();
    }

    public static void setElementAttribute(WebElement element, String attribute, String value) {
        ((JavascriptExecutor) getDriver()).executeScript(JavaScript.SET_ELEMENT_ATTRIBUTE.getScript(), element, attribute, value);
    }

    /**
     * @param pseudoElement optional. Allows to get styles from pseudo elements like :before, :after
     */
    public static String getElementCssValue(WebElement element, String style, String... pseudoElement) {
        String pseudo = VarargsUtils.isNotEmpty(pseudoElement) ? pseudoElement[0] : "";

        return (String) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_CSS_VALUE.getScript(), element, pseudo, style);
    }

    public static String getElementInnerHTML(WebElement element) {
        return (String) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_INNER_HTML.getScript(), element);
    }

    public static WebElement getClosestAncestor(WebElement child, String cssSelector) {
        return (WebElement) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_CLOSEST_ANCESTOR.getScript(), child, cssSelector);
    }

    public static List<Number> getElementPosition(WebElement element) {
        return (List<Number>) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENT_POSITION.getScript(), element);
    }

    public static List<WebElement> getElementsFromPoint(Number x, Number y) {
        return (List<WebElement>) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.GET_ELEMENTS_FROM_POINT.getScript(), x, y);
    }

    public static void checkForParasiticElementsAndHide() {
        if (parasiticCssSelectors.isEmpty()) return;

        for (String selector: parasiticCssSelectors) {
            try {
                WebElement parasiticElement = getDriver().findElement(By.cssSelector(selector));
                hideElement(parasiticElement);
            } catch (WebDriverException exc) {
                Browser.makePageScrollable();
            }
        }
    }
}