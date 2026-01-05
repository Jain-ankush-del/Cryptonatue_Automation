package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.browser.Browser;
import framework.elements.Image;
import framework.elements.Label;
import framework.elements.Link;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends StandardPage {

private static final String PAGE_LOCATOR ="//body[contains(@class, \"page-id-143122\")]";

    private final Label headerHomepage = new Label(By.xpath("//section[@class='m-hero']/div[1]"), "Homepage banner");
    private final Link latestNews = new Link(By.xpath("//div[@class = 'm-grid m-post-grid m-post-grid-7']//a"), "Latest news link");
    private final Image latestNewsImages = new Image(By.xpath("//div[@class = 'm-grid m-post-grid m-post-grid-7']//a"), "Latest news Images");
    private final Link NewsHeadingBanner = new Link(By.xpath("//div[@class = 'm-hero--menu-grid']/a/h2"), "Homepage Banner New title ");
    private final Link LiveCryptoLink = new Link(By.xpath("//div[@class = \"swiper-slide\"]/div/h3/a/font/font"), " Live Crypto news Link");
    private final Link AllNewsLink  = new Link(By.xpath("//section[contains(@class,'m-post-grid-section')]//div[contains(@class,'m-container')]//a"),"All News Link");
    private final Image AllNewsImage = new Image(By.xpath("//section[contains(@class,'m-post-grid-section')]//img"), "All News Images");
    private final Link BestBrokersLink = new Link (By.xpath("//div[(@class='grey-bg m-brokers mb-0')]//a"),"Best Brokers Link");
    private final Link BestExchangePlatform = new Link(By.xpath("//div[(@class='grey-bg m-brokers default')]//a"),"Best exchange platform");
    private final Link WatchCoin    = new Link(By.xpath("//div[(@class='coins-row')]//a"), "Watch coin");


    public HomePage() {
        super(By.xpath(PAGE_LOCATOR), Browser.getTitle());
    }

    @Step("Verifying Home Page Opened.")
    public boolean isPageOpened(){
        return headerHomepage.isDisplayed();
    }


    @Step("Verifying Home page links")
    public void verifyAllMainLinksValid() {
     latestNews.verifyLinksAreValid();
     NewsHeadingBanner.verifyLinksAreValid();
     //LiveCryptoLink.verifyLinksAreValid();
     AllNewsLink.verifyLinksAreValid();
     BestBrokersLink.verifyLinksAreValid();
     BestExchangePlatform.verifyLinksAreValid();
     WatchCoin.verifyLinksAreValid();

    }

    @Step("Verifying Home Page Images.")
    public void verifyAllImageValid(){
    //latestNewsImages.verifyImagesAreLoaded();
   AllNewsImage.verifyImagesAreLoaded();

    }
}
