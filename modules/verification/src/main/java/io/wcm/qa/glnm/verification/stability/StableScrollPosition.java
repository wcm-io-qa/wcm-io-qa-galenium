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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.browser.ScrollPositionSampler;

/**
 * Verifies stable scroll position of current page. Useful when waiting for scrolling to finish.
 *
 * @since 1.0.0
 */
public class StableScrollPosition extends Stability<Long> {

  private static final Logger LOG = LoggerFactory.getLogger(StableScrollPosition.class);

  /**
   * Constructor.
   *
   * @since 2.0.0
   */
  public StableScrollPosition() {
    super(new ScrollPositionSampler());
  }

  @Override
  protected boolean checkForEquality(Long value1, Long value2) {
    LOG.trace("comparing scroll positions: '" + value1 + "' <> '" + value2 + "'");
    return value1.equals(value2);
  }

}
