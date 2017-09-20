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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.util.BrowserType.CHROME;
import static io.wcm.qa.galenium.util.BrowserType.FIREFOX;

import org.openqa.selenium.Dimension;

import com.galenframework.utils.GalenUtils;

/**
 * Collection of utility methods when dealing with browsers.
 */
public final class BrowserUtil {

  private BrowserUtil() {
    // do not instantiate
  }

  /**
   * Turn Galen syntax size string into Selenium {@link Dimension}.
   * @param size to parse
   * @return Selenium representation of size
   */
  public static Dimension getDimension(String size) {
    return new Dimension(GalenUtils.readSize(size).width, GalenUtils.readSize(size).height);
  }

  /**
   * @return whether current browser is Chrome
   */
  public static boolean isChrome() {
    return isBrowser(CHROME);
  }

  /**
   * @return whether current browser is Firefox
   */
  public static boolean isFirefox() {
    return isBrowser(FIREFOX);
  }

  private static boolean isBrowser(BrowserType browserType) {
    TestDevice testDevice = GaleniumContext.getTestDevice();
    if (testDevice == null) {
      return false;
    }
    return testDevice.getBrowserType() == browserType;
  }
}
