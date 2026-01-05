package framework.logger;

import framework.driversettings.PropertiesHandler;
import framework.listeners.AllureStepListener;
import framework.localization.Localization;
import framework.utils.VarargsUtils;
import org.apache.logging.log4j.LogManager;
import org.testng.Assert;

import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("java:S6548")
public final class Logger {

    private static final String LOG4J2_PROPERTIES = "log.properties";
    private static final String LOG_LANGUAGE = "logger.language";
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Logger.class);

    private static final String STEP = "locale.logger.step";
    private static final String STEPS = "locale.logger.steps";

    public static final String INFORMATION = "INFO";
    public static final String WARNING = "WARN";
    public static final String FAIL = "FAILURE";

    private static final String INFO_BRACES = "[INFO] ";
    private static final String WARN_BRACES = "[WARN] ";
    private static final String ERROR_BRACES = "[ERROR] ";
    private static final String FATAL_BRACES = "[FATAL] ";

    private static Logger instance;

    private static PropertiesHandler propertyLogger;
    private static PropertiesHandler propertyLogMessages;

    private Logger() {}

    private static void initProperties() throws IOException {
        propertyLogger = new PropertiesHandler(LOG4J2_PROPERTIES);
        propertyLogMessages = Localization.setupLoggerLocale(propertyLogger.getProperty(LOG_LANGUAGE));
    }

    public static String getLogProperty(final String key) {
        return propertyLogger.getProperty(key);
    }

    public static String getLogMessage(final String key) {
        return propertyLogMessages.getProperty(key);
    }

    public static synchronized Logger getInstance() throws IOException {
        if (instance == null) {
            initProperties();
            instance = new Logger();
        }
        return instance;
    }

    public void step(final int step) {
        logSeparatorMsg(getLogMessage(STEP) + step);
    }

    public void steps(final int fromStep, final int toStep) {
        logSeparatorMsg(getLogMessage(STEPS) + fromStep + "-" + toStep);
    }

    private void logSeparatorMsg(final String msg) {
        info(String.format("--------------------------==[ %1$s ]==---------------------------", msg));
    }

    public void info(final String message) {
        log.info(INFO_BRACES.concat(message));
    }

    public void info(final String message, boolean... addMessageToAllureStep) {
        log.info(INFO_BRACES.concat(message));
        if (VarargsUtils.isTrue(addMessageToAllureStep)) {
            AllureStepListener.appendStep(message);
        }
    }

    public void warn(final String message) {
        log.warn(WARN_BRACES.concat(message));
        AllureStepListener.addLogToStep(message, WARNING);
    }

    public void warn(final String message, String exceptionToString) {
        log.warn(WARN_BRACES.concat(message).concat(": [").concat(exceptionToString).concat("]"));
        AllureStepListener.addLogToStep(message.concat("\n").concat(exceptionToString), WARNING);
        AllureStepListener.takeScreenshot(exceptionToString);
    }

    public void error(final String message) {
        log.fatal(ERROR_BRACES.concat(message));
        AllureStepListener.addLogToStep(message, FAIL);
    }

    public void fatal(final String message) {
        log.fatal(FATAL_BRACES.concat(message));
        Assert.fail(message);
    }

    public void fatal(final String message, Throwable exc) {
        log.fatal(FATAL_BRACES.concat(message).concat("\n"));
        log.fatal(Arrays.toString(exc.getStackTrace()));
        Assert.fail(message);
    }

    public void fatal(final String message, String exceptionToString) {
        log.fatal(FATAL_BRACES.concat(message).concat(": [").concat(exceptionToString).concat("]"));
        Assert.fail(message.concat(": [").concat(exceptionToString).concat("]"));
    }
}