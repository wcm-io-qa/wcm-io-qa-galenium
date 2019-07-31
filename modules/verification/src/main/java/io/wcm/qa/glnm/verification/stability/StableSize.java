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

import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.element.SizeSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Verifies stable size of element. Useful when waiting for animated size changes of elements to finish.
 */
public class StableSize extends Stability<Dimension> {

  private static final Logger LOG = LoggerFactory.getLogger(StableSize.class);

  /**
   * @param selector identifies the element
   */
  public StableSize(Selector selector) {
    super(new SizeSampler(selector));
  }

  @Override
  protected boolean checkForEquality(Dimension value1, Dimension value2) {
    LOG.trace("comparing sizes: '" + value1 + "' <> '" + value2 + "'");
    return value1.equals(value2);
  }

}
