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


public class MPBase {
    /**
     * Keep the same date prefix to identify job sets.
     **/
    Date date = new Date();
    String sessionId;
    public URL serverAddress;
    public WebDriverWait driverWait;
    public AndroidDriver androidDriver;
    public WebEventListener eventListener;
    public EventFiringWebDriver eDriver;
    // must wait at least 60 seconds for running on Sauce.
    // waiting for 30 seconds works locally however it fails on Sauce.
    public int timeoutInSeconds = 180;


    /**
     * 外部传入配置参数:
     */
    public String PlatformVersion;      //Android操作系统版本
    public String DeviceName;      //设备名称（即设备序列号）
    public String WeChatNickName;  //微信号昵称
    public int MaxWaitTime = 180; //客服响应时间最大等待时间
    public Integer Count;      //测试次数
    public Integer Interval;      //两次测试之间的时间间隔


    /**
     * 该测试用例内部配置参数
     */

    public String MPName = "公众号名称";      //被测试公众号名称
    public String ClearScreenText = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n天王盖地虎";   //进入人工客服前清屏
    public String StartText1 = "排队";   //进入人工客服方式
    public String CustomerServiceName = "微臣";   //人工客服名称
    public String EndText1 = "没事了,搞定了,谢谢!";   //结束消息1
    public String EndText2 = "打扰了,谢谢!";   //结束消息2
    public int FindMPMaxRetryTimes = 5; //查找公众号偶尔错误,所以允许重新查找,这里定义最大重试次数


    @BeforeSuite
    @Parameters({"PlatformVersion", "DeviceName", "WeChatNickName", "MaxWaitTime", "Count", "Interval"})
    public void beforeSuite(@Optional("6.0") String PlatformVersion, @Optional("Android") String DeviceName, @Optional("mine") String WeChatNickName, @Optional("180") String MaxWaitTime, @Optional("1") String Count, @Optional("30") String Interval) {
        this.PlatformVersion = PlatformVersion;
        this.DeviceName = DeviceName;
        this.WeChatNickName = WeChatNickName;
        this.MaxWaitTime = Integer.parseInt(MaxWaitTime);
        this.Count = Integer.parseInt(Count);
        this.Interval = Integer.parseInt(Interval);
    }

    /**
     * Run before each test
     **/
    @BeforeClass
    public void beforeClass() throws Exception {
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
        String localApp = "WeiXinV6.6.7.apk";
        System.out.println("Trying install WeiXin to phone...");
        String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
        capabilities.setCapability(MobileCapabilityType.APP, appPath);


        URL serverAddress;
        serverAddress = new URL("http://127.0.0.1:4723/wd/hub");
        androidDriver = new AndroidDriver(serverAddress, capabilities);
        sessionId = androidDriver.getSessionId().toString();
        androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        if (this.Interval > timeoutInSeconds) {
            driverWait = new WebDriverWait(androidDriver, this.Interval);
        } else {
            driverWait = new WebDriverWait(androidDriver, timeoutInSeconds);
        }

        // Initializing EventFiringWebDriver using AndroidDriver instance
        eDriver = new EventFiringWebDriver(androidDriver);
        // Now create object of EventListerHandler to register it with EventFiringWebDriver
        eventListener = new WebEventListener();
        eDriver.register(eventListener);


    }

    @Test
    public void TestEntry() {
        List<WebElement> els = null;
        WebElement el = null;
        for (int i = 0; i < this.Count; i++) {
            startAutoChat();
//            TODO:两次时间间隔不能超过60S问题原因
            if (i > 0)
                WaitNSecond(this.Interval);
        }
    }

    /**
     * Run after each test
     **/
    @AfterClass
    public void afterClass() throws Exception {
        if (androidDriver != null) {
            androidDriver.quit();
//            System.out.println("afterClass quit...");
        }
    }


