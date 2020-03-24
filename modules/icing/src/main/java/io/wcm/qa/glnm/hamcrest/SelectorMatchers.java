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
package io.wcm.qa.glnm.hamcrest;

import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.baselineString;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.Matchers;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matchers around
 *
 * @since 5.0.0
 */
public final class SelectorMatchers {

  private SelectorMatchers() {
    // do not instantiate
  }

  /**
   * Match Selenium WebElement attribute against baseline.
   *
   * @param name defines attribute to check
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   */
  public static Matcher<Selector> attribute(String name) {
    return attribute(name, baselineString());
  }

  /**
   * Match Selenium WebElement attribute with Hamcrest matcher.
   *
   * @param name defines attribute to check
   * @param matcher to check against baseline
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   */
  public static Matcher<Selector> attribute(String name, Matcher<String> matcher) {
    return element(Matchers.attribute(name, matcher));
  }

  /**
   * Match Selenium WebElement if it is displayed.
   *
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   */
  public static Matcher<Selector> displayed() {
    return element(Matchers.displayed());
  }

  /**
   * Match Selenium WebElement text with Hamcrest matcher.
   *
   * @param matcher to check against baseline
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   */
  public static Matcher<Selector> text(Matcher<String> matcher) {
    return element(Matchers.text(matcher));
  }

  /**
   * Match Selenium WebElement text against baseline.
   *
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   */
  public static Matcher<Selector> text() {
    return text(baselineString());
  }

  private static Matcher<Selector> element(Matcher<WebElement> matcher) {
    return new SelectorWebElementMatcher(matcher);
  }

}
