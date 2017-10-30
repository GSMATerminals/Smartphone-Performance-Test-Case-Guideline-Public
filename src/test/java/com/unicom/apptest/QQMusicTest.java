package com.unicom.apptest;

import com.unicom.apptest.util.Helpers;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.unicom.apptest.util.Helpers.driver;

public class QQMusicTest {
    /**
     * Keep the same date prefix to identify job sets.
     **/
    Date date = new Date();
    String sessionId;

    /**
     * Run before each test
     **/
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.1.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("name", "QQMusicTest " + date);
        String userDir = System.getProperty("user.dir");
        URL serverAddress;
        String localApp = "QQMusic.apk";
        System.out.println("Trying install QQMusic.apk to phone...");
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


    @org.junit.Test
    public void testApp() throws Exception {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement el = driver.findElementByAccessibilityId("跳过");
        if (el != null) {
            System.out.println("findElement 跳过");
            el.click();
            el = driver.findElementByAccessibilityId("更多设置");
            if (el != null) {
                System.out.println("findElement 更多设置");
                el.click();
                List<WebElement> els = driver.findElementsByClassName("android.widget.TextView");
                for (WebElement e : els) {
                    if (e.getText().equals("关闭")) {
                        e.click();
                        System.out.println("click 关闭");
                        break;
                    }
                }
            } else {
                System.out.println("findElement 更多设置 failed!");
            }
        } else {
            System.out.println("findElement 跳过 failed!");
        }
        driver.removeApp("com.tencent.qqmusic");
    }

}