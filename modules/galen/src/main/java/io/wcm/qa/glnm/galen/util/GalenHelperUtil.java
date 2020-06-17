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
package io.wcm.qa.glnm.galen.util;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getMediaQueryHeight;
import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getCurrentDriver;

import org.openqa.selenium.Dimension;

import com.galenframework.browser.Browser;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.galenframework.utils.GalenUtils;

import io.wcm.qa.glnm.context.GaleniumContext;

/**
 * Helper methods for dealing with Galen.
 *
 * @since 1.0.0
 */
public final class GalenHelperUtil {

  private GalenHelperUtil() {
    // do not instantiate
  }

  /**
   * <p>adjustViewport.</p>
   *
   * @param adjustBrowserViewportSize a boolean.
   * @since 4.0.0
   */
  public static void adjustViewport(boolean adjustBrowserViewportSize) {
    GalenConfig.getConfig().setProperty(
        GalenProperty.GALEN_BROWSER_VIEWPORT_ADJUSTSIZE,
        Boolean.toString(adjustBrowserViewportSize));
  }

  /**
   * <p>getBrowser.</p>
   *
   * @return a {@link com.galenframework.browser.Browser} object.
   * @since 4.0.0
   */
  public static Browser getBrowser() {
    return new SeleniumBrowser(GaleniumContext.getDriver());
  }

  /**
   * Turn Galen syntax size string into Selenium {@link org.openqa.selenium.Dimension}.
   *
   * @param size to parse
   * @return Selenium representation of size
   * @since 4.0.0
   */
  public static Dimension getDimension(String size) {
    java.awt.Dimension parsedSize = GalenUtils.readSize(size);
    return new Dimension(parsedSize.width, parsedSize.height);
  }

  /**
   * Resize the viewport.
   *
   * @param size to set viewport to
   * @since 5.0.0
   */
  public static void resizeViewport(Dimension size) {
    GalenUtils.resizeDriver(getCurrentDriver(), size.getWidth(), size.getHeight());
  }

  /**
   * Resize the viewport.
   *
   * @param width to set viewport to
   * @since 5.0.0
   */
  public static void resizeViewport(int width) {
    resizeViewport(new Dimension(width, getMediaQueryHeight()));
    ;
  }

}
