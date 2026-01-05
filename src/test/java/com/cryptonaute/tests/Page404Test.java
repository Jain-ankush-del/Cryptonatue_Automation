package com.cryptonaute.tests;

import com.cryptonaute.templates.Page404Template;
import framework.browser.Browser;
import framework.tests.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class Page404Test extends BaseTest {

    public Page404Test() throws IOException {
    }

    @Test()
    void verifyThatRobotsPageIIsOpenedAndLooksAsExpected() throws IOException {
        ;
        Page404Template page404Template = new Page404Template();
        browser.navigate(Browser.getCurrentUrl() + page404Template.get404Link());
        page404Template.verify404IsOpened();
        page404Template.verify404PageElements();
    }
}
