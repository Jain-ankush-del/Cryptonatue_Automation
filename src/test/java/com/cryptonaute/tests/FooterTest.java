package com.cryptonaute.tests;

import com.cryptonaute.pageobject.footer.Footer;
import framework.forms.BasePage;
import framework.tests.BaseTest;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static framework.browser.Browser.*;

public class FooterTest extends BaseTest {
    public FooterTest() throws IOException {
    }

  @Test
    void verifyScrollToTopButtonScrollsPageToTop() {
        Footer footer = new Footer();
        footer.clickScrollToTopButton();
      /*JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });");*/
        waitForScrollToComplete("top");
        Assert.assertTrue(isPageAtTop(), "Page wasn't scrolled to the top");
    }

  @Test()
    void verifyAllFooterHasMandatoryElements() {
        Footer footer =new Footer();
        footer.VerifyFooterHasMandatoryElements();
    }


   @Test()
    void verifyDescriptionsIsDisplayed() {
        Footer footer =new Footer();
        footer.verifyFooterDescriptionIsDisplayed();
    }

   @Test()
    public void verifyFooterLinksAreValid() {
        Footer footer = new Footer();
        footer.verifyFooterLinksAreValid();
    }
}
