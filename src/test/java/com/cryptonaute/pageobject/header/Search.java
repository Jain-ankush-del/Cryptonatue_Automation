package com.cryptonaute.pageobject.header;

import framework.elements.Button;
import framework.elements.TextBox;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Search {
    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //private static final Button SEARCH_DROPDOWN_BUTTON = new Button(new By.ByXPath("//img[@alt='Icône de recherche']"), "Search button");
    private static final Button SEARCH_DROPDOWN_BUTTON = new Button(new By.ByXPath("//button[contains(@class,'header-search-button')]"), "Search button");
   // private static final Button SEARCH_DROPDOWN_BUTTON = new Button(new By.ByXPath("//img[@alt='Icône de recherche']"), "Search button");
    private static final TextBox SEARCH_FIELD = new TextBox(new By.ByXPath("//input[@id='keyword']"), "Search Field");
    private static final Button SEARCH_BUTTON = new Button(new By.ByXPath("//button[contains(normalize-space(), 'Search')]"), "Search button");

    private static void openSearch () throws UnsupportedEncodingException {
       SEARCH_DROPDOWN_BUTTON.getElement().click();
        //SEARCH_DROPDOWN_BUTTON.waitForElementToBeDisplayed();
        //SEARCH_DROPDOWN_BUTTON.click();
    }

    private static void typeTextInSearchField (String text) throws UnsupportedEncodingException {
        SEARCH_FIELD.getElement().sendKeys(text);
        SEARCH_FIELD.waitForElementToBeDisplayed();
        //SEARCH_FIELD.getElement().sendKeys(text);
    }

    private static void clickSearch () throws UnsupportedEncodingException {
        SEARCH_BUTTON.getElement().getLocation();
        SEARCH_BUTTON.getElement().click();
        //SEARCH_BUTTON.waitForElementToBeClickable();
        //SEARCH_BUTTON.click();
    }

    @Step("open Search page...")
    public static void useSearchInHeader(String text) throws UnsupportedEncodingException {
        openSearch();
        typeTextInSearchField(text);
        clickSearch();
    }
}
