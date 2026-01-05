package framework.listeners;

import framework.browser.Browser;
import framework.tests.BaseTest;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Stage;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;

import java.io.IOException;
import java.util.LinkedList;

import static io.qameta.allure.Allure.getLifecycle;

public class AllureTestListener extends BaseTest implements TestLifecycleListener {

    public AllureTestListener() throws IOException {
    }

    @Override
    public void beforeTestStop(TestResult result) {
        getLifecycle().updateTestCase(testCase -> {
            if (testCase.getSteps().stream().anyMatch(element -> element.getStatus() != null && element.getStatus().equals(Status.BROKEN))) {
                testCase.setStatus(Status.BROKEN);
            }

            if (testCase.getSteps().stream().anyMatch(element -> element.getStatus() != null && element.getStatus().equals(Status.FAILED))) {
                testCase.setStatus(Status.FAILED);
            }

            if (testCase.getName().contains("Not Found")) {
                testCase.setStatus(Status.BROKEN);
            }

            String testName = getTestName();
            if (testName != null) {
                testCase.setName(testName);
            } else if (testCase.getStatus().equals(Status.BROKEN) || testCase.getStatus().equals(Status.FAILED)){
                testCase.setName(testCase.getName() + " (URL: " + Browser.getCurrentUrl(true) + ")");
            }

            testCase.setParameters(new LinkedList<>());
            testCase.setStage(Stage.FINISHED).setStop(System.currentTimeMillis());
        });
    }
}