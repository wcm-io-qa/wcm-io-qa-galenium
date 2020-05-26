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
package io.wcm.qa.glnm.hamcrest.element;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.hamcrest.FunctionalWrappingMatcher;


class WebElementMatcher<T> extends FunctionalWrappingMatcher<WebElement, T> {

  private String mappingDescription;

  protected WebElementMatcher(
      String description,
      Function<WebElement, T> mapping,
      Matcher<T> matcher) {
    super(mapping, matcher);
    setMappingDescription(description);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    if (StringUtils.isNotEmpty(getMappingDescription())) {
      description.appendText(getMappingDescription());
      description.appendText(" ");
    }
    super.describeTo(description);
  }

  protected String getMappingDescription() {
    return mappingDescription;
  }

  protected void setMappingDescription(String mappingDescription) {
    this.mappingDescription = mappingDescription;
  }

  /**
   * <p>elementMapped.</p>
   *
   * @param <T> type of mapped value
   * @param mapping extracting value from element
   * @param matcher matches mapped value
   * @return matcher doing mapping and matching
   * @since 5.0.0
   */
  public static <T> Matcher<WebElement> elementMapped(Function<WebElement, T> mapping, Matcher<T> matcher) {
    return new WebElementMatcher<T>("", mapping, matcher);
  }
}
