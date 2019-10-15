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
package io.wcm.qa.glnm.example.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

/**
 * Top level entry in main navigation.
 */
public class NavigationTopLevelEntry extends LinkItem {

  private static final Selector SELECTOR_MAIN_LINK = SelectorFactory.fromCss("a.navlink-main");
  private static final Selector SELECTOR_SUB_LINKS = SelectorFactory.fromCss("ul > li > a");
  private List<LinkItem> subEntries;

  NavigationTopLevelEntry(WebElement webElement) {
    super(webElement);
  }

  @Override
  public void click() {
    getNavLink().click();
  }

  /**
   * @return sub entries or empty list if none exist
   */
  public List<LinkItem> getNavigationSubEntries() {
    if (subEntries == null) {
      subEntries = new ArrayList<LinkItem>();
      List<WebElement> elements = getWebElement().findElements(SELECTOR_SUB_LINKS.asBy());
      for (WebElement webElement : elements) {
        subEntries.add(new LinkItem(webElement));
      }
    }
    return subEntries;
  }

  @Override
  public String getTitle() {
    return getNavLink().getText();
  }

  private WebElement getNavLink() {
    return getWebElement().findElement(SELECTOR_MAIN_LINK.asBy());
  }
}
