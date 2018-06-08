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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.testng.annotations.DataProvider;

import io.wcm.qa.galenium.device.BrowserType;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

/**
 * Test device provider to be used as {@link DataProvider} for tests.
 */
public final class TestDeviceProvider {

  private static final Dimension DIMENSION_DESKTOP = new Dimension(1400, 1000);
  private static final Dimension DIMENSION_MOBILE = new Dimension(400, 1000);
  private static final List<String> TAGS = Collections.emptyList();

  private TestDeviceProvider() {
    // do not instantiate
  }

  /**
   * @return TestDevice array to be used for data driven TestNG Selenium tests.
   */
  @DataProvider(name = "devices")
  public static Object[][] devices() {

    // Objects need to match testMethod's parameters
    ArrayList<TestDevice[]> deviceList = new ArrayList<>();

    for (BrowserType browserType : GaleniumConfiguration.getBrowserTypes()) {
      for (Dimension dimension : getDimensions()) {
        String deviceName = browserType.getBrowser() + "_" + dimension.getWidth();
        deviceList.add(new TestDevice[] {
            new TestDevice(deviceName, browserType, dimension, TAGS, null)
        });
      }
    }

    return deviceList.toArray(new TestDevice[deviceList.size()][]);
  }

  private static Iterable<Dimension> getDimensions() {
    return Arrays.asList(DIMENSION_MOBILE, DIMENSION_DESKTOP);
  }

}
