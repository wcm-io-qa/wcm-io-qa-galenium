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

import io.wcm.qa.glnm.selectors.base.SelectorFactory;


/**
 * Footer meta navigation section.
 */
public class FooterNavSection extends AbstractWebElementPageObject {

  private List<LinkItem> navLinks;

  FooterNavSection(WebElement webElement) {
    super(webElement);
  }

  /**
   * @return list of navigation links in this section or empty list if none exist
   */
  public List<LinkItem> getNavLinks() {
    if (navLinks == null) {
      navLinks = new ArrayList<LinkItem>();
      List<WebElement> elements = getWebElement().findElements(SelectorFactory.fromCss("ul > li > a").asBy());
      for (WebElement webElement : elements) {
        navLinks.add(new LinkItem(webElement));
      }
    }
    return navLinks;
  }

  public String getTitle() {
    return getHeadline().getText();
  }

  private WebElement getHeadline() {
    return getWebElement().findElement(SelectorFactory.fromCss("h2").asBy());
  }
}
