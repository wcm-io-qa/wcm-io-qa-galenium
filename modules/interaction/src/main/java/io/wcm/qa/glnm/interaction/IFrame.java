/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.glnm.interaction;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility methods for switching IFrames.
 */
public final class IFrame {

  private IFrame() {
    // do not instantiate
  }

  /**
   * @param selector identifies IFrame to switch to
   */
  public static void switchTo(Selector selector) {
    getLogger().info("switching to IFrame: " + selector);
    switchTo(Element.findOrFailNow(selector));
  }

  /**
   * @param iFrameElement element to switch to
   */
  public static void switchTo(WebElement iFrameElement) {
    getLogger().info("switching to IFrame element: " + iFrameElement);
    switchTo().frame(iFrameElement);
  }

  /**
   * Switch to default content.
   */
  public static void switchToDefault() {
    getLogger().info("switching to default IFrame.");
    switchTo().defaultContent();
  }

  /**
   * Switch to parent of current IFrame.
   */
  public static void switchToParent() {
    getLogger().info("switching to parent IFrame.");
    switchTo().parentFrame();
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  private static WebDriver getDriver() {
    return GaleniumContext.getDriver();
  }

  private static TargetLocator switchTo() {
    return getDriver().switchTo();
  }

}
