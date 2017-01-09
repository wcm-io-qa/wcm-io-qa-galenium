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
package io.wcm.qa.galenium.example.pageobjects;

import org.openqa.selenium.WebDriver;


class AbstractWebDriverPageObject {

  private static final int APPROXIMATE_WINDOW_SIZE_FOR_MOBILE_CUTOFF = 610;
  protected WebDriver driver;

  AbstractWebDriverPageObject(WebDriver driver) {
    this.driver = driver;
  }

  protected WebDriver getDriver() {
    return driver;
  }

  protected boolean isMobile() {
    return getDriver().manage().window().getSize().getWidth() < APPROXIMATE_WINDOW_SIZE_FOR_MOBILE_CUTOFF;
  }

}
