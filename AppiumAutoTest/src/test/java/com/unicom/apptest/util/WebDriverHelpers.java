package com.unicom.apptest.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;


public abstract class WebDriverHelpers {

  public static AndroidDriver driver;
  public static URL serverAddress;
  private static WebDriverWait driverWait;

  /**
   * Initialize the webdriver. Must be called before using any helper methods. *
   */
  public static void init(AndroidDriver webDriver, URL driverServerAddress) {
    driver = webDriver;
    serverAddress = driverServerAddress;
    int timeoutInSeconds = 60;
    // must wait at least 60 seconds for running on Sauce.
    // waiting for 30 seconds works locally however it fails on Sauce.
    driverWait = new WebDriverWait(webDriver, timeoutInSeconds);
  }

}