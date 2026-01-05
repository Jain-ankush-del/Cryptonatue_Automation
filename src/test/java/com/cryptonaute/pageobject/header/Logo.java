package com.cryptonaute.pageobject.header;

import framework.elements.Label;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.IOException;

public class Logo {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final Label lblCNLogo = new Label(By.xpath("//a[@class='header-logo-link']//img"), "Cryptonatue Logo");

    @Step("Navigating 'Home' page via clicking CN logo...")
    public void navigateHomeThroughLogo(){
        logger.info("Navigating 'Home' page via clicking CN logo...");
        lblCNLogo.click();
    }
}
