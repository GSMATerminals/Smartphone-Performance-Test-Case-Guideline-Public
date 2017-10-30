package com.unicom.apptest;

import com.unicom.apptest.util.Helpers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
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
import static com.unicom.apptest.util.Helpers.for_find;

public class WeChatTest {
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
        capabilities.setCapability("name", "WeChatTest " + date);
        String userDir = System.getProperty("user.dir");
        URL serverAddress;
        String localApp = "wechat.apk";
        System.out.println("Trying install wechat.apk to phone...");
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

        List<WebElement> els = driver.findElementsByClassName("android.widget.Button");
        for (WebElement e : els) {
            if (e.getText().equals("登录")) {
                e.click();
                System.out.println("click 登录");
                WebElement el = driver.findElementById("com.tencent.mm:id/bun");
                if (el != null) {
                    System.out.println("find phonenumber EditText");
                    el.sendKeys("18600000000");
                } else {
                    System.out.println("find phonenumber EditText failed!");
                }
            }
        }
        driver.removeApp("com.tencent.mm");
    }


}