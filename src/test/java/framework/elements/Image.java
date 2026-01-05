package framework.elements;

import framework.elements.attributes.Attributes;
import framework.elements.js.JavaScript;
import framework.elements.styles.Styles;
import framework.utils.WebElementUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static framework.browser.Browser.*;
import static framework.utils.RestAssuredUtils.getResponseStatusCode;
import static org.apache.commons.lang3.StringUtils.*;

public class Image extends BaseElement {

    private static final String LOCALIZED_IMAGE = "locale.image";
    private static final String DATA_BG = "data-bg";
    private static final String BACKGROUND_IMAGE = Styles.BACKGROUND_IMAGE.getValue();
    private static final String IMAGE_NOT_LOADED = "locale.image.not.loaded";

    private static final String RESPONSE_TIMEOUT = "locale.response.timeout";

    private String invalidImageMessage;

    private int testedImages = 0;

    private Thread verifyImages;
    private Thread verifyBackgroundImages;

    private String imageType = "'" + getElementName() + "'";

    private final String mainThreadName = Thread.currentThread().getName();

    private static final int VALID_RESPONSE_CODE = getValidResponseCode();

    public Image(final By locator, String elementName) {
        super(locator, elementName);
    }

    public void launchThreadToVerifyAllImagesOnPageAreLoaded() {
        logger.info("Checking whether all images on the page are loaded...");

        imageType = "page";
        WebDriver mainThreadDriver = getDriver();

        verifyImages = new Thread(() -> {
            setDriver(mainThreadDriver);
            Thread.currentThread().setName(mainThreadName.concat("~img"));
            loadLazyImages();
            verifyImagesAreLoaded();
            driver.remove();
        });

        logger.info("<VerifyImages> thread on page '".concat(getTitle()).concat("' has started"));
        verifyImages.start();
    }

    public void launchThreadToVerifyAllBackgroundImagesOnPageAreLoaded() {
        logger.info("Checking whether all background images on the page are loaded...");

        imageType = "page";
        WebDriver mainThreadDriver = getDriver();

        verifyBackgroundImages = new Thread(() -> {
            setDriver(mainThreadDriver);
            Thread.currentThread().setName(mainThreadName.concat("~img-bgr"));
            loadLazyImages();
            verifyBackgroundImagesAreLoaded();
            driver.remove();
        });

        logger.info("<VerifyBackgroundImages> thread on page '".concat(getTitle()).concat("' has started"));
        verifyBackgroundImages.start();
    }

    public void loadLazyImages() {
        boolean isBackgroundImg = isBackgroundImage();
        List<WebElement> imagesList = findElementsOrReturnEmptyList();

        if (isBackgroundImg) {
            imagesList.forEach(image -> WebElementUtils.setElementAttribute(image, "style", "background-image: url(\"" + image.getAttribute(DATA_BG) + "\");"));
        } else {
            imagesList.forEach(image -> {
                String srcSet = image.getAttribute(Attributes.SRCSET.getValue());
                String lazySrc = image.getAttribute("data-lazy-src");

                if (isNotEmpty(lazySrc)) {
                    WebElementUtils.setElementAttribute(image, Attributes.SRC.getValue(), substringBefore(lazySrc, ' '));
                } else if (isNotEmpty(srcSet)) {
                    WebElementUtils.setElementAttribute(image, Attributes.SRC.getValue(), substringBefore(srcSet, ' '));
                }
            });
        }
    }

