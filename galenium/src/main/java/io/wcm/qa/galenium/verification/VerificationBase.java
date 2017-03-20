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
package io.wcm.qa.galenium.verification;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.InteractionUtil;

public abstract class VerificationBase {

  private MutableDifferences differences;
  private Selector selector;
  private Boolean verified;

  protected VerificationBase(Selector selector) {
    setSelector(selector);
  }

  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  public String getMessage() {
    if (isVerified() == null) {
      return getNotVerifiedMessage();
    }
    if (isVerified()) {
      return getSuccessMessage();
    }
    return getFailureMessage();
  }

  protected String getNotVerifiedMessage() {
    return "NOT VERIFIED: " + getElementName();
  }

  protected String getElementName() {
    return getSelector().elementName();
  }

  public Boolean isVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public boolean verify() {
    setVerified(doVerification());
    return isVerified();
  }

  protected abstract Boolean doVerification();

  protected MutableDifferences getDifferences() {
    return differences;
  }

  protected WebElement getElement() {
    return InteractionUtil.getElementVisible(getSelector());
  }


  protected abstract String getFailureMessage();

  protected Selector getSelector() {
    return selector;
  }

  protected abstract String getSuccessMessage();

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

  protected void setSelector(Selector selector) {
    this.selector = selector;
  }
}
