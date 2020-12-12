package com.unicom.apptest;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class IOSSelectorsTest extends BaseTest {
    private IOSDriver<WebElement> driver;

    @BeforeSuite
    public void setUp() throws Exception {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../apps");
        File app = new File(appDir.getCanonicalPath(), "TestApp.app.zip");
        String deviceName = System.getenv("IOS_DEVICE_NAME");
        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);
        capabilities.setCapability("platformVersion", platformVersion == null ? "11.1" : platformVersion);
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("automationName", "XCUITest");
        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }

    /**
     * This finds elements by "accessibility id", which in the case of IOS is the "name" attribute of the element
     *
     */
    @Test
    public void testFindElementsByAccessibilityID () {
        List<WebElement> computeSumButtons = driver.findElementsByAccessibilityId("ComputeSumButton");
        Assert.assertEquals(computeSumButtons.size(), 1);
        computeSumButtons.get(0).click();
    }

    /**
     *  Find element by Class name
     */
    @Test
    public void testFindElementsByClassName () {
        List<WebElement> windowElements = driver.findElementsByClassName("XCUIElementTypeWindow");
        Assert.assertTrue(windowElements.size() > 1);
    };

    /**
     * Find  element by IOS Predicate selector strategy.
     *
     */
    @Test
    public void testFindElementsByNSPredicateString () {
        List<WebElement> allVisibleElements = driver.findElementsByIosNsPredicate("visible = true");
        Assert.assertTrue(allVisibleElements.size() > 1);
    };

    /**
     * Find  element by  IOS-specific selector strategy. Similar to XPath. This is recommended over XPath.
     */
    @Test
    public void testFindElementsByClassChain () {
        List<WebElement> windowElements = driver.findElementsByIosClassChain("XCUIElementTypeWindow[1]/*[2]");
        Assert.assertEquals(windowElements.size(), 1);
    };

    /**
     * Can find source xml by calling "driver.source()", XPath is not recommended due to major performance issues
     */
    @Test
    public void testFindElementsByXPath () {
        List<WebElement> buttons = driver.findElementsByXPath("//XCUIElementTypeWindow//XCUIElementTypeButton");
        Assert.assertTrue(buttons.size() > 1);
    };
}
