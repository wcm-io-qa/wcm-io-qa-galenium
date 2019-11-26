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

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Utility methods for interaction with web form elements.
 *
 * @since 1.0.0
 */
public final class FormElement {

  private static final Logger LOG = LoggerFactory.getLogger(FormElement.class);

  private FormElement() {
    // do not instantiate
  }

  /**
   * Enters text into element which replaces any text that might already be in element.
   *
   * @param selector identifies the element
   * @param text value to enter
   * @since 1.0.0
   */
  public static void clearAndEnterText(Selector selector, String text) {
    WebElement input = Element.findOrFail(selector);
    try {
      input.clear();
    }
    catch (InvalidElementStateException ex) {
      LOG.debug("could not clear element: '" + selector + "'");
    }
    input.sendKeys(text);
  }

}
