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
package io.wcm.qa.glnm.selectors;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.AbstractSelectorBase;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Selector implementation immediately setting {@link By} and {@link Locator} values on instantiation.
 * This avoids harder to trace errors inherent in lazy evaluation used in other implementations.
 */
public class FixedValueSelector extends AbstractSelectorBase {

  private String elementName;

  /**
   * Uses the element name, selector CSS, {@link By}, and {@link Locator} from selector.
   * @param selector to extract values from
   */
  public FixedValueSelector(Selector selector) {
    this(selector.elementName(), selector.asString(), selector.asBy(), selector.asLocator());
  }

  /**
   * Uses the parameters as values.
   * @param elementName to use for selector
   * @param css to use for selector
   * @param by to use for selector
   * @param locator to use for selector
   */
  public FixedValueSelector(String elementName, String css, By by, Locator locator) {
    setBy(by);
    this.elementName = elementName;
    setLocator(locator);
    setString(css);
  }

  @Override
  public String elementName() {
    return elementName;
  }

}