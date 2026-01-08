package com.cryptonaute.pageobject.footer;

import framework.browser.Browser;
import framework.elements.Button;
import framework.elements.Image;
import framework.elements.Label;
import framework.elements.Link;
import framework.logger.Logger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;

import static framework.browser.Browser.driver;
import static framework.browser.Browser.scrollToBottom;

public class Footer {
    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   private final Image imageCNFooterLogo = new Image(By.xpath("//div[@class='footer-logo-col']"), "Footer Logo");
   private final Label SocailMediaItems = new Label(By.xpath("//div[@class='footer-socials']//div"), "Social Media icon");
   private final Label FooterDescription = new Label(By.xpath("//p[contains(text(),'Le trading est risqué et vous pouvez perdre tout o')]"),"Footer Description");
   //private final Label CopyRightLabel = new Label(By.xpath("//p[contains(text(),'Tous droits réservés – 2017 – 2025 © dev.cryptonau')]"), "Copyright text");
   private final Button btnScolltotop = new Button(By.xpath("//div[@id='to_top_scrollup']//*[local-name()='svg']"),"Scroll to top");
   private final Button CloseTheCross = new Button(By.xpath("//button[normalize-space()='Refuser']"),"Refuse the pop up");
   private final Link  FooterLinks = new Link(By.xpath("//footer[contains(@class, 'footer-info')]//*[@href][not(contains(@class, 'footer-email-link'))]"), "Footer Links");


   @Step("Checking whether footer contains all mandatory elements...")

    public void VerifyFooterHasMandatoryElements() {
       logger.info("Checking whether footer contains all mandatory elements...");
       SoftAssert softAssert = new SoftAssert();

       softAssert.assertTrue(imageCNFooterLogo.isDisplayed(), imageCNFooterLogo.getNotDisplayedMessage());
       softAssert.assertTrue(SocailMediaItems.isDisplayed(), SocailMediaItems.getNotDisplayedMessage());
       softAssert.assertAll();
   }
    @Step("Checking whether all footer links are valid...")
    public void verifyFooterLinksAreValid() {
        logger.info("Checking whether all footer links are valid...");
        FooterLinks.verifyLinksAreValid();
    }

    @Step("Checking whether footer description and copyright text is displayed...")
    public void verifyFooterDescriptionIsDisplayed() {
        logger.info("Checking whether footer description and copyright text is displayed...");
        Assert.assertTrue(FooterDescription.isDisplayed(), FooterDescription.getNotDisplayedMessage());
        CloseTheCross.click();
        //Assert.assertTrue(CopyRightLabel.isDisplayed(), CopyRightLabel.getNotDisplayedMessage());
    }

    @Step("Scrolling to top via 'Scroll to Top' button...")
    public void clickScrollToTopButton() {
        logger.info("Scrolling to top via 'Scroll to Top' button...");
        scrollToBottom();
       CloseTheCross.click();
       btnScolltotop.setElementAttribute("style", "opacity: 1;");
       btnScolltotop.click();

        /*
        Forced Thread.sleep().
        Depending on browser version, there may be delays in the operation of the 'Scroll to Top' button
         */
       try {
            Thread.sleep(4000);
        } catch (InterruptedException exc) {
//            logger.info("Thread " + Thread.currentThread().getName() + " is interrupted", exc.getMessage());
            Thread.currentThread().interrupt();
        }
    }


}
