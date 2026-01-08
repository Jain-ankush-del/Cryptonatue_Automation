package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.browser.Browser;
import framework.elements.Button;
import framework.elements.Image;
import framework.elements.Label;
import framework.utils.DeviceUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AuthorPageNew extends StandardPage {

    private static String AUTHOR_PAGE_LINK = "/auteurs/romaric/";

    private static Label PAGE_AUTHOR_TITLE_WEB = new Label(By.cssSelector("div.m-author-about h4"), "Author Header");
    //private static  Label PAGE_AUTHOR_TITLE_MOBILE = new Label(By.cssSelector("h4.m-author-about__title"),"Author Header");
    private static  Label PAGE_AUTHOR_TITLE_MOBILE = new Label(By.cssSelector("div.m-author-about h4"),"Author Header");
    private static final Image AUTHOR_IMAGE = new Image(By.xpath("//img[contains(@class,'avatar-120')]"), "Author image");
    private static final Button AUTHOR_TWITTER = new Button(By.xpath("//div[@class='m-author-socials'][1]/a[1]"), "Author Twitter Button");
    private static final Button AUTHOR_LINKEDIN = new Button(By.xpath("//div[@class='m-author-socials'][1]/a[2]"), "Author Linkedin button ");
    private static  Label AUTHOR_NAME_MOBILE = new Label(By.cssSelector("h4.m-author-about__title"),"Author Name");
    private static  Label AUTHOR_NAME_WEB = new Label(By.cssSelector("div.m-author-about h4"), "Author Name");
    private static final Label AUTHOR_DESCRIPTION = new Label(By.xpath("//div[@class='m-author-about__description']"), "Author Description");
    private static final Button AUTHOR_SHOW_MORE = new Button(By.xpath("//button[@class='show-more-author-button m-button m-button--sec']"), "Author Show More");
    private static final Label AUTHOR_ROLE_WEB = new Label(By.xpath("//p[contains(@class,'m-show--sm')]"), "Author Role");
    private static  Label  AUTHOR_ROLE_MOBILE = new Label(By.xpath("//p[@class='m-author-expertise m-show--sm']"),"Author Role");
    private static  Label AUTHOR_ARTICLE_COUNT = new Label(By.xpath("//span[@class='m-author-count m-show--sm']"), "Author article count");



    public AuthorPageNew() {
        super();
    }



    @Step("Open Author page")
    public static String getAuthorPageLink() {
        return AUTHOR_PAGE_LINK;
    }



    @Step("Verifying Author Blocks")
    public static void verifyAuthorBlockIsDisplayed() throws UnsupportedEncodingException {

      /*  assertTrue(PAGE_AUTHOR_TITLE_WEB.isPresent(), "author title is present");
        assertTrue(AUTHOR_NAME_WEB.isPresent(), "author name is present");
        assertTrue(AUTHOR_IMAGE.isImageLoaded(), "Image is Loaded");
        assertTrue(AUTHOR_TWITTER.isPresent(), "Twitter is present");
        assertTrue(AUTHOR_LINKEDIN.isPresent(), "Linkedin is present");
        assertTrue(AUTHOR_DESCRIPTION.isPresent(), "Description is present");
        assertTrue(AUTHOR_SHOW_MORE.isPresent(), "Show more button is present");
       // System.out.println("Author role = " + AUTHOR_ROLE_WEB.getText());

        String authorRole = AUTHOR_ROLE_WEB.getText();

        if(DeviceUtils.isMobile()) {
            authorRole = AUTHOR_ROLE_MOBILE.getText().trim();
        } else {
            authorRole = AUTHOR_ROLE_WEB.getText();
        }
        System.out.println("Author role text = " +authorRole);

        assertTrue(AUTHOR_ROLE_WEB.isPresent(), "Author role is present");
        assertTrue(AUTHOR_ROLE_MOBILE.isPresent(), "Author role is present on mobile");
        assertTrue(authorRole.contains("Journaliste"), "Author role is correct");
*/
        if (DeviceUtils.isMobile()) {
            assertTrue(PAGE_AUTHOR_TITLE_MOBILE.isPresent(), "Author title is present on mobile");
            assertTrue(AUTHOR_NAME_MOBILE.isPresent(), "Author name is present on mobile");
        } else {
            assertTrue(PAGE_AUTHOR_TITLE_WEB.isPresent(), "Author title is present on web");
            assertTrue(AUTHOR_NAME_WEB.isPresent(), "Author name is present on web");
        }

        assertTrue(AUTHOR_IMAGE.isImageLoaded(), "Image is Loaded");
        assertTrue(AUTHOR_TWITTER.isPresent(), "Twitter is present");
        assertTrue(AUTHOR_LINKEDIN.isPresent(), "LinkedIn is present");
        assertTrue(AUTHOR_DESCRIPTION.isPresent(), "Description is present");
        assertTrue(AUTHOR_SHOW_MORE.isPresent(), "Show more button is present");

        // Author role based on device
        /*String authorRoleText;
        if (DeviceUtils.isMobile()) {
            assertTrue(AUTHOR_ROLE_MOBILE.isPresent(), "Author role is present on mobile");
            authorRoleText = AUTHOR_ROLE_MOBILE.getText().trim();
        } else {
            assertTrue(AUTHOR_ROLE_WEB.isPresent(), "Author role is present on web");
            authorRoleText = AUTHOR_ROLE_WEB.getText().trim();
        }
        System.out.println("TEST ROLE");
        System.out.println("Author role text = " + authorRoleText);
        assertTrue(authorRoleText.contains("Journaliste"), "Author role is correct");*/
    }





    @Step("Verifying Authors block data")
    public static void verifyAuthorBlockData(String name) {

        /*String titleText = PAGE_AUTHOR_TITLE_WEB.getText();
        //String titleTexMobile = PAGE_AUTHOR_TITLE_MOBILE.getText();
        String authorNameText = AUTHOR_NAME_WEB.getText();
        //String authorNameTextMobile = AUTHOR_NAME_MOBILE.getText();

        System.out.println("PAGE_AUTHOR_TITLE_WEB text = " + titleText);
        System.out.println("AUTHOR_NAME_WEB = " + authorNameText);
        System.out.println("Expected author name on web = " + name);
        System.out.println("Expected author name on mobile = " + name);

        if (DeviceUtils.isMobile()) {
            titleText = PAGE_AUTHOR_TITLE_MOBILE.getText();
        } else {
            titleText = PAGE_AUTHOR_TITLE_WEB.getText();
        }

        if(DeviceUtils.isMobile()) {
            authorNameText = AUTHOR_NAME_MOBILE.getText();
        } else {
            authorNameText = AUTHOR_NAME_WEB.getText();
        }


        assertTrue(titleText.contains("Romaric Saint Aubert"),
                "Author title contains static name");

        assertTrue(titleText.contains(name),
                "Author title contains expected name");

        assertEquals(authorNameText, name,
                "Author name matches expected value");



    }*/
        String titleText;
        String authorNameText;

        if (DeviceUtils.isMobile()) {
            titleText = PAGE_AUTHOR_TITLE_MOBILE.getText().trim();
            authorNameText = AUTHOR_NAME_MOBILE.getText().trim();
        } else {
            titleText = PAGE_AUTHOR_TITLE_WEB.getText().trim();
            authorNameText = AUTHOR_NAME_WEB.getText().trim();
        }

        System.out.println("Is Mobile Run = " + DeviceUtils.isMobile());
        System.out.println("Author title text = " + titleText);
        System.out.println("Author name text = " + authorNameText);

        assertTrue(titleText.contains("Romaric Saint Aubert"),
                "Author title contains static name");

        assertTrue(titleText.contains(name),
                "Author title contains expected name");

        assertEquals(authorNameText, name,
                "Author name matches expected value");
    }

    @Step("Verifying X button")
    public static void verifyXButtonPresentOnAuthorPage() {
        System.out.println(AUTHOR_TWITTER.getText());
        assertTrue(AUTHOR_TWITTER.isPresent(), "Twitter is present");
    }

    @Step("Verifying LinkedIn button")
    public static void verifylinkedInButtonPresentOnAuthorPage() {
        System.out.println(AUTHOR_LINKEDIN.getText());
        assertTrue(AUTHOR_LINKEDIN.isPresent(), "Linkedin is present");
    }

    @Step("Verifying Show more button")
    public static void verifyShowMoreButtonPresentOnAuthorPage() {
        assertTrue(AUTHOR_SHOW_MORE.isPresent(), "Show More is present");
    }

    @Step("Get author article count")
    public static int getAuthorArticleCount() {
        String text = AUTHOR_ARTICLE_COUNT.getText(); // "1931 Articles"
        System.out.println(text);
        String cleaned = text.replaceAll("[^0-9]", "").trim();
        if(cleaned.isEmpty()) {
            logger.info("No number found in article count text:" +text);
        }
        return Integer.parseInt(cleaned);
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
