package com.cryptonaute.tests;

import com.cryptonaute.templates.RobotsTemplate;
import framework.tests.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import java.io.IOException;

public class RobotsTest extends BaseTest
{
    public RobotsTest() throws IOException {
    }

    @Description("Verify that Robots.txt file is loaded")
    @Test(testName = "Verify Robots.txt file")
    void verifyThatRobotsPageIIsOpenedAndLooksAsExpected() {
        RobotsTemplate robotsTemplate = new RobotsTemplate();
        robotsTemplate.openRobotsLink();
        robotsTemplate.verifyRobotsIsOpenedAndHasProperText();
    }
}
