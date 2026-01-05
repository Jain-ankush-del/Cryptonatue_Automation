package framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

import static framework.logger.Logger.getLogProperty;
import static framework.utils.ExcelUtils.writeToExcelFile;

public class LogsUtils {

    private static final String LOG_PAGE_IDENTIFIER = "log.form.identifier";
    private static final String LOG_PAGES_SEPARATOR = "log.forms.separator";
    private static final String LOG_PAGE_URL_OPEN = "log.form.url.open";
    private static final String LOG_PAGE_URL_CLOSE = "log.form.url.close";

    private static final String PAGE_IDENTIFIER = getLogProperty(LOG_PAGE_IDENTIFIER);
    private static final String PAGES_SEPARATOR = getLogProperty(LOG_PAGES_SEPARATOR);
    private static final String PAGE_URL_OPEN = getLogProperty(LOG_PAGE_URL_OPEN);
    private static final String PAGE_URL_CLOSE = getLogProperty(LOG_PAGE_URL_CLOSE);

    private LogsUtils() {}

    public static void convertLogReportsToExcel(List<File> reports, String pathToResultFile) {
        reports.forEach(report -> {
            String reportPath = report.getPath();
            String reportName = report.getName();
            String[][] pagesAndNotes = getArraysOfPagesAndNotesFromLog(reportPath);

            writeToExcelFile(pathToResultFile, reportName.substring(0, reportName.indexOf('.')), pagesAndNotes);
        });
    }

    public static String[][] getArraysOfPagesAndNotesFromLog(String filePath) {
        List<String> lines = FileUtils.readFileAndReturnListOfLines(filePath);

        if (lines.isEmpty()) return new String[0][];

        int pagesInLog = (int) lines.stream().filter(line -> line.contains(PAGE_IDENTIFIER)).count();
        String[][] pagesWithNotes = new String[pagesInLog + 1][2];

        pagesWithNotes[0][0] = "Page URL";
        pagesWithNotes[0][1] = "Notes";

        lines.removeIf(line -> line.equals(PAGES_SEPARATOR));

        for (int i = 1 ; i < pagesInLog + 1; i++) {
            for (int j = 0 ; j < 2; j++) {
                if (j == 0) {
                    pagesWithNotes[i][j] = StringUtils.substringBetween(lines.get(0), PAGE_URL_OPEN, PAGE_URL_CLOSE);
                    lines.subList(0, 1).clear();
                } else {
                    int nextPageLine = lines.indexOf(lines.stream().filter(line -> line.contains(PAGE_IDENTIFIER)).findFirst().orElse(null));
                    int currentPageNotesEndLine = nextPageLine != -1 ? nextPageLine : lines.size();

                    List<String> notesList = lines.subList(0, currentPageNotesEndLine);
                    String notesString = String.join("\n---\n", notesList);

                    pagesWithNotes[i][j] = notesString;

                    lines.subList(0, currentPageNotesEndLine).clear();
                }
            }
        }
        return pagesWithNotes;
    }
}
