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


public abstract class MPBase {
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
     * 微信版本号适配内容,当前版本:V6.7.3
     */
    public static String APP_APK_FILE_NAME = "WeiXinV6.7.3.apk";      //微信安装文件名称
    public static String ELEMENT_ID_GONGZHONGHAO = "com.tencent.mm:id/a2n";      //微信版本适配--查找“公众号”按钮
    public static String ELEMENT_ID_GONGZHONGHAO_CHUANGKOU = "com.tencent.mm:id/j6"; //微信版本适配--查找公众号对话窗口标题
    public static String ELEMENT_ID_KEYBOARD = "com.tencent.mm:id/aiw"; //微信版本适配--查找键盘
    public static String ELEMENT_ID_TEXT_EDIT = "com.tencent.mm:id/aie";   //微信版本适配--查找编辑框
    public static String ELEMENT_ID_SEND_BUTTON = "com.tencent.mm:id/aik";     //微信版本适配--查找发送按钮
    public static String ELEMENT_ID_CUSTOMER_SERVICE_LABLE = "com.tencent.mm:id/mp";   //微信版本适配--查找人工客服标签


  /*  *//**
     * 微信版本号适配内容,当前版本:V6.7.2
     *//*
    public static String APP_APK_FILE_NAME = "WeiXinV6.7.2.apk";      //微信安装文件名称
    public static String ELEMENT_ID_GONGZHONGHAO = "com.tencent.mm:id/a01";      //微信版本适配--查找“公众号”按钮
    public static String ELEMENT_ID_GONGZHONGHAO_CHUANGKOU = "com.tencent.mm:id/j1"; //微信版本适配--查找公众号对话窗口标题
    public static String ELEMENT_ID_KEYBOARD = "com.tencent.mm:id/af7"; //微信版本适配--查找键盘
    public static String ELEMENT_ID_TEXT_EDIT = "com.tencent.mm:id/aep";   //微信版本适配--查找编辑框
    public static String ELEMENT_ID_SEND_BUTTON = "com.tencent.mm:id/aev";     //微信版本适配--查找发送按钮
    public static String ELEMENT_ID_CUSTOMER_SERVICE_LABLE = "com.tencent.mm:id/ly";   //微信版本适配--查找人工客服标签*/
    // TODO:(注意:王卡助手公众号的人工客服已经不能通过标签进行查找,人工客服不在显示客服名称和头像)

    /**
     * 配置文件传入微信测试全局参数,beforeSuite中使用;
     */
    public static String PlatformVersion;      //Android操作系统版本
    public static String DeviceName;      //设备名称（即设备序列号）
    public static String WeChatNickName;  //微信号昵称


    /**
     * 配置文件传入每个公众号测试参数,beforeClass中使用;
     */

    public static String MPName = "公众号名称";      //被测试公众号名称
    public static String StartCustomerServiceText = "排队";   //进入人工客服方式
    public static String CustomerServiceName = "微臣";   //人工客服名称
    public static String EndText1 = "没事了,搞定了,谢谢!";   //结束消息1
    public static String EndText2 = "打扰了,谢谢!";   //结束消息2
    public static int FindMPMaxRetryTimes = 5; //查找公众号偶尔错误,所以允许重新查找,这里定义最大重试次数
    public static int ResponseMaxWaitTime = 180;   //客服响应最大等待实际


