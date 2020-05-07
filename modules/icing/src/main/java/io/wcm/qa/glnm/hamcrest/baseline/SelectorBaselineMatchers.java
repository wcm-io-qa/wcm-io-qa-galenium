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
package io.wcm.qa.glnm.hamcrest.baseline;


import org.hamcrest.Matcher;

import io.wcm.qa.glnm.persistence.Persistence;
import io.wcm.qa.glnm.sampling.element.TextSampler;
import io.wcm.qa.glnm.sampling.element.VisibilitySampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matchers around
 *
 * @since 5.0.0
 */
public final class SelectorBaselineMatchers {

  private SelectorBaselineMatchers() {
    // do not instantiate
  }

  /**
   * Match Selenium WebElement if it is displayed.
   *
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   * @since 5.0.0
   */
  public static Matcher<Selector> hasBaselineVisibility() {
    return new SelectorSamplerBaselineMatcher<Boolean>(
        Persistence::forBoolean,
        VisibilitySampler.class);
  }

  /**
   * Match Selenium WebElement text against baseline.
   *
   * @return {@link io.wcm.qa.glnm.selectors.base.Selector} based matcher
   * @since 5.0.0
   */
  public static Matcher<Selector> hasBaselineText() {
    return new SelectorSamplerBaselineMatcher<String>(
        Persistence::forString,
        TextSampler.class);
  }

}
