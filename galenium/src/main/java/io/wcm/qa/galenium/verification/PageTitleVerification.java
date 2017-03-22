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

  private String actualTitle;
  private String expectedTitle;

  public PageTitleVerification(String expectedTitle) {
    this.setExpectedTitle(expectedTitle);
  }

  private String getActualTitle() {
    return actualTitle;
  }

  private String getExpectedTitle() {
    return expectedTitle;
  }

  private void setActualTitle(String actualTitle) {
    this.actualTitle = actualTitle;
  }

  private void setExpectedTitle(String expectedTitle) {
    this.expectedTitle = expectedTitle;
  }

  @Override
  protected Boolean doVerification() {
    setActualTitle(GaleniumContext.getDriver().getTitle());
    return StringUtils.equals(getActualTitle(), getExpectedTitle());
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedTitle();
  }

  @Override
  protected String getFailureMessage() {
    return "Expected: '" + getExpectedTitle() + "' but found '" + getActualTitle() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "Title matched '" + getExpectedTitle() + "'";
  }

}
