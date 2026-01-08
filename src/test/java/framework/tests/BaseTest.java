package framework.tests;

import framework.browser.Browser;
import framework.driversettings.PropertiesHandler;
import framework.logger.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

import static framework.browser.Browser.driver;
import static framework.browser.Browser.getConfigProperty;

public class BaseTest {
    protected static final ThreadLocal<String> testName = new ThreadLocal<>();

    protected Logger logger = Logger.getInstance();

    protected Browser browser;

    private ITestResult iTestResult;

    private static final String DATA_PROPERTIES_FILE = "data/data.properties";

    private static final String NAVIGATE_URL = "locale.navigate.url";
    private static final String PATH_TO_REPORTS = "path.to.log.reports";
    private static final String LOG_PARASITIC_LINES = "log.parasitic.lines";
    private static final String PATH_TO_EXCEL_REPORT = "path.to.excel.report";

    private static final String UI_ERRORS_EXCEL_SHEET_NAME = "UI errors";

    private static final PropertiesHandler dataProperty;



    
    static {
        try {
            dataProperty = new PropertiesHandler(DATA_PROPERTIES_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseTest() throws IOException {
    }

    @Parameters({ "env" })
    @BeforeMethod
    public void setUp(@Optional("dev") final String env) {
        browser = Browser.getInstance();
        browser.getDriver().manage().window().maximize();
        // Timeouts
        browser.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
        browser.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Explicit wait
        WebDriverWait wait = new WebDriverWait(browser.getDriver(), Duration.ofSeconds(60));
        String url = getConfigProperty(env + ".URL");
        switch (env) {
            case "dev":
            case "stage":
                Browser.extractBasicAuthCredentialsFromUrl(url);
                browser.navigate(url);

                logger.info(String.format(Logger.getLogMessage(NAVIGATE_URL), url));
                break;
            case "prod":
                browser.navigate(url);
                logger.info(String.format(Logger.getLogMessage(NAVIGATE_URL), url));
                break;
            default:
                logger.fatal("Cannot define test env: " + env);
        }
        logger.info(String.format(Logger.getLogMessage(NAVIGATE_URL), url));
    }
    /*
        @BeforeMethod
        public void setInitialUrlForTestMethod(ITestResult iTestResult) {
            this.iTestResult = iTestResult;
            iTestResult.setAttribute(iTestResult.getMethod().getMethodName(), getCurrentUrl());
        }
    */
    @AfterMethod(alwaysRun = true)
    public void teardownAfterMethod() {
        testName.remove();
    }

    @AfterTest(alwaysRun = true)
    public void teardownAfterTest() {
        if (browser != null && browser.getDriver() != null) {
            browser.quit();
        } //driver.remove();
    }

    /*@Parameters({ "removeSkippedTestsFromAllureReport" })
    @AfterSuite(alwaysRun = true)
    public void teardownAfterSuite(@Optional("false") final boolean removeSkippedTestsFromAllureReport) {
        String[] parasiticLines = getLogProperty(LOG_PARASITIC_LINES).split(", ");
        String pathToReports = getConfigProperty(PATH_TO_REPORTS);
        String pathToExcelReport = getConfigProperty(PATH_TO_EXCEL_REPORT);
        List<File> reportsToProcess = returnFilesFromDirectory(pathToReports);

        stopAppiumServerAndKillEmulator();

        reorderLogsByThreads(pathToReports);
        reportsToProcess.forEach(report -> removeParasiticLinesFromFile(report.getPath(), parasiticLines));

        convertLogReportsToExcel(reportsToProcess, pathToExcelReport);
        addSheetToXlsxFile(pathToExcelReport, UI_ERRORS_EXCEL_SHEET_NAME, 0);
        writeToExcelFile(pathToExcelReport, UI_ERRORS_EXCEL_SHEET_NAME, new String[][] {{ "Page URL", "Notes" }});

        if (removeSkippedTestsFromAllureReport) AllureUtils.removeSkippedTestsFromAllureReport();
    }*/

    protected void setTestName(String name) {
        testName.set(name);
    }

    protected String getTestName() {
        return testName.get();
    }

    protected ITestResult getTestResult() {
        return iTestResult;
    }

    public static String getDataProperty(String key) {
        return dataProperty.getProperty(key);
    }

}