package framework.listeners;

import framework.browser.Browser;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    int initCounter = 0;
    int retryLimit = 3;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            String urlToReNavigate = iTestResult.getAttribute(iTestResult.getMethod().getMethodName()).toString();

            if (initCounter < retryLimit) {
                Browser.getInstance().navigate(urlToReNavigate);
                initCounter++;
                iTestResult.setStatus(ITestResult.FAILURE);

                return true;
            } else {
                Browser.getInstance().navigate(urlToReNavigate);
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.getTestContext().getFailedTests().removeResult(iTestResult);
            iTestResult.getTestContext().getSkippedTests().removeResult(iTestResult);
            iTestResult.setStatus(ITestResult.SUCCESS);
        }

        return false;
    }
}