    /**
     * 点击“通讯录”->“公众号”->具体公众号
     */
    public void startAutoChat() {
        List<WebElement> els = null;
        WebElement el = null;
        try {
            System.out.println("find【通讯录】");
            els = eDriver.findElements(By.className("android.widget.TextView"));
            for (WebElement e : els) {
                if (e.getText().equals("通讯录")) {
                    e.click();
                    System.out.println("click 通讯录");
                    el = androidDriver.findElementById("com.tencent.mm:id/xs");
                    if (el != null) {
                        System.out.println("click 公众号");
                        el.click();
                        WaitNSecond(3);
                        findMPName(MPName);
                        for (int i = 0; i < FindMPMaxRetryTimes; i++) {
                            if (checkMPName(MPName)) {
                                findKeyBoard();
                                sendClearScreenText(ClearScreenText);
                                WaitNSecond(5);
                                sendStartText(StartText1);
                                break;
                            } else { //返回,重新查找公众号
                                WaitNSecond(3);
                                androidDriver.navigate().back();
                                WaitNSecond(3);
                                findMPName(MPName);
                            }
                        }
                    }
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("未找到:" + e.getMessage());
        }
    }

    public void findMPName(String mpName) {
        WebElement sEl = androidDriver.scrollToExact(mpName);
        if (sEl != null) {
            if (sEl.getText().trim().equals(mpName)) {
                sEl.click();
                System.out.println("click " + mpName);
                WaitNSecond(3);
            } else { //返回,重新查找公众号
                System.out.println("find " + mpName + " failed!");
            }
        } else {
            System.out.println("findMPName sEl==null!");
        }
    }

    private boolean checkMPName(String mpName) {
        List<WebElement> els = null;
        WebElement el = null;
        el = androidDriver.findElementById("com.tencent.mm:id/hm"); //查找公众号对话窗口标题,核对名称
        if (el != null) {
            if (el.getText().equals(mpName)) {
                return true;
            } else {
                System.out.println("error MPName:" + el.getText());
                return false;
            }
        }
        System.out.println("checkMPName 未找到:" + mpName);
        return false;
    }

    private boolean findKeyBoard() {
        List<WebElement> els = null;
        WebElement el = null;
        boolean result = false;
        el = androidDriver.findElementById("com.tencent.mm:id/acp"); //find keyboard V6.6.6:aas
        if (el != null) {
            el.click();
//            System.out.println("click keyboard");
            result = true;
        }
        return result;
    }

    private void sendClearScreenText(String text) {
        WebElement el = null;
        el = androidDriver.findElementById("com.tencent.mm:id/ac8");    //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById("com.tencent.mm:id/acd");    //找发送
            if (el != null) {
                el.click();
//                System.out.println(new Date().toString() + "sendClearScreenText:" + text);
//                Reporter.log(new Date().toString() + "sendClearScreenText:" + text + "\n");
            }
        }
    }

    private boolean sendStartText(String text) {
        List<WebElement> els = null;
        WebElement el = null;
        boolean result = false;
        el = androidDriver.findElementById("com.tencent.mm:id/ac8");    //找编辑框 V6.6.6:aaa
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById("com.tencent.mm:id/acd");    //找发送 V6.6.6:aag
            if (el != null) {
                el.click();
                WaitNSecond(2);
                System.out.println(new Date().toString() + "【" + MPName + "】" + text);
                Reporter.log(new Date().toString() + "【" + MPName + "】" + text + "\n");
                if (getResposeStatus()) {
                    //成功识别,答复客服并返回
                    sendEndText1(EndText1);
                    WaitNSecond(10);
                    sendEndText2(EndText2);
                    result = true;
                } else {
                    WaitNSecond(3);
                    androidDriver.navigate().back();
                    WaitNSecond(3);
                    androidDriver.navigate().back();
                }
            }
        }
        return result;
    }

