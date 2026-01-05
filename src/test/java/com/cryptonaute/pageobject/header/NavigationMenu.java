package com.cryptonaute.pageobject.header;

import framework.elements.Label;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;

public class NavigationMenu {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final Label navigationElements = new Label(By.xpath("//div[@class='header-container']"), "Navigation menu elements");

    @Step("Navigating 'Home' page via clicking CC logo...")
    public void veryfyElementsInMenu() {
        logger.info("Verify navigation menu");
        navigationElements.isDisplayed();
    }
}
