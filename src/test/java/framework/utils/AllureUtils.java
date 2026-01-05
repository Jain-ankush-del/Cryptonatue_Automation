package framework.utils;

import framework.logger.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static framework.browser.Browser.getConfigProperty;
import static framework.utils.FileUtils.returnFilesFromDirectory;

public class AllureUtils {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String PATH_TO_ALLURE_REPORTS = "path.to.allure.reports";

    private AllureUtils() {}

    public static void removeSkippedTestsFromAllureReport() {
        String pathToAllureReports = getConfigProperty(PATH_TO_ALLURE_REPORTS);
        List<File> allureReportsToProcess = new ArrayList<>(returnFilesFromDirectory(pathToAllureReports));

        if (allureReportsToProcess.isEmpty()) return;

        allureReportsToProcess.removeIf(fileName -> !fileName.getName().contains("result"));

        allureReportsToProcess.forEach(fileToProcess -> {
            try (FileReader fileReader = new FileReader(fileToProcess)) {
                JSONObject resultObj = (JSONObject) new JSONParser().parse(fileReader);

                if (resultObj.get("status").equals("skipped")) {
                    fileToProcess.deleteOnExit();
                }
            } catch (Exception exc) {
                logger.fatal(exc.getMessage());
            }
        });
    }
}