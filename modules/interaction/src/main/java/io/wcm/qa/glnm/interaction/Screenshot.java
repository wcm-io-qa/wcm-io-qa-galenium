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

import java.util.List;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Methods for grabbing pixels from Browser.
 */
public final class Screenshot {

  private Screenshot() {
    // do not instantiate
  }

  /**
   * Takes screenshot of element.
   * @param selector identifies element
   * @param index identifies which instance
   * @return message to log to report
   */
  public static String takeScreenshotOfNth(Selector selector, int index) {
    List<WebElement> allElements = Element.findAll(selector);
    if (allElements.isEmpty()) {
      return "could not take screenshot of '" + selector + "[" + index + "]': no elements found";
    }
    if (allElements.size() <= index) {
      return "could not take screenshot of '" + selector + "[" + index + "]': only found " + allElements.size() + " instances";
    }
    WebElement element = allElements.get(index);
    Element.scrollTo(element);
    return GaleniumReportUtil.takeScreenshot(element);
  }

  /**
   * Takes screenshot of element.
   * @param selector identifies element
   * @return message to log to report
   */
  public static String takeScreenshot(Selector selector) {
    WebElement element = Element.findOrFail(selector);
    Element.scrollTo(element);
    return GaleniumReportUtil.takeScreenshot(element);
  }
}
