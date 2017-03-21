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
package io.wcm.qa.galenium.selectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

/**
 * Implementation of {@link Selector} interface.
 */
public class SelectorFromString implements Selector {

  private final By by;
  private final Locator locator;
  private String name;
  private final String string;

  /**
   * @param selectorString CSS selector
   */
  public SelectorFromString(String selectorString) {
    string = selectorString;
    locator = Locator.css(selectorString);
    by = By.cssSelector(selectorString);
  }

  @Override
  public By asBy() {
    return by;
  }

  @Override
  public Locator asLocator() {
    return locator;
  }

  @Override
  public String asString() {
    return this.string;
  }

  @Override
  public String elementName() {
    String nameBase;
    if (StringUtils.isNotBlank(getName())) {
      nameBase = getName();
    }
    else {
      nameBase = asString();
    }
    return nameBase.replaceAll("[^a-zA-Z0-9]+", "-");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}