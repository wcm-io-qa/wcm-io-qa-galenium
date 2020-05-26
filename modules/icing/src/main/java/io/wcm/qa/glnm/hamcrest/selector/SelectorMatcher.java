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
package io.wcm.qa.glnm.hamcrest.selector;


import org.hamcrest.Matcher;

import io.wcm.qa.glnm.hamcrest.TypeSafeWrappingMatcher;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Base class for matching based on {@link io.wcm.qa.glnm.selectors.base.Selector}.
 *
 * @param <T> type to match
 * @since 5.0.0
 */
public abstract class SelectorMatcher<T> extends TypeSafeWrappingMatcher<Selector, T> {

  private Selector selector;

  protected SelectorMatcher(Matcher<T> matcher) {
    super(matcher);
  }

  protected Selector getSelector() {
    return selector;
  }

  protected String getSelectorName() {
    Selector s = getSelector();
    if (s != null) {
      return s.elementName();
    }
    return "NULL";
  }

  @Override
  protected boolean matchesSafely(Selector item) {
    setSelector(item);
    return super.matchesSafely(item);
  }

  protected void setSelector(Selector selector) {
    this.selector = selector;
  }

}
