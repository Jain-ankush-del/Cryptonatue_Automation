package com.cryptonaute.tests;

import com.cryptonaute.templates.ContactUSPage;
import framework.tests.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class ContactUSTest extends BaseTest {
    public ContactUSTest() throws IOException {
    }

    @Test
    void verifyContactUsPage(){
        ContactUSPage contactUSPage = new ContactUSPage();
        contactUSPage.openContactUs();
        contactUSPage.verifyContacUsIsOpen();

        contactUSPage.verifyContactUsFormHasMandatoryElements();
    }
}
