package com.unicom.apptest;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;


public class WangKaZhuShou extends MPBase {


    @BeforeSuite
    @Parameters({"PlatformVersion", "DeviceName", "WeChatNickName", "MaxWaitTime", "Count", "Interval"})
    public void beforeSuite(@Optional("6.0") String PlatformVersion, @Optional("Android") String DeviceName, @Optional("mine") String WeChatNickName, @Optional("180") String MaxWaitTime, @Optional("1") String Count, @Optional("30") String Interval) {

        this.PlatformVersion = PlatformVersion;
        this.DeviceName = DeviceName;
        this.WeChatNickName = WeChatNickName;
        this.MaxWaitTime = Integer.parseInt(MaxWaitTime);
        this.Count = Integer.parseInt(Count);
        this.Interval = Integer.parseInt(Interval);

        //初始化公众号参数
        MPName = "王卡助手";      //被测试公众号名称
        ClearScreenText = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n天王盖地虎";
        StartText1 = "排队";   //进入人工客服方式
        CustomerServiceName = "微臣";   //人工客服名称
        EndText1 = "没事了,搞定了,谢谢!";   //结束消息1
        EndText2 = "打扰了,谢谢!";   //结束消息2
        FindMPMaxRetryTimes = 5; //查找公众号偶尔错误,所以允许重新查找,这里定义最大重试次数

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


}