package com.cryptonaute.tests;

import com.cryptonaute.templates.AuthorPageNew;
import framework.browser.Browser;
import framework.tests.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.cryptonaute.templates.AuthorPage.*;
import static com.cryptonaute.templates.AuthorPage.verifyAuthorArticleCountIsDisplayed;
import static com.cryptonaute.templates.AuthorPage.verifyShowMoreButtonPresentOnAuthorPage;
import static com.cryptonaute.templates.AuthorPage.verifyXButtonPresentOnAuthorPage;
import static com.cryptonaute.templates.AuthorPage.verifylinkedInButtonPresentOnAuthorPage;

public class AuthorTestNew extends BaseTest {
    public AuthorTestNew() throws IOException {
    }

    @Test
    public void verifyThatAuthorPageIsOpenedAndLooksAsExpected() throws UnsupportedEncodingException {
        browser.navigate(Browser.getCurrentUrl() +getAuthorPageLink(), true);
        AuthorPageNew author = new AuthorPageNew();
        //author.verifyAuthorBlockIsDisplayed();
        //author.verifyAuthorBlockData("Romaric Saint Aubert");
        author.verifyXButtonPresentOnAuthorPage();
        author.verifylinkedInButtonPresentOnAuthorPage();
        author.verifyShowMoreButtonPresentOnAuthorPage();
        author.verifyAuthorArticleCountIsDisplayed();

    }
}
