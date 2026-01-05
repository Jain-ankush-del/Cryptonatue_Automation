package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.elements.Button;
import framework.elements.Image;
import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static framework.browser.Browser.scrollToBottom;
import static org.testng.Assert.assertTrue;

public class BMPage extends StandardPage {

   private static String BM_PAGE_LINK ="/ethereum-casino/";

    private static final Label BM_SECTION_TITLE = new Label(By.xpath("//h2[@id='lhistoire_du_casino_ethereum']"), "BM Section Title");
    private static final Image BM_CARDS = new Image(By.xpath("//div[@class='toplist-cryptonaute__offers']//div//div//a//img"),"BM cards Displayed or not");
    private static final Label BM_OFFER_TITLE =  new Label(By.xpath("//div[@class='toplist-cryptonaute__offer-title']"),"Offer Title");
    private static final Image BM_STAR_RATING_IMAGE = new Image(By.xpath("//div[@class='toplist-cryptonaute__offer-rating']//div/img"),"Star rating image");
    private final Button CloseTheCross = new Button(By.xpath("//button[normalize-space()='Refuser']"),"Refuse the pop up");
    private final Button SHOW_MORE_button = new Button(By.xpath("//div[@class='toplist-cryptonaute__show-more-btn']"), "Click on show more button");

    @Step("Open BM Page")
    public static String getBmPageLink() {return BM_PAGE_LINK;}


 @Step("Pop up refused")
 public void PopupClosed() {
     CloseTheCross.click();
  //Assert.assertTrue(CopyRightLabel.isDisplayed(), CopyRightLabel.getNotDisplayedMessage());
 }

    @Step("Click on show More button")
    public void ShowMoreBuuton() {
        SHOW_MORE_button.click();
        //Assert.assertTrue(CopyRightLabel.isDisplayed(), CopyRightLabel.getNotDisplayedMessage());
    }



    @Step("Verifying BM Blocks")
    public static void verifyBMBlockIsDisplayed() throws UnsupportedEncodingException {
        //scrollToBottom();

        //assertTrue(BM_SECTION_TITLE.isPresent(), "BM title is present");
        String bmTitleText = BM_SECTION_TITLE.getText();
        System.out.println("BM Section Title = " + bmTitleText);
        assertTrue(BM_SECTION_TITLE.isPresent(), "BM title is present");
        System.out.println(BM_CARDS.getElement().getSize());
        assertTrue(BM_CARDS.isPresent(), "BM cards is present");
        assertTrue(BM_CARDS.isImageLoaded(), "Image is Loaded");
        assertTrue(BM_OFFER_TITLE.isPresent(), "BM offer title is present");
        assertTrue(BM_STAR_RATING_IMAGE.isPresent(), "Star rating is present");

    }




}
