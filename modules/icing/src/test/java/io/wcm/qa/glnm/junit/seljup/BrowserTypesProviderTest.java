/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.junit.seljup;

import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getCurrentDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.seljup.Arguments;
import io.github.bonigarcia.seljup.BrowserType;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.junit.combinatorial.CartesianProduct;

class BrowserTypesProviderTest {

  @BeforeEach
  void initDriver(@Arguments({ "--headless" }) WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  @CartesianProduct
  @BrowserTypes(BrowserType.CHROME)
  void testChrome() {
    assertThat(getCurrentDriver(), is(notNullValue()));
  }

  @CartesianProduct
  @BrowserTypes(BrowserType.FIREFOX)
  void testFirefox() {
    assertThat(getCurrentDriver(), is(notNullValue()));
  }

  @CartesianProduct
  @BrowserTypes({
      BrowserType.FIREFOX,
      BrowserType.CHROME
  })
  void testChromeAndFirefox() {
    assertThat(getCurrentDriver(), is(notNullValue()));
  }
}