    public void waitForImagesValidationThreadToStop() {
        if (verifyImages == null) {
            logger.info("<VerifyImages> thread was not launched on page '".concat(getTitle()));
            return;
        }

        try {
            verifyImages.join();
            logger.info("<VerifyImages> thread on page '".concat(getTitle()).concat("' is completed"));
        } catch (InterruptedException exc) {
            logger.warn("<VerifyImages> thread is interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void waitForBackgroundImagesValidationThreadToStop() {
        if (verifyBackgroundImages == null) {
            logger.info("<VerifyBackgroundImages> thread was not launched on page '".concat(getTitle()));
            return;
        }

        try {
            verifyBackgroundImages.join();
            logger.info("<VerifyBackgroundImages> thread on page '".concat(getTitle()).concat("' is completed"));
        } catch (InterruptedException exc) {
            logger.warn("<VerifyBackgroundImages> thread is interrupted");
            Thread.currentThread().interrupt();
        }
    }

    @Step("Checking whether {this.imageType} image(-s) is/are loaded...")
    public void verifyImagesAreLoaded() {
        logger.info("Checking whether " + imageType + " image(-s) is/are loaded...");

        int invalidImgCounter = 0;
        List<WebElement> imagesList = findElementsOrReturnEmptyList();

        logger.info("Number of specified images: " + imagesList.size());

        for (WebElement image: imagesList) {

            String srcValue = image.getAttribute(Attributes.SRC.getValue());
            if (srcValue.contains("null")) {
                logger.warn("Blank link on some image");
                invalidImgCounter++;
                continue;
            }

            try {
                invalidImgCounter = !(isImageNaturalWidthPositive(image) && isResponseFromImageCorrect(srcValue)) ?
                        invalidImgCounter + 1 : invalidImgCounter;
                logger.info("Number of tested images: ".concat(String.valueOf(++testedImages)));

            } catch (Exception exc) {
                logger.info("Some lazy loading image did not load (MIME type displayed instead)");
            }
        }

        logger.info("Total number of invalid images: " + invalidImgCounter);
    }

    @Step("Checking whether {this.imageType} background image(-s) is/are loaded...")
    public void verifyBackgroundImagesAreLoaded() {
        logger.info("Checking whether " + imageType + " background image(-s) is/are loaded...");

        int invalidBgrImgCounter = 0;
        List<WebElement> backgroundImagesList = findElementsOrReturnEmptyList();

        logger.info("Number of specified background images: " + backgroundImagesList.size());

        for (WebElement backgroundImage : backgroundImagesList) {

            String urlValue = backgroundImage.getCssValue(BACKGROUND_IMAGE);

            if (urlValue.equals("none")) {
                logger.warn("Blank link on some background image");
                invalidBgrImgCounter++;
                continue;
            }

            String bgrUrl = substringBetween(urlValue, "(\"", "\")");

            try {
                invalidBgrImgCounter = !isResponseFromImageCorrect(bgrUrl) ? invalidBgrImgCounter + 1 : invalidBgrImgCounter;
            } catch (Exception exc) {
                logger.info("Some lazy loading image did not load (MIME type displayed instead)");
            }
        }

        logger.info("Total number of invalid background images: " + invalidBgrImgCounter);
    }

    public boolean isImageLoaded() throws UnsupportedEncodingException {
        int getRequestTimeout = getRequestTimeoutLong();
        boolean isImageNaturalWidthPositive = false;
        String src;

        if (isBackgroundImage()) {
            String cssBgrUrl = getCssValue(BACKGROUND_IMAGE);
            src = cssBgrUrl.equals("none") ? getAttribute(DATA_BG) : substringBetween(cssBgrUrl, "(\"", "\")");
        } else {
            src = getAttribute(Attributes.SRC.getValue());
            isImageNaturalWidthPositive = isImageNaturalWidthPositive(getElement());
        }

        int actualStatusCode = getResponseStatusCode(src, getRequestTimeout);
        boolean isLinkValid = actualStatusCode == VALID_RESPONSE_CODE;

        if (actualStatusCode == 408) {
            invalidImageMessage = formatLog(": " + String.format(getLogProperty(RESPONSE_TIMEOUT), getRequestTimeout));
        } else if (!isLinkValid) {
            invalidImageMessage = formatLog(String.format(getLogProperty(IMAGE_NOT_LOADED), VALID_RESPONSE_CODE, actualStatusCode));
        }

        return isImageNaturalWidthPositive || isLinkValid;
    }

    @Step("Checking whether path to image(-s) contains specific domain(-s)...")
    public void verifyPathToImagesContainsSpecificDomains(String[] domains) {
        logger.info("Checking whether path to image(-s) contains specific domain(-s)...");

        List<String> searchStringsList = new ArrayList<>(Arrays.asList(domains));

        searchStringsList.replaceAll(domain -> '.' + domain);
        searchStringsList.add("i.ytimg.com/vi");
        searchStringsList.add("data:image/svg+xml");

        String[] searchStrings = searchStringsList.toArray(new String[0]);

        boolean isBackgroundImg = isBackgroundImage();
        List<WebElement> imagesList = findElementsOrReturnEmptyList();

        imagesList.forEach(image -> {
            String path;
            boolean isValidPath;

            if (isBackgroundImg) {
                String cssBgrUrl = image.getCssValue(BACKGROUND_IMAGE);
                String bgrUrl = substringBetween(cssBgrUrl, "(\"", "\")");
                path = image.getAttribute(DATA_BG) != null ? image.getAttribute(DATA_BG) : bgrUrl;

                isValidPath = containsAny(path, searchStrings) || containsAny(bgrUrl, searchStrings);
            } else {
                path = image.getAttribute(Attributes.SRC.getValue());
                String dataLazySrc = image.getAttribute("data-lazy-src");
                String srcSet = image.getAttribute(Attributes.SRCSET.getValue());

                isValidPath = containsAny(path, searchStrings) || containsAny(dataLazySrc, searchStrings) || containsAny(srcSet, searchStrings);
            }

            if (!isValidPath) {
                logger.warn("Image path '" + path + "' does not belong to any of the " + Arrays.toString(domains) + " domains");
            }
        });
    }

    private boolean isResponseFromImageCorrect(String url) throws UnsupportedEncodingException {
        int getRequestTimeout = getRequestTimeoutShort();
        int actualStatusCode = getResponseStatusCode(url, getRequestTimeout);

        if (actualStatusCode == VALID_RESPONSE_CODE) return true;

        if (actualStatusCode >= 600 || actualStatusCode == 429) {
            logger.info("Error '".concat(String.valueOf(actualStatusCode)).concat("' when trying to reach the image source link ").concat(url));
            return true;
        } else if (actualStatusCode == 408) {
            logger.info("GET request timeout (" + getRequestTimeout + " ms) when trying to reach the image source link ".concat(url));
            return true;
        }

        logger.warn("Image with url: ".concat(url).
                concat(". Expected status code <".concat(String.valueOf(VALID_RESPONSE_CODE)).
                        concat("> but got <").concat(String.valueOf(actualStatusCode)).concat(">")));
        return false;
    }

    private boolean isImageNaturalWidthPositive(WebElement image) {
        return (Boolean) ((JavascriptExecutor) getDriver()).executeScript(JavaScript.IS_IMAGE_NATURAL_WIDTH_POSITIVE.getScript(), image);
    }

    private boolean isBackgroundImage() {
        try {
            waitForElementToBePresent(0);
            return getAttribute(DATA_BG) != null || !getCssValue(BACKGROUND_IMAGE).equals("none");
        } catch (Exception exc) {
            return false;
        }
    }

    public String getNotLoadedImageMessage() {
        return invalidImageMessage;
    }

    protected String getElementType() {
        return getLogProperty(LOCALIZED_IMAGE);
    }
}