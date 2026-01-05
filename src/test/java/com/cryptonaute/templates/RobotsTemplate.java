package com.cryptonaute.templates;

import com.cryptonaute.pages.StandardPage;
import framework.browser.Browser;
import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static framework.browser.Browser.getDriver;
import static org.testng.AssertJUnit.assertEquals;

public class RobotsTemplate extends StandardPage {
    private static By ROBOTS_TEXT = By.xpath("//pre[contains(text(),'# START YOAST BLOCK')]");
    private static Label ROBOTS_TEXT_LABEL = new Label(ROBOTS_TEXT, "Robot Text");
    private static String ROBOT_LINK = "/robots.txt";

    private static String ROBOTS_TEXT_STRING ="User-agent: *" +
            "Disallow: /wp-admin/" +
            "Disallow: /?s=" +
            "Sitemap: https://dev.cryptonaute.fr/sitemap_index.xml";


    public RobotsTemplate() {
        super();
    }

    @Step("Get Robots link...")
    public String getRobotsLink() {
        return ROBOT_LINK;
    }

    @Step("Open Robots page")
    public void openRobotsLink() {
        browser.navigate(Browser.getCurrentUrl() + getRobotsLink());
    }

    @Step("Verify that Robots.txt file is opened and looks as expected")
    public void verifyRobotsIsOpenedAndHasProperText() {
     /*   if (getDriver().getCurrentUrl().contains("dev"))
        {
            assertEquals("Robots are different on stage",  ROBOTS_TEXT_LABEL.getText());
        }else {
        assertEquals("Robots are different on prod", ROBOTS_TEXT_STRING, ROBOTS_TEXT_LABEL.getText());
    }
        logger.info("Robots file looks as expected...");
    }*/
        String robotsContent = getDriver().getPageSource();
        System.out.println("===== ROBOTS.TXT CONTENT =====");
        System.out.println(robotsContent);
        System.out.println("===== END =====");

        if (getDriver().getCurrentUrl().contains("staging")
                || getDriver().getCurrentUrl().contains("dev")) {

            Assert.assertTrue(
                    robotsContent.contains("User-agent:"),
                    "Robots.txt does not contain User-agent on dev/stage"
            );

        } else {

            Assert.assertTrue(
                    robotsContent.contains("Sitemap"),
                    "Robots.txt does not contain Sitemap on prod"
            );
        }

        logger.info("Robots file looks as expected...");
    }
}