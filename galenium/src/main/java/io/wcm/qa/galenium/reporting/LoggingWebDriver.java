/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.qa.galenium.reporting;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

/**
 * Logging wrapper around {@link WebDriver}.
 */
public class LoggingWebDriver extends AbstractLoggingBase<WebDriver>
implements WebDriver, HasInputDevices, TakesScreenshot, JavascriptExecutor {

  private HasInputDevices inputDelegate;
  private TakesScreenshot screenshotDelegate;
  private JavascriptExecutor jsDelegate;

  /**
   * @param driver all method calls are delegated to this driver
   * @param logging all method calls will be logged
   */
  public LoggingWebDriver(WebDriver driver, GaleniumLogging logging) {
    super(driver, logging);
    if (driver instanceof HasInputDevices) {
      inputDelegate = (HasInputDevices)driver;
    }
    if (driver instanceof TakesScreenshot) {
      screenshotDelegate = (TakesScreenshot)driver;
    }
    if (driver instanceof JavascriptExecutor) {
      jsDelegate = (JavascriptExecutor)driver;
    }
  }

  @Override
  public void close() {
    debugStart("close");
    getDelegate().close();
    debugStop();
  }

  @Override
  public WebElement findElement(By arg0) {
    debugStart("findElement", arg0);
    WebElement findElement = getDelegate().findElement(arg0);
    debugStop(findElement);
    return findElement;
  }

  @Override
  public List<WebElement> findElements(By arg0) {
    debugStart("findElements", arg0);
    List<WebElement> findElements = getDelegate().findElements(arg0);
    debugStop(findElements);
    return findElements;
  }

  @Override
  public void get(String arg0) {
    debugStart("get", arg0);
    getDelegate().get(arg0);
    debugStop();
  }

  @Override
  public String getCurrentUrl() {
    debugStart("getCurrentUrl");
    String currentUrl = getDelegate().getCurrentUrl();
    debugStop(currentUrl);
    return currentUrl;
  }

  @Override
  public String getPageSource() {
    debugStart("getPageSource");
    String pageSource = getDelegate().getPageSource();
    debugStop(pageSource);
    return pageSource;
  }

  @Override
  public String getTitle() {
    debugStart("getTitle");
    String title = getDelegate().getTitle();
    debugStop(title);
    return title;
  }

  @Override
  public String getWindowHandle() {
    debugStart("getWindowHandle");
    String windowHandle = getDelegate().getWindowHandle();
    debugStop(windowHandle);
    return windowHandle;
  }

  @Override
  public Set<String> getWindowHandles() {
    debugStart("getWindowHandles");
    Set<String> windowHandles = getDelegate().getWindowHandles();
    debugStop(windowHandles);
    return windowHandles;
  }

  @Override
  public Options manage() {
    debugStart("manage");
    Options manage = getDelegate().manage();
    debugStop(manage);
    return new LoggingOptions(manage, loggingDelegate);
  }

  @Override
  public Navigation navigate() {
    debugStart("navigate");
    Navigation navigate = getDelegate().navigate();
    debugStop(navigate);
    return navigate;
  }

  @Override
  public void quit() {
    debugStart("quit");
    getDelegate().quit();
    debugStop();
  }

  @Override
  public TargetLocator switchTo() {
    debugStart("switchTo");
    TargetLocator switchTo = getDelegate().switchTo();
    debugStop(switchTo);
    return switchTo;
  }

  @Override
  public Keyboard getKeyboard() {
    checkInputDelegate();
    return inputDelegate.getKeyboard();
  }

  @Override
  public Mouse getMouse() {
    checkInputDelegate();
    return inputDelegate.getMouse();
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
    if (screenshotDelegate == null) {
      throw new IllegalArgumentException(screenshotDelegate + " does not implement TakesScreenshot");
    }
    debugStart("getScreenshotAs", arg0);
    X screenshotAs = screenshotDelegate.getScreenshotAs(arg0);
    debugStop(screenshotAs);
    return screenshotAs;
  }


  @Override
  public Object executeAsyncScript(String arg0, Object... arg1) {
    checkJsDelegate();
    debugStart("executeAsyncScript", arg0, arg1);
    Object executeAsyncScript = jsDelegate.executeAsyncScript(arg0, arg1);
    debugStop(executeAsyncScript);
    return executeAsyncScript;
  }

  @Override
  public Object executeScript(String arg0, Object... arg1) {
    checkJsDelegate();
    debugStart("executeScript", arg0, arg1);
    Object executeScript = jsDelegate.executeScript(arg0, arg1);
    debugStop(executeScript);
    return executeScript;
  }


  private void checkJsDelegate() {
    if (jsDelegate == null) {
      throw new IllegalArgumentException(jsDelegate + " does not implement JavascriptExecutor");
    }
  }

  private void checkInputDelegate() {
    if (inputDelegate == null) {
      throw new IllegalArgumentException(inputDelegate + " does not implement HasInputDevices");
    }
  }

}
