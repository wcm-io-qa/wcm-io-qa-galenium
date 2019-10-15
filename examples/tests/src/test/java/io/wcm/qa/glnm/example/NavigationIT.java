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
import io.wcm.qa.glnm.providers.TestDeviceProvider;
import io.wcm.qa.glnm.util.BrowserUtil;
import io.wcm.qa.glnm.verification.driver.TitleAndUrlVerification;
import io.wcm.qa.glnm.verification.util.Check;

/**
 * Example for pure Selenium test based on Galenium.
 */
public class NavigationIT extends AbstractExampleBase {

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = TestDeviceProvider.GALENIUM_TEST_DEVICES_FROM_DEVICE_CONFIG)
  public NavigationIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Override
  public String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

  @Test
  public void testNavigation() {
    loadStartUrl();
    openNav();
    clickConferenceNavLink();
    if (BrowserUtil.isFirefox()) {
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException ex) {
        getLogger().debug("exception when sleeping after click", ex);
      }
    }
    Check.verify(new TitleAndUrlVerification("conference"));
  }

}
