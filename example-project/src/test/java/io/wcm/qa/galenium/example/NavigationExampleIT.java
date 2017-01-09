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

import io.wcm.qa.galenium.util.TestDevice;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Example for pure Selenium test based on Galenium.
 */
public class NavigationExampleIT extends AbstractExampleBase {

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = "devices")
  public NavigationExampleIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test
  public void testNavigation() {
    loadStartUrl();
    openNav();
    clickConferenceNavLink();
    assertRelativePath(PATH_TO_CONFERENCE_PAGE);
  }

  @Override
  public String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

}
