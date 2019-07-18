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
package io.wcm.qa.glnm.verification.stability;

import org.openqa.selenium.Point;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.sampling.element.LocationSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Verifies stable position of element. Useful when waiting for animated moving of elements to finish.
 */
public class StablePosition extends Stability<Point> {

  /**
   * @param selector identifies element
   */
  public StablePosition(Selector selector) {
    super(new LocationSampler(selector));
  }

  @Override
  protected boolean checkForEquality(Point value1, Point value2) {
    GaleniumReportUtil.getLogger().trace("comparing locations: '" + value1 + "' <> '" + value2 + "'");
    return value1.equals(value2);
  }

}
