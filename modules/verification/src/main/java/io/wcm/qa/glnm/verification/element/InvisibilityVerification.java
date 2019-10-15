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
package io.wcm.qa.glnm.verification.element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Make sure a certain element is not visible.
 *
 * @since 1.0.0
 */
public class InvisibilityVerification extends VisibilityVerification {

  private static final Logger LOG = LoggerFactory.getLogger(InvisibilityVerification.class);

  /**
   * <p>Constructor for InvisibilityVerification.</p>
   *
   * @param selector to identify element
   * @since 2.0.0
   */
  public InvisibilityVerification(Selector selector) {
    super(selector);
  }

  @Override
  protected void afterVerification() {
    if (isVerified()) {
      LOG.trace("successfully confirmed invisibility of '" + getElementName() + "'");
    }
    else {
      LOG.trace("could not confirm invisibility of '" + getElementName() + "'");
    }
  }

  @Override
  protected boolean doVerification() {
    Boolean visibilityResult = super.doVerification();
    LOG.debug("visibility was '" + visibilityResult + "' when checking for invisibility");
    return !visibilityResult;
  }

  @Override
  protected String getFailureMessage() {
    return super.getSuccessMessage();
  }

  @Override
  protected String getSuccessMessage() {
    return super.getFailureMessage();
  }

}
