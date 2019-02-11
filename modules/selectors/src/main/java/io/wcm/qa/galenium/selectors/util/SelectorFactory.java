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
package io.wcm.qa.galenium.selectors.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.IndexedSelector;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.impl.DelegatingIndexedSelector;
import io.wcm.qa.galenium.selectors.impl.FixedValueNestedSelector;
import io.wcm.qa.galenium.selectors.impl.FixedValueSelector;
import io.wcm.qa.galenium.selectors.impl.NestedSelectorWrapper;
import io.wcm.qa.galenium.selectors.impl.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.impl.SelectorFromString;
import io.wcm.qa.galenium.selectors.impl.TrueIndexedSelector;

/**
 * Creates Selectors for use with Galenium.
 */
public final class SelectorFactory {

  private SelectorFactory() {
    // do not instantiate
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param relativeSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static NestedSelector absoluteFromRelative(Selector parent, Selector relativeSelector) {
    return absoluteFromRelative(parent, relativeSelector.elementName(), relativeSelector.asString());
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param relativeCssSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static Selector absoluteFromRelative(Selector parent, String relativeCssSelector) {
    return absoluteFromRelative(parent, "child", relativeCssSelector);
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param childName to use in construction of name of relative selector
   * @param relativeCssSelector relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static IndexedSelector absoluteFromRelative(Selector parent, String childName, String relativeCssSelector) {
    TrueIndexedSelector instance = new TrueIndexedSelector();

    NestedSelector nestedParent = nestedFromSelector(parent);
    instance.setParent(nestedParent);
    nestedParent.getChildren().add(instance);
    return instance;
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
   * @return new selector representing the CSS selector
   */
  public static Selector fromCss(String elementName, String selectorString) {
    return new SelectorFromString(elementName, selectorString);
  }

  /**
   * @param locator to construct selector from
   * @return new selector representing the locator
   */
  public static SelectorFromLocator fromLocator(Locator locator) {
    return fromLocator("", locator);
  }

  /**
   * @param elementName alternative name to use in Selector
   * @param locator to construct selector from
   * @return new selector representing the locator
   */
  public static SelectorFromLocator fromLocator(String elementName, Locator locator) {
    ensureCssLocator(locator);
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
   * @param parent parent selector
   * @param children child selectors
   * @return a {@link NestedSelector} built using the passed values
   */
  public static NestedSelector fromValues(String elementName, String css, By by, Locator locator, NestedSelector parent, Collection<NestedSelector> children) {
    return new FixedValueNestedSelector(elementName, css, by, locator, parent, children);
  }

  /**
   * Makes sure a selector is indexed. Wrapping it if necessary.
   * @param selector to ensure is indexed
   * @return indexed version of selector or selector itself if already indexed
   */
  public static IndexedSelector indexedFromSelector(Selector selector) {
    if (selector instanceof IndexedSelector) {
      return (IndexedSelector)selector;
    }
    return indexedFromSelector(selector, 0);
  }

  /**
   * Wrapping passed in selector and setting the index
   * @param selector to wrap
   * @param index index to set
   * @return indexed version of selector
   */
  public static IndexedSelector indexedFromSelector(Selector selector, int index) {
    if (selector instanceof NestedSelector) {
      return new DelegatingIndexedSelector((NestedSelector)selector, index);
    }
    return new DelegatingIndexedSelector(nestedFromSelector(selector), index);
  }

  /**
   * Makes sure selector is nested. Wraps it if necessary.
   * @param selector to ensure nestedness of
   * @return nested version of selector or selector itself if already nested
   */
  public static NestedSelector nestedFromSelector(Selector selector) {
    if (selector instanceof NestedSelector) {
      return (NestedSelector)selector;
    }
    return new NestedSelectorWrapper(selector);
  }

  /**
   * Constructs absolute selector from selector relative to its parent selector.
   * @param selector selector to be used
   * @return new selector that is relative to its parent selector
   */
  public static Selector relativeFromAbsolute(Selector selector) {
    return relativeFromAbsolute(nestedFromSelector(selector).getParent(), selector);
  }

  /**
   * Constructs absolute selector from selector relative to a parent selector.
   * @param parent parent selector to be used as base
   * @param absoluteChild relative to parent
   * @return new selector that is relative to the parent selector
   */
  public static Selector relativeFromAbsolute(Selector parent, Selector absoluteChild) {
    if (parent == null) {
      return absoluteChild;
    }
    String relativeChildCss = StringUtils.removeStart(absoluteChild.asString(), parent.asString());
    String relativeChildName = StringUtils.removeStart(absoluteChild.elementName(), parent.elementName());
    Selector relativeSelector = fromCss(relativeChildName, relativeChildCss);
    return indexedFromSelector(relativeSelector, indexedFromSelector(absoluteChild).getIndex());
  }

  private static void ensureCssLocator(Locator locator) {

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
