package com.unicom.apptest;

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class IOSBasicInteractionsTest extends BaseTest {
    private IOSDriver<WebElement> driver;

    @BeforeTest
    public void setUp() throws IOException {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../apps");
        File app = new File(appDir.getCanonicalPath(), "TestApp.app.zip");

        String deviceName = System.getenv("IOS_DEVICE_NAME");
        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
        DesiredCapabilities capabilities = new DesiredCapabilities();

         /*
        'deviceName' capability only affects device selection if you run the test in a cloud
        environment or your run your test on a Simulator device. This combination of this value
        plus `platformVersion` capability value
        is used to select a proper Simulator if it already exists. Use `xcrun simctl list` command
        to list available Simulator devices.
        */
        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);

        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}
