package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.browser.Browser;
import framework.elements.Button;
import framework.elements.DropDown;
import framework.elements.TextBox;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

import static framework.browser.Browser.getTitle;
import static org.testng.Assert.assertEquals;

public class ContactUSPage extends StandardPage {

    private String PAGE_CONTACT_US = "/contactez-notre-equipe-commerciale/";
    private final Button btnSubmit = new Button(By.xpath("//p/input[@value='Envoyer']"),"Submit");

    private final TextBox tbxFirstName = new TextBox(By.xpath("//input[@name='first_name']"), "First Name *");
    private final TextBox tbxLastName = new TextBox(By.xpath("//input[@name='last_name']"), "Last Name *");
    private final TextBox tbxWorkEmail = new TextBox(By.xpath("//input[@name='work_email']"), "Email *");
    private final TextBox tbxConfirmWorkEmail = new TextBox(By.xpath("//input[@name='work_email_confirm']"), "Email *");
    private final TextBox tbxCompany = new TextBox(By.xpath("//input[@name='company_name']"), "Company *");
    private final TextBox tbxWebsiteURL = new TextBox(By.xpath("//input[@name='company_url']"), "Your company website URL *");
    private final DropDown dropDownService = new DropDown(By.xpath("//select[@name='YourBudget']"),"Your budget *");
    private final DropDown dropDownProject = new DropDown(By.xpath("//select[@name='YourProject']"),"What project is it about? *");
    private final TextBox tbxPhone = new TextBox(By.xpath("//input[@name='phone_number']"), "Phone *");

    @Step("Get Contact Us link...")
    public String getContactUS() {
        return PAGE_CONTACT_US;
    }

    @Step("Open 404 page")
    public void openContactUs() {
        browser.navigate(Browser.getCurrentUrl() + getContactUS());
    }

    @Step("Get Contact Us link...")
    public void verifyContacUsIsOpen() {
        String pageTitle = getTitle();
        logger.info("waiting contact us to be opened...");
        logger.info(pageTitle);
        assertEquals(pageTitle, "Contactez notre équipe commerciale - Cryptonaute", "Contact Us isn't opened");
        logger.info("contact us opened...");
    }

    @Step("Verifying Home Page Opened.")
    public void verifyContactUsFormHasMandatoryElements() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(dropDownService.isDisplayed(), dropDownService.getNotDisplayedMessage());
        softAssert.assertTrue(dropDownProject.isDisplayed(), dropDownProject.getNotDisplayedMessage());

        softAssert.assertTrue(tbxCompany.isDisplayed(), tbxCompany.getNotDisplayedMessage());
        softAssert.assertTrue(tbxPhone.isDisplayed(), tbxPhone.getNotDisplayedMessage());
        softAssert.assertTrue(tbxFirstName.isDisplayed(), tbxFirstName.getNotDisplayedMessage());
        softAssert.assertTrue(tbxLastName.isDisplayed(), tbxLastName.getNotDisplayedMessage());
        softAssert.assertTrue(tbxWorkEmail.isDisplayed(), tbxWorkEmail.getNotDisplayedMessage());
        softAssert.assertTrue(tbxConfirmWorkEmail.isDisplayed(), tbxWorkEmail.getNotDisplayedMessage());
        softAssert.assertTrue(tbxWebsiteURL.isDisplayed(), tbxWebsiteURL.getNotDisplayedMessage());

        softAssert.assertTrue(btnSubmit.isDisplayed(), btnSubmit.getNotDisplayedMessage());

        softAssert.assertAll();
    }
}
