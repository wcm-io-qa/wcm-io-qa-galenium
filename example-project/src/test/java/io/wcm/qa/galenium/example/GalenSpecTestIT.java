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
package io.wcm.qa.galenium.example;

import static io.wcm.qa.galenium.util.GalenLayoutChecker.checkLayout;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Example of how to easily integrate Galen specs into Selenium based test.
 */
public class GalenSpecTestIT extends AbstractExampleBase {

  private static final String SPEC_PATH_FOR_CONFERENCE_PAGE = GaleniumConfiguration.getGalenSpecPath()
      + "/conference.gspec";
  private static final String SPEC_PATH_FOR_HOMEPAGE = GaleniumConfiguration.getGalenSpecPath() + "/homepage.gspec";

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = "devices")
  public GalenSpecTestIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test
  public void checkConferencePageWithNavigationAndGalenSpec() {
    getLogger().info("Testing Conference Page");
    loadStartUrl();
    openNav();
    clickConferenceNavLink();
    checkLayout("Conference Page", SPEC_PATH_FOR_CONFERENCE_PAGE);
  }

  @Test
  public void checkHomepageWithGalenSpec() {
    getLogger().info("Testing Homepage");
    loadStartUrl();
    checkLayout("Homepage", SPEC_PATH_FOR_HOMEPAGE);
  }

  @Override
  protected String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

}
