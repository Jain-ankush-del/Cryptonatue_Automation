package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.elements.Button;
import framework.elements.Image;
import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AuthorPage extends StandardPage {

    private static String AUTHOR_PAGE_LINK ="/auteurs/romaric/";

    private static final Label PAGE_AUTHOR_TITLE = new Label(By.cssSelector("div.m-author-about h4"),"Author Header");
    private static final Image AUTHOR_IMAGE = new Image(By.xpath("//img[contains(@class,'avatar-120')]"), "Author image");
    private static final Button AUTHOR_TWITTER = new Button(By.xpath("//div[@class='m-author-socials'][1]/a[1]"),"Author Twitter Button");
    private static final Button AUTHOR_LINKEDIN = new Button(By.xpath("//div[@class='m-author-socials'][1]/a[2]"), "Author Linkedin button ");
    private static final Label AUTHOR_NAME = new Label(By.cssSelector("div.m-author-about h4"),"Author Name");
    private static final Label AUTHOR_DESCRIPTION =  new Label(By.xpath("//div[@class='m-author-about__description']"), "Author Description");
    private static final Button AUTHOR_SHOW_MORE = new Button(By.xpath("//button[@class='show-more-author-button m-button m-button--sec']"), "Author Show More");
    private static final Label  AUTHOR_ROLE = new Label(By.xpath("//p[contains(@class,'m-show--sm')]"),"Author Role");
    private static final Label AUTHOR_ARTICLE_COUNT = new Label(By.xpath("//span[@class='m-author-count m-show--sm']"), "Author article count");

 public AuthorPage(){super();}

    @Step("Open Author page")
    public static String getAuthorPageLink() {return AUTHOR_PAGE_LINK;}

    @Step("Verifying Author Blocks")
    public static void verifyAuthorBlockIsDisplayed() throws UnsupportedEncodingException {

        assertTrue(PAGE_AUTHOR_TITLE.isPresent(), "author title is present");
        assertTrue(AUTHOR_NAME.isPresent(), "author name is present");
        assertTrue(AUTHOR_IMAGE.isImageLoaded(), "Image is Loaded");
        assertTrue(AUTHOR_TWITTER.isPresent(), "Twitter is present");
        assertTrue(AUTHOR_LINKEDIN.isPresent(), "Linkedin is present");
        assertTrue(AUTHOR_DESCRIPTION.isPresent(), "Description is present");
        assertTrue(AUTHOR_SHOW_MORE.isPresent(), "Show more button is present");
        System.out.println("Author role = " + AUTHOR_ROLE.getText());
        assertTrue(AUTHOR_ROLE.isPresent(),"Author role is present");


    }
    @Step("Verifying Authors block data")
    public static void verifyAuthorBlockData(String name) {

        String titleText = PAGE_AUTHOR_TITLE.getText();
        String authorNameText = AUTHOR_NAME.getText();

        System.out.println("PAGE_AUTHOR_TITLE text = " + titleText);
        System.out.println("AUTHOR_NAME text = " + authorNameText);
        System.out.println("Expected author name = " + name);

        assertTrue(titleText.contains("Romaric Saint Aubert"),
                "Author title contains static name");

        assertTrue(titleText.contains(name),
                "Author title contains expected name");

        assertEquals(authorNameText, name,
                "Author name matches expected value");


        /*System.out.println("PAGE_AUTHOR_TITLE text = " + PAGE_AUTHOR_TITLE);
        System.out.println("AUTHOR_NAME text = " + AUTHOR_NAME);
        System.out.println("Expected author name = " + name);

        assertTrue(PAGE_AUTHOR_TITLE.getText().contains("Romaric Saint Aubert"), "author title is present");
        assertTrue(PAGE_AUTHOR_TITLE.getText().contains(name), "author title is present");
        assertEquals(name, AUTHOR_NAME.getText(), "author name is present");
*/
    /*    String expectedName = name.trim();
        String actualTitle = PAGE_AUTHOR_TITLE.getText().trim();
        String actualName = AUTHOR_NAME.getText().trim();

        assertTrue(actualTitle.contains(expectedName),
                "Author title contains expected name. Actual title: " + actualTitle);

        assertTrue(actualName.contains(expectedName),
                "Author name contains expected name. Actual name: " + actualName);
*/
    }
    @Step("Verifying X button")
    public static void verifyXButtonPresentOnAuthorPage() {
     System.out.println(AUTHOR_TWITTER.getText());
    assertTrue(AUTHOR_TWITTER.isPresent(),"Twitter is present");
 }

    @Step("Verifying LinkedIn button")
    public static void verifylinkedInButtonPresentOnAuthorPage() {
        System.out.println(AUTHOR_LINKEDIN.getText());
        assertTrue(AUTHOR_LINKEDIN.isPresent(),"Linkedin is present");
    }

    @Step("Verifying Show more button")
    public static void verifyShowMoreButtonPresentOnAuthorPage() {
        assertTrue(AUTHOR_SHOW_MORE.isPresent(),"Show More is present");
    }

    @Step("Get author article count")
    public static int getAuthorArticleCount() {
        String text = AUTHOR_ARTICLE_COUNT.getText(); // "1931 Articles"
        System.out.println(text);
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    @Step("Verify author article count is greater than zero")
    public static void verifyAuthorArticleCountIsDisplayed() {
        Assert.assertTrue(AUTHOR_ARTICLE_COUNT.isPresent(),
                "Author article count is displayed");

        int count = getAuthorArticleCount();
        System.out.println(count);
        Assert.assertTrue(count > 0,
                "Author article count is greater than 0. Found: " + count);
    }
}
