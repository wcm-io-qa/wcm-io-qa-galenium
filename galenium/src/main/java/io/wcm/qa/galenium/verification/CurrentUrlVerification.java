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

public class CurrentUrlVerification extends VerificationBase {

  private String actualUrl;
  private String expectedUrl;

  public CurrentUrlVerification(String expectedUrl) {
    setExpectedUrl(expectedUrl);
  }

  private String getActualUrl() {
    return actualUrl;
  }

  private String getExpectedUrl() {
    return expectedUrl;
  }

  private void setActualUrl(String actualUrl) {
    this.actualUrl = actualUrl;
  }

  private void setExpectedUrl(String expectedUrl) {
    this.expectedUrl = expectedUrl;
  }

  @Override
  protected Boolean doVerification() {
    setActualUrl(GaleniumContext.getDriver().getCurrentUrl());
    return StringUtils.equals(getActualUrl(), getExpectedUrl());
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedUrl();
  }

  @Override
  protected String getFailureMessage() {
    return "Expected URL: '" + getExpectedUrl() + "' but found '" + getActualUrl() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "Found URL: '" + getExpectedUrl() + "'";
  }

}
