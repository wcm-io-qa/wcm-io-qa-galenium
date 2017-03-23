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

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.util.GaleniumContext;

public class PageTitleVerification extends VerificationBase {

  private static final String KEY_PART_PAGE_TITLE = "title";

  public PageTitleVerification(String pageName) {
    super(pageName);
  }

  public PageTitleVerification(String pageName, String expectedTitle) {
    super(pageName, expectedTitle);
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedValue();
  }

  @Override
  protected String getExpectedKey() {
    if (StringUtils.isNotBlank(super.getExpectedKey())) {
      return super.getExpectedKey() + "." + KEY_PART_PAGE_TITLE;
    }
    return KEY_PART_PAGE_TITLE;
  }

  @Override
  protected String getFailureMessage() {
    return "Expected page title: '" + getExpectedValue() + "' but found '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "Title matched '" + getExpectedValue() + "'";
  }

  @Override
  protected String sampleValue() {
    return GaleniumContext.getDriver().getTitle();
  }

}
