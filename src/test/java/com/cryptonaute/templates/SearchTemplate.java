package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static org.testng.Assert.assertTrue;

public class SearchTemplate  extends StandardPage {

    private static final By PAGE_TITLE = By.xpath("//h1[contains(text(),'Chercher des résultats pour')]");
    private static final Label PAGE_TITLE_TEXT = new Label(PAGE_TITLE, "Page title");


    public SearchTemplate() {
        super();
    };

    @Step("Search page is opened...")
    public static void verifySearchPageIsOpened(String text) {
        assertTrue(PAGE_TITLE_TEXT.getText().contains("Chercher des résultats pour test"),"Search page isn't opened");
        assertTrue(PAGE_TITLE_TEXT.getText().contains(text),"Search page isn't opened");
    };
}
