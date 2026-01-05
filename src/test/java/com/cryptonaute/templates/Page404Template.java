package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.browser.Browser;
import framework.elements.Button;
import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

import java.io.UnsupportedEncodingException;

import static framework.browser.Browser.getTitle;
import static org.testng.Assert.assertTrue;

public class Page404Template extends StandardPage {
    private String PAGE_404_LINK = "/404page/";

    private final Label LABEL_404 = new Label(By.xpath("//div[@class= 'm-container']/main/div[1]"), "Page Title");
    private final Label SECTION_404 = new Label(By.xpath("//div[@class= 'm-container']/main/div[2]"), "404 page description");
    private final Button SEARCHButton = new Button(By.xpath("//input[@value='Search']"), "Search Button");
    private final Label SearchField = new Label(By.xpath("//input[@placeholder='Search …']"), "Search field");

    public Page404Template() {
        super();
    };

    @Step("Open 404 page")
    public void open404Page() {
        browser.navigate(Browser.getCurrentUrl() + get404Link());
    }

    @Step("Get 404 link...")
    public String get404Link() {
        return PAGE_404_LINK;
    }

    @Step("404 is opened...")
    public void verify404IsOpened() {
        String pageTitle = getTitle();
        SoftAssert softAssert = new SoftAssert();
        logger.info("waiting 404 to be opened...");
        assertTrue(pageTitle.contains("Page non trouvée"),"404 isn't opened");
        logger.info("404 opened...");
    };

    @Step("Verify 404 page elements...")
    public void verify404PageElements() throws UnsupportedEncodingException {
        assertTrue(LABEL_404.isDisplayed());
        assertTrue(LABEL_404.getText().contains("Introuvable"));
        //assertTrue(LABEL_404.getText().contains("Upps ... Fehler 404"));
        assertTrue(SECTION_404.getText().contains("Désolé, la page que vous"));

        assertTrue(SEARCHButton.isPresent());

        assertTrue(SearchField.isPresent());
    }
}
