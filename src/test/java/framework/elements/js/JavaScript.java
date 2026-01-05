package framework.elements.js;

//import static framework.utils.FileUtils.readFileAndReturnString;

import static framework.utils.FileUtils.readFileAndReturnString;

public enum JavaScript {

    CHANGE_TARGET_ATTRIBUTE("changeTargetAttribute.js"),
    CLICK_ELEMENT("clickElement.js"),
    GET_CHILDREN("getChildren.js"),
    GET_CLOSEST_ANCESTOR("getClosestAncestor.js"),
    GET_CSS_VALUE("getCssValue.js"),
    GET_DOCUMENT_HEIGHT("getDocumentHeight.js"),
    GET_ELEMENT_BY_XPATH("getElementByXpath.js"),
    GET_ELEMENT_BY_ID("getElementById.js"),
    GET_ELEMENTS_BY_CSS_SELECTOR("getElementByCssSelector.js"),
    GET_ELEMENT_BY_CLASS_NAME("getElementByClassName.js"),
    GET_ELEMENTS_BY_TAG_NAME("getElementsByTagName.js"),
    GET_PARENT_ELEMENT("getParentElement.js"),
    GET_ELEMENT_FROM_POINT("getElementFromPoint.js"),
    GET_ELEMENTS_FROM_POINT("getElementsFromPoint.js"),
    GET_INNER_HTML("getInnerHTML.js"),
    GET_USER_AGENT("getUserAgent.js"),
    IS_ELEMENT_IN_VIEWPORT("isElementInViewport.js"),
    IS_IMAGE_NATURAL_WIDTH_POSITIVE("isImageNaturalWidthPositive.js"),
    IS_JQUERY_LOADED("isJQueryLoaded.js"),
    IS_JQUERY_READY("isJQueryReady.js"),
    IS_PAGE_LOADED("isPageLoaded.js"),
    MAKE_ELEMENT_BORDER_BOLD_AND_RED("makeElementBorderBoldAndRed.js"),
    OPEN_NEW_TAB("openNewTab.js"),
    REMOVE_ELEMENT("removeElement.js"),
    SCROLL_INTO_VIEW("scrollIntoView.js"),
    SCROLL_TO_BOTTOM("scrollToBottom.js"),
    SCROLL_TO_TOP("scrollToTop.js"),
    MAKE_ELEMENT_VISIBLE("makeElementVisible.js"),
    MAKE_PAGE_SCROLLABLE("makePageScrollable.js"),
    HIDE_ELEMENT("hideElement.js"),
    SET_ELEMENT_ATTRIBUTE("setElementAttribute.js"),
    GET_PAGE_Y_OFFSET("getPageYOffset.js"),
    GET_ELEMENT_POSITION_OY("getElementPositionOy.js"),
    GET_ELEMENT_POSITION("getElementPosition.js"),
    GET_WINDOW_INNER_HEIGHT("getWindowInnerHeight.js");

    private final String filename;

    JavaScript(String filename) {
        this.filename = filename;
    }

    public String getScript() {
        return readFileAndReturnString("src//test//resources//js//".concat(filename));
    }
}