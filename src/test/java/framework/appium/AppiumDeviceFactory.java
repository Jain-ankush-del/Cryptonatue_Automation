package framework.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.naming.NamingException;

import static framework.appium.AppiumServer.getServiceUrl;
import static framework.appium.AppiumServer.startAppiumServer;

public abstract class AppiumDeviceFactory {

    private AppiumDeviceFactory() {}

    protected static final ThreadLocal<DesiredCapabilities> capabilities = ThreadLocal.withInitial(DesiredCapabilities::new);

    public static AppiumDriver setup(final String devicePlatform, final String deviceName, final String platformVersion) throws NamingException {
        String deviceNameForCmd = WordUtils.capitalize(deviceName).replace(" ", "_");
        //String deviceNameForCmd = deviceName;

        switch (devicePlatform.toLowerCase().trim()) {
            case "android":
                startAppiumServer(deviceNameForCmd, "Chrome");
                setAndroidCapabilities(deviceName, platformVersion);
                break;
            case "ios":
                startAppiumServer(deviceName, "safari");
                setIosCapabilities(deviceName, platformVersion);
                break;
            default:
                throw new NamingException();
        }

        return new AppiumDriver(getServiceUrl(), capabilities.get());
    }

    private static void setAndroidCapabilities(String deviceName, String platformVersion) {
        capabilities.get().setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.get().setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        //capabilities.get().setCapability("avd", deviceName);

        capabilities.get().setCapability("avd", WordUtils.capitalize(deviceName).replace(" ", "_"));
        //capabilities.get().setCapability("isHeadless", true);
        capabilities.get().setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        capabilities.get().setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.get().setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
       /* DesiredCapabilities caps = capabilities.get();

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        caps.setCapability("isHeadless", true);
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);

        caps.setCapability("avd", deviceName);
        caps.setCapability("browserName", "Chrome");
*/
        capabilities.get().setCapability("newCommandTimeout", 8000);

    }

    private static void setIosCapabilities(String deviceName, String platformVersion) {
        capabilities.get().setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.get().setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        capabilities.get().setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        //capabilities.get().setCapability("isHeadless", true);
        capabilities.get().setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.get().setCapability(MobileCapabilityType.DEVICE_NAME, WordUtils.capitalize(deviceName).replaceAll("(?i)iphone", "iPhone"));
    }

    public static String getCapability(String capability) {
        return capabilities.get().getCapability(capability).toString();
    }
}