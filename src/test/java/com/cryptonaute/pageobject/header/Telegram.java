package com.cryptonaute.pageobject.header;

import framework.elements.Button;
import framework.elements.Label;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static framework.browser.Browser.*;
import static org.testng.Assert.assertTrue;

public class Telegram {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Label TG_LABEL = new Label(By.cssSelector("a[href*='t.me/cryptonautefr']"), "Telegram Link");
    private static final Button TG_LINK = new Button(By.xpath("//div[@class='header-telegram-outer']//a//*[name()='svg']"), "Telegram button");

    @Step("Telegram button has proper naming...")
    public static void checkTelegramButtonName() throws UnsupportedEncodingException {
       /* logger.info("Checking that button Telegram has proper name...");
        assertTrue(TG_LABEL.getText().equals("t.me/cryptonautefr"), "Telegram Link is present");
        logger.info("Telegram button has proper name");*/
        logger.info("Checking Telegram button link...");

        String href = TG_LABEL.getAttribute("href");

        assertTrue(
                href.contains("t.me/cryptonautefr"),
                "Telegram link is incorrect or missing"
        );

        logger.info("Telegram button has proper link");
    }


    @Step("Click Telegram button...")
    public static void clickTelegramButton() throws UnsupportedEncodingException {
        logger.info("Click Telegram button...");
        TG_LINK.openInNewTabByDataLink();
    }

    @Step("Check that Join TG is opened...")
    public static void isTelegramChannelOpened() {
        logger.info("Check Telegram Link...");
        String telegramLink = "https://t.me/cryptonautefr";
        String openedPageUrl = getCurrentUrl();
        assertTrue(openedPageUrl.equals(telegramLink), "Telegram CTA leads to incorrect page");
        logger.info("Telegram has correct link...");
        closeUpperWindow();
        scrollToTop();
    }
}
