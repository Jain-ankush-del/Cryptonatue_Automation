package com.cryptonaute.tests;

import com.cryptonaute.templates.BMPage;
import com.cryptonaute.templates.HomePage;
import com.cryptonaute.templates.Page404Template;
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
import static com.cryptonaute.templates.BMPage.getBmPageLink;
import static com.cryptonaute.templates.BMPage.verifyBMBlockIsDisplayed;

public class BMTest extends BaseTest {
    public BMTest() throws IOException {}

    @Test(priority = 0)
    void verifyThatBMPageIsOpenAsExpected() throws UnsupportedEncodingException {
        BMPage bmPage = new BMPage();
        browser.navigate(Browser.getCurrentUrl() +getBmPageLink(), true);
        //bmPage.PopupClosed();
        bmPage.ShowMoreBuuton();
        bmPage.verifyBMBlockIsDisplayed();



    }


}
