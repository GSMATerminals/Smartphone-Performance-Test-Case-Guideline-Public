package com.unicom.apptest;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Auto Test for YiZhiBo app.
 */
public class YiZhiBoTest {
    /**
     * Keep the same date prefix to identify job sets.
     **/
    Date date = new Date();
    String sessionId;
    private URL serverAddress;
    private WebDriverWait driverWait;
    private AndroidDriver androidDriver;
    private WebEventListener eventListener;
    private EventFiringWebDriver eDriver;


    /**
     * Run before each test
     **/
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.1.0");
//        capabilities.setCapability("name", "YiZhiBoTest " + date);
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");


        //set apk path
        String userDir = System.getProperty("user.dir");
        String localApp = "YiZhiBoV1.8.3.apk";
        System.out.println("Trying install YiZhiBo to phone...");
        String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
        capabilities.setCapability(MobileCapabilityType.APP, appPath);

        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "tv.xiaoka.live");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.yixia.live.activity.SplashNewActivity");

        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");
        androidDriver = new AndroidDriver(serverAddress, capabilities);
        sessionId = androidDriver.getSessionId().toString();
        androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        int timeoutInSeconds = 60;
        // must wait at least 60 seconds for running on Sauce.
        // waiting for 30 seconds works locally however it fails on Sauce.
        driverWait = new WebDriverWait(androidDriver, timeoutInSeconds);

        // Initializing EventFiringWebDriver using AndroidDriver instance
        eDriver = new EventFiringWebDriver(androidDriver);
        // Now create object of EventListerHandler to register it with EventFiringWebDriver
        eventListener = new WebEventListener();
        eDriver.register(eventListener);


    }

    /**
     * Run after each test
     **/
    @After
    public void tearDown() throws Exception {
        if (eDriver != null) {
            System.out.println(androidDriver.getCapabilities().getCapability("name") + " finished!");
            eDriver.quit();
        }
    }


    @org.junit.Test
    public void TestEntry() {
        List<WebElement> els = null;
        WebElement el = null;

        //find ad skip button
        System.out.println("find  【skip】");
        els = eDriver.findElements(By.className("android.widget.Button"));
        for (WebElement e : els) {
            if (e.getText().equals("跳过")) {
                e.click();
                System.out.println("click 【skip】");
                break;
            }
        }

        new PlayThread().start();
        WaitNSecond(1000);
    }


    /**
     * click “home”->“hot” and start to play online video
     */
    public void startPlay() {
        List<WebElement> els = null;
        WebElement el = null;
        try {
            System.out.println("find【home】");
            els = eDriver.findElements(By.className("android.widget.TextView"));
            for (WebElement e : els) {
                if (e.getText().equals("首页")) {
                    e.click();
                    System.out.println("click 【home】");
                    break;
                }
            }
            el = androidDriver.findElementById("tv.xiaoka.live:id/photo_iv");
            if (el != null) {
                el.click();
                System.out.println("click 【play】");
                WaitNSecond(10);
                androidDriver.navigate().back();
                System.out.println("navigate【back】");
            }
        } catch (NoSuchElementException e) {
            System.out.println("not find【home】");
        }
    }

    class PlayThread extends Thread {
        public PlayThread() {
        }

        public void run() {
            while (true) {
                startPlay();
                WaitNSecond(1);
            }
        }
    }

    private void WaitNSecond(int n) {
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class WebEventListener implements WebDriverEventListener {
        /**
         * close permission popup
         */
        public boolean CheckPermissionPopup() {
            List<WebElement> els = null;
            WebElement el = null;
            System.out.println("find【permission】");
            try {
                el = androidDriver.findElementById("com.android.packageinstaller:id/permission_message");
                if (el != null) {
                    el = androidDriver.findElementById("com.android.packageinstaller:id/permission_allow_button");
                    if (el != null) {
                        el.click();
                        System.out.println("click 【allow permission】");
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("not find【permission】");
            }
            return false;
        }


        /**
         * auto login
         */
        public boolean CheckLogin() {
            List<WebElement> els = null;
            WebElement el = null;
            System.out.println("find【login】 ");
            try {
                el = androidDriver.findElementById("tv.xiaoka.live:id/btn_login_by_phone");
                if (el != null) {
                    el.click();
                    System.out.println("click login");
                    //auto input phone number and password
                    el = androidDriver.findElementById("tv.xiaoka.live:id/phone_edit");
                    if (el != null) {
                        System.out.println("find phone_edit");
                        el.sendKeys("18600000000");
                        el = androidDriver.findElementById("tv.xiaoka.live:id/password_edit");
                        if (el != null) {
                            System.out.println("find password_edit");
                            el.sendKeys("password");
                            el = androidDriver.findElementById("tv.xiaoka.live:id/tv_login_status");
                            if (el != null) {
                                System.out.println("click 【login】");
                                el.click();
                                return true;
                            }
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("未找到【手机号登陆】");
            }
            return false;
        }

        /**
         * close vip popup
         */
        public boolean CheckVIPPopup() {
            List<WebElement> els = null;
            WebElement el = null;
            //首页
            System.out.println("find【VIP】");
            try {
                el = androidDriver.findElementById("tv.xiaoka.live:id/medal_btn_text");
                if (el != null) {
                    el = androidDriver.findElementById("tv.xiaoka.live:id/medal_close");
                    if (el != null) {
                        el.click();
                        System.out.println("close【VIP】");
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("not find【VIP】");
            }
            return false;
        }

        /**
         * close update popup
         */
        public boolean CheckUpdatePopup() {
            List<WebElement> els = null;
            WebElement el = null;
            //首页
            System.out.println("find【update】");
            try {
                el = androidDriver.findElementById("android:id/alertTitle");
                if (el != null) {
                    el = androidDriver.findElementById("android:id/button2");
                    if (el != null) {
                        el.click();
                        System.out.println("click 【ignore】");
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("not find【update】");
            }
            return false;
        }

        /**
         * close sign popup
         */
        public boolean CheckSignPopup() {
            List<WebElement> els = null;
            WebElement el = null;
            System.out.println("find【sign】");
            try {
                el = androidDriver.findElementById("tv.xiaoka.live:id/sign_btn");
                if (el != null) {
                    el = androidDriver.findElementById("tv.xiaoka.live:id/tv_isshow");
                    if (el != null) {
                        el.click();
                        System.out.println("close 【sign】");
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("not find 【sign】");
            }
            return false;
        }

        /**
         * close gift popup
         */
        public boolean CheckHongbao() {
            List<WebElement> els = null;
            WebElement el = null;
            System.out.println("find【gift】");
            try {
                el = androidDriver.findElementById("tv.xiaoka.live:id/iv_close");
                if (el != null) {
                    el.click();
                    System.out.println("close 【gift】");
                    return true;
                }
            } catch (NoSuchElementException e) {
                System.out.println("not find 【gift】");
            }
            return false;
        }

        public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
            System.out.println("beforeFindBy:" + by.toString());
            if (CheckSignPopup()) return;
            if (CheckVIPPopup()) return;
            if (CheckHongbao()) return;
            if (CheckLogin()) return;
            if (CheckPermissionPopup()) return;
            if (CheckUpdatePopup()) return;
        }

        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("afterFindBy:" + by.toString());
        }

        public void beforeNavigateTo(String url, WebDriver driver) {
            System.out.println("beforeNavigateTo:'" + url + "'");
        }

        public void afterNavigateTo(String url, WebDriver driver) {
            System.out.println("afterNavigateTo:'" + url + "'");
        }

        public void beforeChangeValueOf(WebElement element, WebDriver driver) {
            System.out.println("beforeChangeValueOf:" + element.toString());
        }

        public void afterChangeValueOf(WebElement element, WebDriver driver) {
            System.out.println("afterChangeValueOf:" + element.toString());
        }

        public void beforeClickOn(WebElement element, WebDriver driver) {
            System.out.println("beforeClickOn:" + element.toString());
        }

        public void afterClickOn(WebElement element, WebDriver driver) {
            System.out.println("afterClickOn:" + element.toString());
        }

        public void beforeNavigateBack(WebDriver driver) {
            System.out.println("beforeNavigateBack");
        }

        public void afterNavigateBack(WebDriver driver) {
            System.out.println("afterNavigateBack");
        }

        public void beforeNavigateForward(WebDriver driver) {
            System.out.println("beforeNavigateForward");
        }

        public void afterNavigateForward(WebDriver driver) {
            System.out.println("afterNavigateForward");
        }


        public void onException(Throwable error, WebDriver driver) {
            System.out.println("onException: " + error);
        }


        public void beforeScript(String script, WebDriver driver) {
            System.out.println("beforeScript: " + script);
        }

        public void afterScript(String script, WebDriver driver) {
            System.out.println("afterScript: " + script);
        }

    }


}