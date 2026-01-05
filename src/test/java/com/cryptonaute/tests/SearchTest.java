package com.cryptonaute.tests;

import framework.tests.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.cryptonaute.pageobject.header.Search.useSearchInHeader;
import static com.cryptonaute.templates.SearchTemplate.verifySearchPageIsOpened;

public class SearchTest extends BaseTest {
    public SearchTest() throws IOException {
    }

    @Test
    void verifySearchCanBeUsedFromHPHeader() throws UnsupportedEncodingException {
        useSearchInHeader("test");
        verifySearchPageIsOpened("test");
    }
}
