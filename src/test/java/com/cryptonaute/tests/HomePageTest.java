package com.cryptonaute.tests;

import com.cryptonaute.templates.HomePage;
import framework.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class HomePageTest extends BaseTest {
    public HomePageTest() throws IOException {

    }
    @Test(priority = 0)
    void checkHomePage(){
        HomePage homePage = new HomePage();
        Assert.assertTrue(homePage.isPageOpened(), "Home Page was not opened ");
    }
    @Test(priority = 1)
    void verifyHomePageLinks(){
        HomePage homePage = new HomePage();
        homePage.verifyAllMainLinksValid();

    }

    @Test(priority = 2)
    void verifyHomePageImages(){
        HomePage homePage = new HomePage();
        homePage.verifyAllImageValid();
    }
}
