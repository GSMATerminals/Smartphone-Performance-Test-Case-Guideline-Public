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

  /**
   * Set implicit wait in seconds *
   */
  public static void setWait(int seconds) {
    driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
  }

  /**
   * Return an element by locator *
   */
  public static WebElement element(By locator) {
    return driver.findElement(locator);
  }

  /**
   * Return a list of elements by locator *
   */
  public static List<WebElement> elements(By locator) {
    return driver.findElements(locator);
  }

  /**
   * Press the back button *
   */
  public static void back() {
    driver.navigate().back();
  }

  /**
   * Return a list of elements by tag name *
   */
  public static List<WebElement> tags(String tagName) {
    return elements(for_tags(tagName));
  }

  /**
   * Return a tag name locator *
   */
  public static By for_tags(String tagName) {
    return By.className(tagName);
  }

}