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
 * Matches {@link org.openqa.selenium.WebElement#getCssValue(String)}.
 *
 * @since 5.0.0
 */
public class ElementCssProperty extends WebElementMatcher<String> {

  protected ElementCssProperty(String name, Matcher<String> matcher) {
    super(
        "CSS value " + name,
        element -> element.getCssValue(name),
        matcher);
  }

  /**
   * <p>elementCssProperty.</p>
   *
   * @param name name of CSS property
   * @param matcher to match CSS value
   * @return {@link io.wcm.qa.glnm.hamcrest.element.ElementCssProperty} matcher
   */
  public static Matcher<WebElement> elementCssProperty(
      String name,
      Matcher<String> matcher) {
    return new ElementCssProperty(name, matcher);
  }
}
