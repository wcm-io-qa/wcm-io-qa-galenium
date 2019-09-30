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

import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import org.openqa.selenium.NoAlertPresentException;

/**
 * Alert related convenience methods.
 */
public final class Alert {

  private Alert() {
    // do not instantiate
  }

  /**
   * Accepting alert popups in browser.
   * @return whether an alert was accepted
   */
  public static boolean accept() {
    if (Alert.isShowing()) {
      getDriver().switchTo().alert().accept();
      return true;
    }
    return false;
  }

  /**
   * @return whether browser is showing an alert popup.
   */
  public static boolean isShowing() {
    try {
      getDriver().switchTo().alert();
      return true;
    }
    catch (NoAlertPresentException e) {
      return false;
    }
  }

}
