/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.glnm.sampling.element;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.element.base.WebElementBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Samples position of web element.
 */
public class LocationSampler extends WebElementBasedSampler<Point> {

  private static final Logger LOG = LoggerFactory.getLogger(LocationSampler.class);

  /**
   * @param selector identifies element
   */
  public LocationSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected Point freshSample(WebElement element) {
    Point location = element.getLocation();
    LOG.trace("Sampled location for '" + getInput().getSelector().elementName() + "': " + location);
    return location;
  }


}
