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

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.sampling.element.AttributeSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matches if selector defines a visible element.
 *
 * @since 5.0.0
 */
public class HasAttribute extends SelectorSamplerMatcher<String> {

  private String attributeName;

  /**
   * Constructor.
   *
   * @param attributeName a {@link java.lang.String} object.
   * @param matcher a {@link org.hamcrest.Matcher} object.
   */
  public HasAttribute(String attributeName, Matcher<String> matcher) {
    super(matcher, AttributeSampler.class, attributeName);
    setAttributeName(attributeName);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("attribute '");
    description.appendText(getAttributeName());
    description.appendText("' of ");
    super.describeTo(description);
  }

  protected String getAttributeName() {
    return attributeName;
  }

  protected void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  /**
   * Is element defined by selector visible
   *
   * @return matcher
   * @param name a {@link java.lang.String} object.
   * @param matcher a {@link org.hamcrest.Matcher} object.
   */
  public static Matcher<Selector> hasAttribute(String name, Matcher<String> matcher) {
    return new HasAttribute(name, matcher);
  }
}
