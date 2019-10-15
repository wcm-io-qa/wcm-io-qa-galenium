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
package io.wcm.qa.glnm.util;

import static io.wcm.qa.glnm.device.BrowserType.CHROME;
import static io.wcm.qa.glnm.device.BrowserType.FIREFOX;
import static io.wcm.qa.glnm.device.BrowserType.IE;

import io.wcm.qa.glnm.device.BrowserType;
import io.wcm.qa.glnm.device.TestDevice;

/**
 * Collection of utility methods when dealing with browsers.
 *
 * @since 1.0.0
 */
public final class BrowserUtil {

  private BrowserUtil() {
    // do not instantiate
  }

  /**
   * <p>isChrome.</p>
   *
   * @return whether current browser is Chrome
   * @since 3.0.0
   */
  public static boolean isChrome() {
    return isBrowser(CHROME);
  }

  /**
   * <p>isFirefox.</p>
   *
   * @return whether current browser is Firefox
   * @since 3.0.0
   */
  public static boolean isFirefox() {
    return isBrowser(FIREFOX);
  }

  /**
   * <p>isInternetExplorer.</p>
   *
   * @return whether current browser is Internet Explorer
   * @since 3.0.0
   */
  public static boolean isInternetExplorer() {
    return isBrowser(IE);
  }

  private static boolean isBrowser(BrowserType browserType) {
    TestDevice testDevice = GaleniumContext.getTestDevice();
    if (testDevice == null) {
      return false;
    }
    return testDevice.getBrowserType() == browserType;
  }
}
