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

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

/**
 * Base class for the different {@link Selector} implementations.
 */
public abstract class AbstractSelectorBase implements Selector {

  private static final String REGEX_NAME_CLEANING = "[^a-zA-Z0-9.]+";

  private By by;
  private Locator locator;
  private String name;
  private String string;

  @Override
  public By asBy() {
    if (getBy() == null) {
      return By.cssSelector(getString());
    }
    return getBy();
  }

  @Override
  public Locator asLocator() {
    if (getLocator() == null) {
      setLocator(Locator.css(getString()));
    }
    return getLocator();
  }

  @Override
  public String asString() {
    return getString();
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
    return nameBase.replaceAll(REGEX_NAME_CLEANING, "-");
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return elementName() + "(" + asString() + ")";
  }

  protected By getBy() {
    return by;
  }

  protected Locator getLocator() {
    return locator;
  }

  protected String getString() {
    return string;
  }

  protected void setBy(By by) {
    this.by = by;
  }

  protected void setLocator(Locator locator) {
    this.locator = locator;
  }

  protected void setName(String name) {
    this.name = name;
  }

  protected void setString(String string) {
    this.string = string;
  }

}
