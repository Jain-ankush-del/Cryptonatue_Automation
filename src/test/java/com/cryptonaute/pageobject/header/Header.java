package com.cryptonaute.pageobject.header;

import framework.elements.Button;
import framework.elements.Label;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static framework.browser.Browser.scrollToBottom;

public class Header {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Logo logo = new Logo();
    NavigationMenu navigationMenu = new NavigationMenu();
    Telegram  telegram = new Telegram();

    //private final Button handelBtnHeader = new Button(By.xpath("//div[@class=\"handel-button\"]"), "Jetzt handeln button");
    private final Label header = new Label(By.xpath("//header[@class='header']/div[1]"), "Header");

    @Step("Verifying Header is present")
    public void verifyHeaderExist(){
        header.assertElementIsPresent();
    }

    @Step("Navigating 'Home' page via clicking CN logo...")
    public void navigateHomeThroughLogo() {
        logger.info("Navigating 'Home' page via clicking CN logo...");
        logo.navigateHomeThroughLogo();
    }

    @Step("Verifying Header is present")
    public void verifyHeaderExistAfterScrolling(){
        scrollToBottom();
        header.assertElementIsPresent();
    }

    @Step("Open telegram from header")
    public void openTelegramFromHeader() throws UnsupportedEncodingException {
        telegram.clickTelegramButton();
    }

    @Step("Check telegram from header")
    public void checkTelegramFromHeader() throws UnsupportedEncodingException {
        telegram.checkTelegramButtonName();
    }

   // @Step("Verifying Jetzt handeln button in header")
    /*public void verifyHandelButton(){
        handelBtnHeader.isDisplayed();
    }*/

    public void checkMenu(){
        navigationMenu.veryfyElementsInMenu();
    }
}
