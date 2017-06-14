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
    SelectorFromString selector = new SelectorFromString(selectorString);
    selector.setName(elementName);
    return selector;
  }

  public static Selector relativeSelector(Selector parent, Selector relativeSelector) {
    return relativeSelector(parent, relativeSelector.elementName(), relativeSelector.asString());
  }

  public static Selector relativeSelector(Selector parent, String relativeCssSelector) {
    return relativeSelector(parent, "child", relativeCssSelector);
  }

  public static Selector relativeSelector(Selector parent, String childName, String relativeCssSelector) {
    String selectorString = parent.asString() + " " + relativeCssSelector;
    String elementName = parent.elementName() + "|" + childName;
    return fromCss(elementName, selectorString);
  }
}
