package com.cryptonaute.pages;

import com.cryptonaute.pageobject.header.Telegram;
import framework.browser.Browser;
import framework.forms.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.UnsupportedEncodingException;

import static com.cryptonaute.pageobject.header.MainMenuDropdown.checkHeaderMenuLink;
import static com.cryptonaute.pageobject.header.Search.useSearchInHeader;

public class StandardPage  extends BasePage {

    private static final String PAGE_XPATH = "//a[@class='header-logo-link']//img";

    public StandardPage(By pageLocator, String pageTitle) {
        super(pageLocator, pageTitle);

    }

    public StandardPage() {
        super(By.xpath(PAGE_XPATH), Browser.getTitle());

    }

    @Step("Check Telegram button name")
    public void checkTelegramButtonNameHeader() throws UnsupportedEncodingException {
        Telegram.checkTelegramButtonName();
    }

    @Step("Check that telegrame link is opend")
    public void checkThatTelegramLinkIsOpenedHeader() throws UnsupportedEncodingException {
    Telegram.isTelegramChannelOpened();
    }

    @Step("Check Header links")
    public void checkHeaderLinks () throws UnsupportedEncodingException{
        checkHeaderMenuLink();
    }
    @Step("Check Search in Header")
    public void checkSearchInHeader (String text) throws UnsupportedEncodingException {
        useSearchInHeader(text);
    }

    }
