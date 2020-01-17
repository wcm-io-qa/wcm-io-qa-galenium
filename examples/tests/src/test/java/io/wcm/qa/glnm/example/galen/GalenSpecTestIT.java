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
package io.wcm.qa.glnm.example.galen;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getBaseUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.device.TestDeviceUtil;
import io.wcm.qa.glnm.example.specs.Homepage;
import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

/**
 * Example of how to easily integrate Galen specs into Selenium based test.
 */
class GalenSpecTestIT {

  private static final String PATH_TO_HOMEPAGE = "/en.html";

  @BeforeEach
  void initDriver() {
    TestDevice testDevice = TestDeviceUtil.getSingleTestDevice();
    WebDriverManagement.getDriver(testDevice);
  }

  @Test
  @DisplayName("Testing Homepage")
  void checkHomepageWithGalenSpec() {
    Browser.load(getBaseUrl() + PATH_TO_HOMEPAGE);

    Homepage.check();
  }

  @ParameterizedTest(name = "Testing {0} with {1}")
  @CsvSource(value = {
      "/en.html,/homepage.gspec",
      "/en/conference.html,/conference.gspec",
      "/en/conference.html,/homepage.gspec"
  })
  void checkPageWithGalenSpec(String url, String specPath) {
    Browser.load(getBaseUrl() + url);

    GalenValidation.check(GaleniumConfiguration.getGalenSpecPath() + specPath);
  }

}
