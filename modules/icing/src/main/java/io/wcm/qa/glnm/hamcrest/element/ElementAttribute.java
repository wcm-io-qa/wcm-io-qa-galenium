/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.hamcrest.element;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

/**
 * Matches {@link org.openqa.selenium.WebElement#getAttribute(String)}
 *
 * @since 5.0.0
 */
public class ElementAttribute extends WebElementMatcher<String> {

  protected ElementAttribute(String name, Matcher<String> matcher) {
    super(
        name + " attribute",
        element -> element.getAttribute(name),
        matcher);
  }

  /**
   * <p>elementAttribute.</p>
   *
   * @param attributeName name of attribute
   * @param matcher used to match attribute value
   * @return matcher matching element's attribute value
   */
  public static Matcher<WebElement> elementAttribute(
      String attributeName,
      Matcher<String> matcher) {
    return new ElementAttribute(attributeName, matcher);
  }
}
