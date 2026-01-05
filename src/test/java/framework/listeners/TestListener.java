package framework.listeners;

import framework.logger.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class TestListener implements ITestListener {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String TEST_CASE = "locale.logger.test.case";
    private static final String TEST_SKIP = "locale.logger.test.skip";
    private static final String TEST_SUCCESS = "locale.logger.test.success";
    private static final String TEST_FAILURE = "locale.logger.test.failure";

    private static final String FORMATTED_START = "=====================  %1$s: '%2$s' =====================";
    private static final String FORMATTED_END = "===================== %1$s: '%2$s' %3$s =====================";

    @Override
    public void onTestStart(ITestResult result) {
        String formattedLog= String.format(FORMATTED_START, Logger.getLogMessage(TEST_CASE), result.getName());
        logTestStatus(formattedLog);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String formattedLog;
        formattedLog = String.format(FORMATTED_END, Logger.getLogMessage(TEST_CASE), result.getName(), Logger.getLogMessage(TEST_SUCCESS));
        logTestStatus(formattedLog);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String formattedLog;
        formattedLog = String.format(FORMATTED_END, Logger.getLogMessage(TEST_CASE), result.getName(), Logger.getLogMessage(TEST_FAILURE));
        logTestStatus(formattedLog);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String formattedLog;
        formattedLog = String.format(FORMATTED_END, Logger.getLogMessage(TEST_CASE), result.getName(), Logger.getLogMessage(TEST_SKIP));
        logTestStatus(formattedLog);
    }

    private void logTestStatus(String formattedLog) {
        StringBuilder divider = new StringBuilder();
        int nChars = formattedLog.length();
        //divider.append("*".repeat(nChars));

        logger.info(divider.toString());
        logger.info(formattedLog);
        logger.info(divider.toString());
        logger.info("");
        logger.info("");
    }
}