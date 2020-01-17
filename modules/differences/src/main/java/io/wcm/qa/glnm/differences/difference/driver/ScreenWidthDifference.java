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
package io.wcm.qa.glnm.differences.difference.driver;

import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.differences.base.DifferenceBase;

/**
 *  {@link io.wcm.qa.glnm.device.TestDevice} based  {@link io.wcm.qa.glnm.differences.base.Difference}.
 *
 * @since 1.0.0
 */
public class ScreenWidthDifference extends DifferenceBase {

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return "width";
  }

  @Override
  protected String getRawTag() {
    int width = GaleniumContext.getTestDevice().getScreenSize().getWidth();
    return String.format("%04d", width);
  }

}
