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

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.selectors.FixedValueNestedSelector;
import io.wcm.qa.glnm.selectors.FixedValueSelector;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.SelectorFromString;

/**
 * Creates Selectors for use with Galenium.
 */
public final class SelectorFactory {

  private SelectorFactory() {
    // do not instantiate
  }

  /**
   * @param selectorString CSS selector
   * @return Galenium selector representing the CSS selector
   */
  public static Selector fromCss(String selectorString) {
    return new SelectorFromString(selectorString);
  }

  /**
   * @param elementName name to use for selector
   * @param selectorString CSS selector
   * @return Galenium selector representing the CSS selector
   */
  public static Selector fromCss(String elementName, String selectorString) {
    AbstractSelectorBase selector = new SelectorFromString(selectorString);
    selector.setName(elementName);
    return selector;
  }

  /**
   * @param locator to construct selector from
   * @return Galenium selector representing the locator
   */
  public static SelectorFromLocator fromLocator(Locator locator) {
    checkLocator(locator);
    return new SelectorFromLocator(locator);
  }

  /**
   * @param elementName alternative name to use in Selector
   * @param locator to construct selector from
   * @return Galenium selector representing the locator
   */
  public static SelectorFromLocator fromLocator(String elementName, Locator locator) {
    checkLocator(locator);
    return new SelectorFromLocator(elementName, locator);
  }

  /**
   * @param sourceSelector to take values from
   * @return new selector with values identical to source selector
   */
  public static NestedSelector fromSelector(NestedSelector sourceSelector) {
    return new FixedValueNestedSelector(sourceSelector);
  }

  /**
   * @param sourceSelector to take values from
   * @return new selector with values identical to source selector
   */
  public static Selector fromSelector(Selector sourceSelector) {
    return new FixedValueSelector(sourceSelector);
  }

  /**
   * @param elementName element name
   * @param css CSS value
   * @param by Selenium {@link By}
   * @param locator Galen {@link Locator}
   * @return a {@link NestedSelector} built using the passed values
   */
  public static Selector fromValues(String elementName, String css, By by, Locator locator) {
    return new FixedValueSelector(elementName, css, by, locator);
  }

  /**
   * @param elementName element name
   * @param css CSS value
   * @param by Selenium {@link By}
   * @param locator Galen {@link Locator}
   * @param absolute absolute version integrating CSS from ancestors
   * @param relative relative version with CSS relative to direct parent
   * @param parent parent selector
   * @param children child selectors
   * @return a {@link NestedSelector} built using the passed values
   */
  public static NestedSelector fromValues(String elementName, String css, By by, Locator locator, Selector absolute,
      Selector relative, NestedSelector parent, Collection<NestedSelector> children) {
    return new FixedValueNestedSelector(elementName, css, by, locator, absolute, relative, parent, children);
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param relativeSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static Selector relativeToAbsolute(Selector parent, Selector relativeSelector) {
    return relativeToAbsolute(parent, relativeSelector.elementName(), relativeSelector.asString());
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param relativeCssSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static Selector relativeToAbsolute(Selector parent, String relativeCssSelector) {
    return relativeToAbsolute(parent, "child", relativeCssSelector);
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param childName to use in construction of name of relative selector
   * @param relativeCssSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static Selector relativeToAbsolute(Selector parent, String childName, String relativeCssSelector) {
    String selectorString = parent.asString() + " " + relativeCssSelector;
    String parentName = parent.elementName();
    String elementName;
    if (StringUtils.startsWith(childName, parentName)) {
      elementName = childName;
    }
    else {
      elementName = parentName + "." + childName;
    }
    return fromCss(elementName, selectorString);
  }

  private static void checkLocator(Locator locator) {

    switch (locator.getLocatorType()) {
      case "css":
        // standard case
        return;

      case "id":
        // can be transformed into standard case
        locator.setLocatorType("css");
        locator.setLocatorValue("#" + locator.getLocatorValue());
        return;

      default:
        throw new GaleniumException("unsupported locator type: '" + locator.getLocatorType() + "'");
    }
  }
}