    private void sendEndText1(String text) {
        WebElement el = null;
        el = androidDriver.findElementById("com.tencent.mm:id/ac8");    //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById("com.tencent.mm:id/acd");    //找发送
            if (el != null) {
                el.click();
                System.out.println(new Date().toString() + "sendEndText:" + text);
//                Reporter.log(new Date().toString() + "sendEndText:" + text + "\n");
            }
        }
    }

    private boolean sendEndText2(String text) {
        List<WebElement> els = null;
        WebElement el = null;
        boolean result = false;
        el = androidDriver.findElementById("com.tencent.mm:id/ac8");    //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById("com.tencent.mm:id/acd");    //找发送
            if (el != null) {
                el.click();
                System.out.println(new Date().toString() + "sendEndText:" + text);
//                Reporter.log(new Date().toString() + "sendEndText:" + text + "\n");
                WaitNSecond(3);
                androidDriver.navigate().back();
                WaitNSecond(3);
                androidDriver.navigate().back();
                result = true;
            }
        }
        return result;
    }

    private boolean getResposeStatus() {
        List<WebElement> els = null;
        WebElement el = null;
        boolean result = false;
        Date startTime = new Date();
        while (!(getSeconds(startTime.getTime(), new Date().getTime()) > MaxWaitTime)) {
            List<String> stringArray = new ArrayList<String>();
            els = androidDriver.findElementsById("com.tencent.mm:id/kg");    //查找客服名称 V6.6.6:jy
            if (els.size() > 0) {
                for (WebElement element : els) {
                    if (element.getText().contains(CustomerServiceName)) {
                        Date currentTime = new Date();
                        long diffSeconds = getSeconds(startTime.getTime(), currentTime.getTime());
                        System.out.println(currentTime.toString() + "【" + MPName + "】--" + element.getText() + "答复,等待时长:" + diffSeconds + "秒");
                        Reporter.log(currentTime.toString() + "【" + MPName + "】--" + element.getText() + "答复,等待时长:" + diffSeconds + "秒\n");
                        result = true;
                        return result;
                    } else {
                        System.out.println("未发现答复" + new Date().toString());
                    }

                }
            } else {
                System.out.println("未发现答复" + new Date().toString());
            }
            WaitNSecond(3);
        }
        System.out.println(new Date().toString() + "【" + MPName + "】等待超时:" + getSeconds(startTime.getTime(), new Date().getTime()) + "秒");
        Reporter.log(new Date().toString() + "【" + MPName + "】等待超时:" + getSeconds(startTime.getTime(), new Date().getTime()) + "秒\n");
        return result;
    }

    private long getSeconds(long startTime, long currentTime) {
        return (currentTime - startTime) / 1000;
    }


    public void WaitNSecond(int n) {
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class AutoChatThread extends Thread {
        public AutoChatThread() {
        }

        public void run() {
            for (int i = 0; i < 2; i++) {
                startAutoChat();
                WaitNSecond(10);
            }
            System.out.println("AutoChatThread exit...");
        }
    }

    public class WebEventListener implements WebDriverEventListener {
        /**
         * 权限许可
         */
        public boolean CheckPermissionPopup() {
            List<WebElement> els = null;
            WebElement el = null;
            //首页
            System.out.println("find【权限许可】");
            try {
                el = androidDriver.findElementById("com.android.packageinstaller:id/permission_message");
                if (el != null) {
                    el = androidDriver.findElementById("com.android.packageinstaller:id/permission_allow_button");
                    if (el != null) {
                        el.click();
                        System.out.println("click 【权限许可】");
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("未找到【权限许可】");
            }
            return false;
        }


        /**
         * 登陆（暂不支持首次短信验证码登陆,支持长时间未登陆失效的情况）
         */
        public boolean CheckLogin() {
            List<WebElement> els = null;
            WebElement el = null;
            //手机号登陆
            System.out.println("find【登陆】 ");
            try {
                els = androidDriver.findElementsByClassName("android.widget.Button");
                for (WebElement e : els) {
                    if (e.getText().equals("登录")) {
                        e.click();
                        System.out.println("click 登录");
                        el = androidDriver.findElementById("com.tencent.mm:id/hx");
                        if (el != null) {
                            System.out.println("find phonenumber EditText");
                            el.sendKeys("18600000000");
                            return true;
                        } else {
                            System.out.println("find phonenumber EditText failed!");
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("登陆失败");
            }
            return false;
        }


        public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
            MyLog("beforeFindBy:" + by.toString());
//            if (CheckLogin()) return;
//            if (CheckPermissionPopup()) return;
        }

        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            MyLog("afterFindBy:" + by.toString());
        }

        public void beforeNavigateTo(String url, WebDriver driver) {
            MyLog("beforeNavigateTo:'" + url + "'");
        }

        public void afterNavigateTo(String url, WebDriver driver) {
            MyLog("afterNavigateTo:'" + url + "'");
        }

        public void beforeChangeValueOf(WebElement element, WebDriver driver) {
            MyLog("beforeChangeValueOf:" + element.toString());
        }

        public void afterChangeValueOf(WebElement element, WebDriver driver) {
            MyLog("afterChangeValueOf:" + element.toString());
        }

        public void beforeClickOn(WebElement element, WebDriver driver) {
            MyLog("beforeClickOn:" + element.toString());
        }

        public void afterClickOn(WebElement element, WebDriver driver) {
            MyLog("afterClickOn:" + element.toString());
        }

        public void beforeNavigateBack(WebDriver driver) {
            MyLog("beforeNavigateBack");
        }

        public void afterNavigateBack(WebDriver driver) {
            MyLog("afterNavigateBack");
        }

        public void beforeNavigateForward(WebDriver driver) {
            MyLog("beforeNavigateForward");
        }

        public void afterNavigateForward(WebDriver driver) {
            MyLog("afterNavigateForward");
        }


        public void onException(Throwable error, WebDriver driver) {
            MyLog("onException: " + error);
        }


        public void beforeScript(String script, WebDriver driver) {
            MyLog("beforeScript: " + script);
        }

        public void afterScript(String script, WebDriver driver) {
            MyLog("afterScript: " + script);
        }

        public boolean ENABLE_LOG = false;

        public void MyLog(String text) {
            if (ENABLE_LOG) {
                System.out.println(text);
            }

        }

    }


}