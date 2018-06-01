package com.unicom.apptest.util;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.unicom.apptest.util.Helpers.driver;

/*Base class for testCase extend*/
public class AppiumTest {

    /**
     * Keep the same date prefix to identify job sets.
     **/
    public static Date date = new Date();
    String sessionId;

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }


    /**
     * wait wraps Helpers.wait
     **/
    public static WebElement wait(By locator) {
        return Helpers.wait(locator);
    }

    /**
     * Run before each test
     **/
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.1.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("name", "WeChatTest " + date);
        String userDir = System.getProperty("user.dir");
        URL serverAddress;
        String localApp = "WinXinV6.6.6.apk";
        System.out.println("Trying install WinXinV6.6.6.apk to phone...");
        {
            String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
            capabilities.setCapability("app", appPath);
            serverAddress = new URL("http://127.0.0.1:4723/wd/hub");
            driver = new AndroidDriver(serverAddress, capabilities);
        }
        sessionId = driver.getSessionId().toString();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Helpers.init(driver, serverAddress);
    }

    /**
     * Run after each test
     **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            System.out.println(driver.getCapabilities().getCapability("name") + " finished!");
            driver.quit();
        }
    }


    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  test: " + description.getMethodName());
        }

        protected void finished(Description description) {
            System.out.print("  finishe: " + description.getMethodName());
        }
    };
}