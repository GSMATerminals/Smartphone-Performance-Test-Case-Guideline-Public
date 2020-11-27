package com.unicom.apptest;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 一个suite(套件) 由一个或多个测试组成。
 * 一个test(测试) 由一个或多个类组成
 * 一个class(类) 由一个或多个方法组成。 ####
 *
 * @BeforeSuite/@AfterSuite 在某个测试套件开始之前/在某个套件所有测试方法执行之后
 * @BeforeTest/@AfterTest 在某个测试开始之前/在某个测试所有测试方法执行之后
 * @BeforeClass/@AfterClass 在某个测试类开始之前/在某个类的所有测试方法执行之后
 * @BeforeMethod/@AfterMethod 在某个测试方法之前/在某个测试方法执行之后
 * @BeforeGroup/@AfterGroup 在某个组的所有测试方法之前/在某个组的所有测试方法执行之后
 */


public abstract class AutoBase {
    /**
     * Keep the same date prefix to identify job sets.
     **/
    Date date = new Date();
    public static String sessionId;
    public static URL serverAddress;
    public static WebDriverWait driverWait;
    public static AndroidDriver androidDriver;

    public static WebEventListener eventListener;
    public static EventFiringWebDriver eDriver;
    //
    // must wait at least 60 seconds for running on Sauce.
    // waiting for 30 seconds works locally however it fails on Sauce.
    public static int timeoutInSeconds = 300;


    /**
     * @param PlatformVersion
     * @param DeviceName
     * @param WeChatNickName
     * @throws Exception
     */
    @BeforeSuite
    @Parameters({"PlatformVersion", "DeviceName", "WeChatNickName"})
    public void beforeSuite(@Optional("6.0") String PlatformVersion, @Optional("Android") String DeviceName, @Optional("mine") String WeChatNickName) throws Exception {
        this.PlatformVersion = PlatformVersion;
        this.DeviceName = DeviceName;
        this.WeChatNickName = WeChatNickName;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.6.5");
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.tencent.mm");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.tencent.mm.ui.LauncherUI");

        //A new session could not be created的解决方法
        capabilities.setCapability(MobileCapabilityType.APP_WAIT_ACTIVITY, "com.tencent.mm.ui.LauncherUI");


        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        capabilities.setCapability("sessionOverride", true);
        //实现中文输入
        capabilities.setCapability("unicodeKeyboard", "True");  //使用unicode编码方式发送字符串
        capabilities.setCapability("resetKeyboard", "True");    //将键盘隐藏起来
        //目标系统和设备配置
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, PlatformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DeviceName);

        //set apk path
        String userDir = System.getProperty("user.dir");
        String localApp = APP_APK_FILE_NAME;
        System.out.println("Trying install WeiXin to phone...");
        String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
        capabilities.setCapability(MobileCapabilityType.APP, appPath);


        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");
        androidDriver = new AndroidDriver(serverAddress, capabilities);
        sessionId = androidDriver.getSessionId().toString();
        androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driverWait = new WebDriverWait(androidDriver, timeoutInSeconds);

        // Initializing EventFiringWebDriver using AndroidDriver instance
        eDriver = new EventFiringWebDriver(androidDriver);
        // Now create object of EventListerHandler to register it with EventFiringWebDriver
        eventListener = new WebEventListener();
        eDriver.register(eventListener);
    }


    public static String param1 = "";

    /**
     * auto test init
     **/
    @BeforeTest
    @Parameters({"param1"})
    public void beforeTest(@Optional("param1") String param1,
                           ) throws Exception {
        this.param1 = param1;
    }


    /**
     * auto test entry
     */
    @Test
    public void TestEntry() {
        List<WebElement> els = null;
        WebElement el = null;
        //TODO:add test body

    }

    @AfterSuite
    public void afterSuite() throws Exception {
        if (androidDriver != null) {
            androidDriver.quit();
            System.out.println("afterSuite-->androidDriver quit...");
        }
    }


}