    /**
     * 全局参数初始化,
     * 例如外部参数--Android版本号,设备名称,微信昵称,最大等待时间,循环次数,循环间隔等
     * 内部参数--appium版本号,微信版本号,微信包名称,微信activity名称等待
     *
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

    @AfterSuite
    public void afterSuite() throws Exception {
        if (androidDriver != null) {
            androidDriver.quit();
            System.out.println("afterSuite-->androidDriver quit...");
        }
    }


    /**
     * 完成每个被测公众号内部初始化
     **/
    @BeforeTest
    @Parameters({"MPName", "StartCustomerServiceText", "CustomerServiceName", "EndText1", "EndText2", "FindMPMaxRetryTimes", "ResponseMaxWaitTime"})
    public void beforeTest(@Optional("MPName") String MPName,
                           @Optional("启动人工客服") String StartCustomerServiceText,
                           @Optional("客服名称") String CustomerServiceName,
                           @Optional("EndText1") String EndText1,
                           @Optional("EndText2") String EndText2,
                           @Optional("5") String FindMPMaxRetryTimes,
                           @Optional("300") String ResponseMaxWaitTime
                           ) throws Exception {
        //初始化公众号参数
        this.MPName = MPName;      //被测试公众号名称
        this.StartCustomerServiceText = StartCustomerServiceText;       //进入人工客服方式
        this.CustomerServiceName = CustomerServiceName;       //人工客服名称
        this.EndText1 = EndText1;     //结束消息1
        this.EndText2 = EndText2;      //结束消息2
        this.FindMPMaxRetryTimes = Integer.parseInt(FindMPMaxRetryTimes);   //查找公众号偶尔错误,所以允许重新查找,这里定义最大重试次数
        this.ResponseMaxWaitTime = Integer.parseInt(ResponseMaxWaitTime);   //客服响应最大等待时间
    }


    @Test
    public void TestEntry() {
        List<WebElement> els = null;
        WebElement el = null;
        startAutoChat();
//        WaitNSecond(60); //TODO:两次时间间隔不能超过60S问题原因
    }


    /**
     * 点击“通讯录”->“公众号”->具体公众号
     */
    public void startAutoChat() {
        List<WebElement> els = null;
        WebElement el = null;
//        System.out.println("startAutoChat-->className:" + Thread.currentThread().getStackTrace()[3].getClassName());
//            System.out.println("androidDriver-->" + androidDriver.toString());
//            System.out.println("eDriver-->" + eDriver.toString());
        try {
            System.out.println("find【通讯录】");
            if (eDriver == null) {
                System.out.println("error eDriver==null!");
            } else {
                els = eDriver.findElements(By.className("android.widget.TextView"));
            }
            for (WebElement e : els) {
                if (e.getText().equals("通讯录")) {
                    e.click();
                    System.out.println("click 通讯录");
                    el = androidDriver.findElementById(ELEMENT_ID_GONGZHONGHAO);
                    if (el != null) {
                        System.out.println("click 公众号");
                        el.click();
                        WaitNSecond(3);
                        findMPName(MPName);
                        for (int i = 0; i < FindMPMaxRetryTimes; i++) {
                            if (checkMPName(MPName)) {
                                findKeyBoard();
                                sendClearScreenText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n天王盖地虎");
                                WaitNSecond(5);
                                sendStartText(StartCustomerServiceText);
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
        el = androidDriver.findElementById(ELEMENT_ID_GONGZHONGHAO_CHUANGKOU);
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
        el = androidDriver.findElementById(ELEMENT_ID_KEYBOARD);
        if (el != null) {
            el.click();
//            System.out.println("click keyboard");
            result = true;
        }
        return result;
    }

    private void sendClearScreenText(String text) {
        WebElement el = null;
        el = androidDriver.findElementById(ELEMENT_ID_TEXT_EDIT);    //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById(ELEMENT_ID_SEND_BUTTON);    //找发送按钮
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
        el = androidDriver.findElementById(ELEMENT_ID_TEXT_EDIT);    //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById(ELEMENT_ID_SEND_BUTTON); //找发送按钮
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
        el = androidDriver.findElementById(ELEMENT_ID_TEXT_EDIT);  //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById(ELEMENT_ID_SEND_BUTTON); //找发送按钮
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
        el = androidDriver.findElementById(ELEMENT_ID_TEXT_EDIT);  //找编辑框
        if (el != null) {
            el.sendKeys(text);
            el = androidDriver.findElementById(ELEMENT_ID_SEND_BUTTON); //找发送按钮
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
        while (!(getSeconds(startTime.getTime(), new Date().getTime()) > this.ResponseMaxWaitTime)) {
            List<String> stringArray = new ArrayList<String>();
            els = androidDriver.findElementsById(ELEMENT_ID_CUSTOMER_SERVICE_LABLE);   //查找客服名称
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