package com.cryptonaute.tests;

import com.cryptonaute.pageobject.header.Header;
import framework.tests.BaseTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static framework.browser.Browser.closeUpperWindow;
import static framework.browser.Browser.scrollToTop;

public class HeaderTest extends BaseTest {
    public HeaderTest() throws IOException {
    }

    @Test
    void verifyHeaderExist() {
        Header header =new Header();
       header.verifyHeaderExist();
    }
    @Test
    void verifyAllFooterHasMandatoryElements() {
        Header header =new Header();
        header.navigateHomeThroughLogo();
    }

   @Test()
    void verifyTelegramLink() throws UnsupportedEncodingException {
        Header header = new Header();
        header.checkTelegramFromHeader();
    }

    @AfterMethod(alwaysRun = true)
    void navigateBack() {
        try {
            closeUpperWindow(); // only closes if exists
        } catch (Exception ignored) {
        }
        scrollToTop();
    }


    @Test
    void verifyNavigationMenu(){
        Header header = new Header();
        header.checkMenu();
    }
}
