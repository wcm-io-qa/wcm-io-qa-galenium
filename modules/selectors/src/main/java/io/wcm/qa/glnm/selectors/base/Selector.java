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
package io.wcm.qa.glnm.selectors.base;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

/**
 * Interface to represent selectors in Galenium. Selectors are used to identify elements in a page. This interface is
 * used throughout Galenium up to the final hand off to Galen (as {@link com.galenframework.specs.page.Locator}) or Selenium (as {@link org.openqa.selenium.By}).
 *
 * @since 1.0.0
 */
public interface Selector {

  /**
   * <p>asBy.</p>
   *
   * @return Selenium By object representing this selector
   */
  By asBy();

  /**
   * <p>asLocator.</p>
   *
   * @return Galen Locator representing this selector
   */
  Locator asLocator();

  /**
   * <p>asString.</p>
   *
   * @return String representing a CSS selector
   */
  String asString();

  /**
   * <p>elementName.</p>
   *
   * @return string representation that can be used as object name in Galen
   */
  String elementName();
}
