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

public class AuthorPageElement extends StandardPage {

        private static String AUTHOR_PAGE_LINK = "/auteurs/romaric/";

    private static final Label TITLE_DESKTOP =
            new Label(By.cssSelector("h4.m-author-about__title.m-show--sm"), "Title Desktop");

    private static final Label TITLE_MOBILE =
            new Label(By.cssSelector("h4.m-author-about__title.m-hide--sm"), "Title Mobile");

    private static final Label NAME_DESKTOP =
            new Label(By.cssSelector("h4.m-author-about__title.m-show--sm"), "Name Desktop");

    private static final Label NAME_MOBILE =
            new Label(By.cssSelector("h4.m-author-about__title.m-hide--sm"), "Name Mobile");

    // ================= ROLE =================
    private static final Label ROLE_DESKTOP =
            new Label(By.cssSelector("p.m-author-expertise.m-show--sm"), "Role Desktop");

    private static final Label ROLE_MOBILE =
            new Label(By.cssSelector("p.m-author-expertise.m-hide--sm"), "Role Mobile");

    // ================= OTHER =================
    private static final Image AUTHOR_IMAGE =
            new Image(By.xpath("//img[contains(@class,'avatar-120')]"), "Author image");

    private static final Button AUTHOR_TWITTER =
            new Button(By.xpath("//div[@class='m-author-socials'][1]/a[1]"), "Author Twitter");

    private static final Button AUTHOR_LINKEDIN =
            new Button(By.xpath("//div[@class='m-author-socials'][1]/a[2]"), "Author Linkedin");

    private static final Label AUTHOR_DESCRIPTION =
            new Label(By.cssSelector("div.m-author-about__description"), "Author Description");

    private static final Button AUTHOR_SHOW_MORE =
            new Button(By.cssSelector("button.show-more-author-button"), "Show More");

    private static final Label AUTHOR_ARTICLE_COUNT =
            new Label(By.cssSelector("span.m-author-count"), "Article Count");



    public AuthorPageElement() {
            super();
        }

    private static String getVisibleText(Label mobile, Label desktop) {
        if (mobile.isDisplayed()) {
            return mobile.getText().trim();
        }
        return desktop.getText().trim();
    }

    public static String getAuthorTitle() {
        return getVisibleText(TITLE_MOBILE, TITLE_DESKTOP);
    }

    public static String getAuthorName() {
        return getVisibleText(NAME_MOBILE, NAME_DESKTOP);
    }

    public static String getAuthorRole() {
        return getVisibleText(ROLE_MOBILE, ROLE_DESKTOP);
    }



    @Step("Open Author page")
        public static String getAuthorPageLink() {
            return AUTHOR_PAGE_LINK;
        }




        @Step("Verifying Author Blocks")
        public static void verifyAuthorBlockIsDisplayed() throws UnsupportedEncodingException {

           /* String title = getAuthorTitle();
            String name = getAuthorName();
            String role = getAuthorRole();

            System.out.println("TITLE = " + title);
            System.out.println("NAME  = " + name);
            System.out.println("ROLE  = " + role);

            assertTrue(!title.isEmpty(), "Title visible");
            assertTrue(!name.isEmpty(), "Name visible");
            assertTrue(!role.isEmpty(), "Role visible");
*/
            assertTrue(AUTHOR_IMAGE.isImageLoaded(), "Image loaded");
            assertTrue(AUTHOR_TWITTER.isPresent(), "Twitter present");
            assertTrue(AUTHOR_LINKEDIN.isPresent(), "LinkedIn present");
            assertTrue(AUTHOR_DESCRIPTION.isPresent(), "Description present");
            assertTrue(AUTHOR_SHOW_MORE.isPresent(), "Show more present");

    }



        @Step("Verifying Authors block data")
        public static void verifyAuthorBlockData(String name) {

           /* String titleText = TITLE_DESKTOP.getText();
            String authorNameText = NAME_DESKTOP.getText();

            System.out.println("PAGE_AUTHOR_TITLE text = " + titleText);
            System.out.println("AUTHOR_NAME text = " + authorNameText);
            System.out.println("Expected author name = " + name);

            assertTrue(titleText.contains("Romaric Saint Aubert"),
                    "Author title contains static name");

            assertTrue(titleText.contains(name),
                    "Author title contains expected name");

            assertEquals(authorNameText, name,
                    "Author name matches expected value");

*/
            String title = getAuthorTitle();
            String authorname = getAuthorName();
            String role = getAuthorRole();

            System.out.println("Title = " + title);
            System.out.println("Name  = " + authorname);
            System.out.println("Role  = " + role);

            assertTrue(title.contains(name), "Title contains author name");
            assertEquals(authorname, name, "Author name matches");
            assertTrue(role.contains("Journaliste"), "Role correct");

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

        /*@Step("Get author article count")
        public static int getAuthorArticleCount() {
            String text = AUTHOR_ARTICLE_COUNT.getText(); // "1934 Articles"
            System.out.println(text);
            String cleaned = text.replaceAll("[^0-9]", "").trim();
            if(cleaned.isEmpty()) {
                throw new RuntimeException("No number found in article count text:" +text);
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
        }*/
        @Step("Verify Article Count")
        public static void verifyAuthorArticleCountIsDisplayed() {
            Assert.assertTrue(AUTHOR_ARTICLE_COUNT.isPresent(), "Article count visible");

            /*String text = AUTHOR_ARTICLE_COUNT.getText();
            String number = text.replaceAll("[^0-9]", "");

            Assert.assertTrue(!number.isEmpty(), "Article count number exists");
            Assert.assertTrue(Integer.parseInt(number) > 0, "Article count > 0");*/
        }
}



