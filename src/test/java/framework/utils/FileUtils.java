package framework.utils;

import framework.driversettings.PropertiesHandler;
import framework.logger.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static framework.browser.Browser.getConfigProperty;
import static framework.utils.RandomUtils.generateRandomNumber;

public class FileUtils {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String PATH_TO_FORMS_PROPERTIES_FILE = "path.to.forms";
    private static final String PATH_TO_DOWNLOADS = "path.to.downloads";
    private static final String DOWNLOAD_WAIT = "locale.download.wait";
    private static final String DOWNLOAD_TIMEOUT = "locale.download.timeout";
    private static final String DOWNLOAD_FINISHED = "locale.download.finished";
    private static final String DOWNLOAD_VERIFY = "locale.download.verify";
    private static final String DOWNLOAD_VERIFIED = "locale.download.verified";
    private static final String DOWNLOAD_FAILED = "locale.download.failed";
    private static final String REGEX_TO_REMOVE = "regex.to.remove.from.files";
    private static final String ABSENT = "locale.is.absent";

    private static String pathToDownloads;
    private static String regexToRemove;

    private static final List<String> formsToTest = new ArrayList<>();

    private FileUtils() {}

    static {
        try {
            pathToDownloads = Paths.get(getConfigProperty(PATH_TO_DOWNLOADS)).toFile().getCanonicalPath();
            regexToRemove = getConfigProperty(REGEX_TO_REMOVE);
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static String getPathToDownloads() {
        return pathToDownloads;
    }

    public static void clearDownloadDir() {
        try {
            Files.delete(Paths.get(getConfigProperty(PATH_TO_DOWNLOADS)));
        } catch (IOException exc) {
            logger.fatal("Error when removing downloads folder", exc.getMessage());
        }
    }
/*
    public static void assertDownload(By locator) {
        String downloadHref = getDriver().findElement(locator).getAttribute(Attributes.HREF.getValue());
        String fileNameFromHref = downloadHref.substring(downloadHref.lastIndexOf('/')+1);
        String downloadedFile = Paths.get(fileNameFromHref).getFileName().toString();
        Path downloadFilePath = Paths.get(pathToDownloads, downloadedFile);

        logger.info(String.format(getLogMessage(DOWNLOAD_WAIT), fileNameFromHref));

        try {
            new WebDriverWait(getDriver(), Duration.ofMillis(getDownloadTimeout()))
                    .until(condition -> downloadFilePath.toFile().exists());
        } catch (TimeoutException e) {
            logger.fatal(getLogMessage(DOWNLOAD_TIMEOUT), e.getMessage());
        }

        logger.info(getLogMessage(DOWNLOAD_FINISHED));

        String actual = downloadFilePath.toFile().getName();

        logger.info(getLogMessage(DOWNLOAD_VERIFY));

        try {
            Assert.assertEquals(actual, fileNameFromHref, (getLogMessage(ABSENT)));
        } catch (AssertionError exc) {
            logger.fatal(getLogMessage(DOWNLOAD_FAILED), exc.getMessage());
        }

        logger.info(getLogMessage(DOWNLOAD_VERIFIED));
    }
*/
    public static String readFileAndReturnString(String pathToFile){
        try (Scanner scanner = new Scanner(Paths.get(pathToFile), StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
            return null;
        }
    }

    public static List<String> readFileAndReturnListOfLines(String pathToFile){
        List<String> lines = new ArrayList<>();
        String singleLine;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            while ((singleLine = br.readLine()) != null) {
                lines.add(singleLine);
            }
            return lines;
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
            return Collections.emptyList();
        }
    }

    public static void removeParasiticLinesFromFile(String pathToFile, String... stringsSequence) {
        List<String> linesFromFile = readFileAndReturnListOfLines(pathToFile);
        List<String> updatedListOfLines;
        int occurrences = 0;

        try (PrintWriter writer = new PrintWriter(pathToFile)) {
            for (String string: stringsSequence) {
                occurrences += linesFromFile.stream().filter(line -> line.contains(string)).count();
            }

            if (occurrences == linesFromFile.size()) return;

            updatedListOfLines = returnListWithRemovedDuplicates(linesFromFile, stringsSequence);
            updatedListOfLines.forEach(listElement -> {
                listElement = Pattern.compile(regexToRemove).matcher(listElement).replaceAll("");
                writer.append(listElement.concat("\n")).flush();
            });
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    private static List<String> returnListWithRemovedDuplicates(List<String> listToClean, String[] repeatedElementsToRemove) {
        int i = 0;

        while (i < listToClean.size() - repeatedElementsToRemove.length) {
            boolean isParasitic = false;
            int j = 0;
            int k = i;

            while (j <= repeatedElementsToRemove.length) {
                isParasitic = StringUtils.containsAny(listToClean.get(k), repeatedElementsToRemove);
                j++;
                k++;
            }

            if (isParasitic) {
                IntStream.range(i, i + repeatedElementsToRemove.length).forEach(obj -> listToClean.set(obj, null));
            }
            i += repeatedElementsToRemove.length;

            while ((i < listToClean.size()) && !StringUtils.containsAny(listToClean.get(i), repeatedElementsToRemove)) {
                i++;
            }
        }

        listToClean.removeAll(Collections.singleton(null));

        for (int x = listToClean.size() - 1; x >= 0; x--) {
            boolean endElementIsDuplicate = StringUtils.containsAny(listToClean.get(x), repeatedElementsToRemove);

            if (!endElementIsDuplicate) break;
            listToClean.remove(x);
        }

        return listToClean;
    }

    public static void reorderLogsByThreads(String pathToLogs, String... threadName) {
        List<File> listOfLogFiles = returnFilesFromDirectory(pathToLogs);

        String thread = VarargsUtils.isNotEmpty(threadName) ? threadName[0] : "TestNG-tests";

        listOfLogFiles.forEach(logFile -> reorderFileRecordsByString(logFile.getPath(), thread));
    }

    public static void reorderFileRecordsByString(String pathToFile, String baseString) {
        List<String> linesFromFile = readFileAndReturnListOfLines(pathToFile);
        linesFromFile.removeIf(obj -> !obj.contains(baseString));

        List<String> distinctThreadNames = new ArrayList<>(linesFromFile);
        distinctThreadNames.replaceAll(obj -> obj.substring(obj.indexOf(baseString), obj.indexOf("]")));
        distinctThreadNames = distinctThreadNames.stream().distinct().collect(Collectors.toList());
        distinctThreadNames.replaceAll(obj -> obj.replaceAll("\\D+",""));

        List<String> reorderedLines = new ArrayList<>();

        int maxThreadsNumber = distinctThreadNames.stream()
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(1);

        for (int i = 1; i <= maxThreadsNumber; i++) {
            int iterator = i;
            linesFromFile.stream()
                    .filter(obj -> obj.matches(".*".concat(baseString).concat("-").concat(String.valueOf(iterator).concat("\\W.*"))))
                    .forEachOrdered(reorderedLines::add);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
            for (String listElement: reorderedLines) {
                Objects.requireNonNull(writer).append(listElement.concat("\n"));
                writer.flush();
            }
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static void sortFileAlphabetically(String pathToFile) {
        List<String> linesFromFile = readFileAndReturnListOfLines(pathToFile);

        linesFromFile.removeIf(obj -> StringUtils.containsAnyIgnoreCase(obj, new String[] {"[TestNG] Running:", ".xml"}) | obj.equals(""));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
            Collections.sort(linesFromFile);
            for (String listElement: linesFromFile) {
                Objects.requireNonNull(writer).append(listElement.concat("\n"));
                writer.flush();
            }
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static void removeLinesContainingStringFromFile(String pathToFile, String keyWord) {
        List<String> linesFromFile;

        try (PrintWriter writer = new PrintWriter(pathToFile)) {
            linesFromFile = readFileAndReturnListOfLines(pathToFile);
            Objects.requireNonNull(linesFromFile).removeIf(element -> element.contains(keyWord));
            for (String listElement: linesFromFile) {
                Objects.requireNonNull(writer).append(listElement.concat("\n"));
                writer.flush();
            }
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static void deleteFile(Path pathToFile) {
        try {
            Files.delete(pathToFile);
        } catch (IOException exc) {
            logger.fatal("Error when removing file", exc.getMessage());
        }
    }

    public static <T> T returnRandomValueFromList(List<T> list) {
        if (list.size() > 1) {
            return list.get(generateRandomNumber(list.size() - 1));
        } else {
            return list.get(0);
        }
    }

    public static List<String> returnFormsFromFile() throws IOException {
        PropertiesHandler formsProperties = new PropertiesHandler(getConfigProperty(PATH_TO_FORMS_PROPERTIES_FILE));
        int formCounter = 1;
        String formToTest;

        do {
            formToTest = formsProperties.getProperty(String.valueOf(formCounter));
            formsToTest.add(formToTest);
            formCounter++;
        } while (formsProperties.getProperty(String.valueOf(formCounter)) != null);

        return formsToTest;
    }

    public static List<File> returnFilesFromDirectory(String pathToDirectory) {
        File directoryPath = new File(pathToDirectory);
        File[] files = directoryPath.listFiles(File::isFile);

        if (files == null) return null;

        return Arrays.asList(files);
    }
}