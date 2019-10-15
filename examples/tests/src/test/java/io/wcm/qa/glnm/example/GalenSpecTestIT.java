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
package io.wcm.qa.glnm.example;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.example.specs.Conference;
import io.wcm.qa.glnm.example.specs.Homepage;
import io.wcm.qa.glnm.providers.TestDeviceProvider;

/**
 * Example of how to easily integrate Galen specs into Selenium based test.
 */
public class GalenSpecTestIT extends AbstractExampleBase {

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = TestDeviceProvider.GALENIUM_TEST_DEVICES_ALL)
  public GalenSpecTestIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test
  public void checkConferencePageWithNavigationAndGalenSpec() {
    getLogger().info("Testing Conference Page");
    loadStartUrl();
    openNav();
    clickConferenceNavLink();
    Conference.check();
  }

  @Test
  public void checkHomepageWithGalenSpec() {
    getLogger().info("Testing Homepage");
    loadStartUrl();
    Homepage.check();
  }

  @Override
  protected String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

}
