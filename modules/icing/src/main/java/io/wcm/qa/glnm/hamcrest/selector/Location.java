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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.Point;

import io.wcm.qa.glnm.sampling.element.LocationSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * <p>Location class.</p>
 *
 * @since 5.0.0
 */
public class Location extends SelectorSamplerMatcher<Point> {

  /**
   * <p>Constructor for Location.</p>
   *
   * @param matcher a {@link org.hamcrest.Matcher} object.
   */
  public Location(Matcher<Point> matcher) {
    super(matcher, LocationSampler.class);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    super.describeTo(description);
    description.appendText(" location");
  }

  /**
   * <p>location.</p>
   *
   * @param matcher a {@link org.hamcrest.Matcher} object.
   * @return a {@link org.hamcrest.Matcher} object.
   */
  public static Matcher<Selector> location(Matcher<Point> matcher) {
    return new Location(matcher);
  }
}
