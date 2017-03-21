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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.selectors.Selector;

public class CssClassVerification extends AttributeVerification {

  public CssClassVerification(Selector selector, String cssClass) {
    super(selector, "class", cssClass);
    setExpectedValue(cssClass);
    setPreVerification(new VisibilityVerification(getSelector()));
  }

  @Override
  protected Boolean doVerification() {
    String cssClasses = getActualValue();
    if (StringUtils.isBlank(cssClasses)) {
      return false;
    }
    String[] splitCssClasses = cssClasses.split(" ");
    return ArrayUtils.contains(splitCssClasses, getExpectedValue());
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedValue();
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should have CSS class '" + getExpectedValue() + "', but only found '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " has CSS class '" + getExpectedValue() + "'";
  }

}
