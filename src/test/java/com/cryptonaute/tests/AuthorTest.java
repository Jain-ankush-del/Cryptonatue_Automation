package com.cryptonaute.tests;

import framework.browser.Browser;
import framework.tests.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.cryptonaute.templates.AuthorPage.*;

public class AuthorTest extends BaseTest {

    public AuthorTest() throws IOException {
    }
        @Test
        public void verifyThatAuthorPageIsOpenedAndLooksAsExpected() throws UnsupportedEncodingException {
        browser.navigate(Browser.getCurrentUrl() +getAuthorPageLink(), true);
            verifyAuthorBlockIsDisplayed();
            verifyAuthorBlockData("Romaric Saint Aubert");
            verifyXButtonPresentOnAuthorPage();
            verifylinkedInButtonPresentOnAuthorPage();
            verifyShowMoreButtonPresentOnAuthorPage();
            verifyAuthorArticleCountIsDisplayed();

        }




}
