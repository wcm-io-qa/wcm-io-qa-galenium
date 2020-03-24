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

import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.difference.sut.SelectorDifference;
import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.selectors.base.Selector;

class SelectorWebElementMatcher extends DifferentiatingMatcherBase<Selector> {

  private final Matcher<WebElement> matcher;

  SelectorWebElementMatcher(Matcher<WebElement> matcher) {
    this.matcher = matcher;
  }

  @Override
  protected Collection<? extends Difference> differencesFor(Selector item) {
    Collection<Difference> differences = new ArrayList<Difference>();
    differences.add(new SelectorDifference(item));
    return differences;
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    matcher.describeTo(description);
  }

  @Override
  protected boolean matchesDifferentiated(Selector item) {
    return matcher.matches(Element.find(item));
  }

}
