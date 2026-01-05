package com.cryptonaute.pageobject.header;

import framework.elements.Link;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainMenuDropdown {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Link MAINMENU_WITH_SUBMENU_LINKS = new Link(By.xpath("//ul[@id='menu-navbar']//li/ul/li/a"),"Main menu Links");

    private static void checkThatLinkCanBeOpenedWithoutConsoleErrorsInHeader(Link link) throws UnsupportedEncodingException {
        link.verifyLinksAreValid();
    }

    @Step("Check links...")
    public static void checkHeaderMenuLink() throws UnsupportedEncodingException {
        logger.info("Checking Header menu links...");
        checkThatLinkCanBeOpenedWithoutConsoleErrorsInHeader(MAINMENU_WITH_SUBMENU_LINKS);
    }
}
