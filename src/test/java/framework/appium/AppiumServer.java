package framework.appium;

import framework.logger.Logger;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.IOException;
import java.net.URL;

import static framework.appium.AppiumDeviceFactory.capabilities;
import static framework.appium.AppiumDeviceFactory.getCapability;
import static framework.utils.CommandLineUtils.execCommand;

public class AppiumServer {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String KILL_ANDROID_EMU = "adb emu kill";
    private static final String KILL_IOS_EMU = "xcrun simctl shutdown all";
    private static final String LAUNCH_ANDROID_EMULATOR = "emulator -avd %s";

    private AppiumServer() {}

    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();

    public static void startAppiumServer(final String deviceName, final String browser) {
        if (isAppiumServerAlive()) {
            return;
        }

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.usingAnyFreePort();
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        builder.withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/");

        switch (browser.toLowerCase().trim()){
            case "chrome":
                builder.withArgument(() -> "--allow-insecure", "chromedriver_autodownload");
                execCommand(String.format(LAUNCH_ANDROID_EMULATOR, deviceName));
                break;
            case "safari":
                builder.withArgument(() -> "--allow-insecure", "safaridriver_autodownload");
                break;
            default:
                logger.fatal(Logger.getLogMessage("locale.browser.name.wrong").concat(" chrome, safari"));
        }

        try {
            service.set(AppiumDriverLocalService.buildService(builder));
            logger.info("Starting appium server...");
            service.get().start();
            logger.info("Appium server is up and running");
        } catch (Exception exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static void stopAppiumServerAndKillEmulator() {
        if (service.get() == null || !service.get().isRunning()) {
            return;
        }

        logger.info("Stopping appium server...");
        service.get().stop();
        logger.info("Server is stopped");

        service.remove();
        capabilities.remove();
        killEmulator();
    }

    public static boolean isAppiumServerAlive() {
        return service.get() != null;
    }

    public static URL getServiceUrl() {
        return service.get().getUrl();
    }

    private static void killEmulator() {
        logger.info("Killing launched emulator...");

        String platformName = getCapability("platformName").toLowerCase();
        String killCommand = platformName.equals("android") ? KILL_ANDROID_EMU : KILL_IOS_EMU;

        execCommand(killCommand);
        logger.info("Emulator is killed");
    }
}