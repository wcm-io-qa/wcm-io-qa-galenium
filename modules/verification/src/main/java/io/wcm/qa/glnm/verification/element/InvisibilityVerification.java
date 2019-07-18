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

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Make sure a certain element is not visible.
 */
public class InvisibilityVerification extends VisibilityVerification {

  /**
   * @param selector to identify element
   */
  public InvisibilityVerification(Selector selector) {
    super(selector);
  }

  @Override
  protected void afterVerification() {
    if (isVerified()) {
      getLogger().trace("successfully confirmed invisibility of '" + getElementName() + "'");
    }
    else {
      getLogger().trace("could not confirm invisibility of '" + getElementName() + "'");
    }
  }

  @Override
  protected boolean doVerification() {
    Boolean visibilityResult = super.doVerification();
    getLogger().debug("visibility was '" + visibilityResult + "' when checking for invisibility");
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
