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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Methods for grabbing pixels from Browser.
 *
 * @since 1.0.0
 */
public final class Screenshot {

  private static final Logger LOG = LoggerFactory.getLogger(Screenshot.class);

  private Screenshot() {
    // do not instantiate
  }

  /**
   * Takes screenshot of element.
   *
   * @param selector identifies element
   * @since 4.0.0
   */
  public static void takeScreenshot(Selector selector) {
    WebElement element = Element.findOrFail(selector);
    Element.scrollTo(element);
    GaleniumReportUtil.takeScreenshot(element);
  }

  /**
   * Takes screenshot of element.
   *
   * @param selector identifies element
   * @param index identifies which instance
   * @since 4.0.0
   */
  public static void takeScreenshotOfNth(Selector selector, int index) {
    List<WebElement> allElements = Element.findAll(selector);
    if (allElements.isEmpty()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("could not take screenshot of '" + selector + "[" + index + "]': no elements found");
      }
      return;
    }
    if (allElements.size() <= index) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("could not take screenshot of '" + selector + "[" + index + "]': only found " + allElements.size() + " instances");
      }
      return;
    }
    WebElement element = allElements.get(index);
    Element.scrollTo(element);
    GaleniumReportUtil.takeScreenshot(element);
  }

}
