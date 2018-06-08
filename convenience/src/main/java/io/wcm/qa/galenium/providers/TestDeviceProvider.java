/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.Dimension;
import org.testng.annotations.DataProvider;

import io.wcm.qa.galenium.device.BrowserType;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.mediaquery.MediaQuery;
import io.wcm.qa.galenium.mediaquery.MediaQueryUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

public final class TestDeviceProvider {

  public static final String GALENIUM_TEST_DEVICES = "galenium.testdevices.all";
  public static final String GALENIUM_SINGLE_TEST_DEVICE = "galenium.testdevices.single";
  private static final Integer MEDIA_QUERY_HEIGHT = GaleniumConfiguration.getMediaQueryHeight();

  private TestDeviceProvider() {
    // do not instantiate
  }

  /**
   * @return configured test devices
   */
  public static Collection<TestDevice> getTestDevices() {
    Collection<TestDevice> testDevices = new ArrayList<>();
    for (MediaQuery mediaQuery : MediaQueryUtil.getMediaQueries()) {
      for (BrowserType browserType : GaleniumConfiguration.getBrowserTypes()) {
        TestDevice testDevice = getTestDevice(browserType, mediaQuery);
        testDevices.add(testDevice);
      }
    }
    if (testDevices.isEmpty()) {
      throw new GaleniumException("no test devices configured");
    }
    return testDevices;
  }

  @DataProvider(name = GALENIUM_SINGLE_TEST_DEVICE)
  public static Object[][] provideSingleTestDevice() {
    List<Object> singleDeviceList = getSingleTestDevice();
    return TestNgProviderUtil.combine(singleDeviceList);
  }

  /**
   * @return first of the configured test devices
   */
  public static List<Object> getSingleTestDevice() {
    Object testDevices = getTestDevices();
    Object firstDevice = CollectionUtils.get(testDevices, 0);
    List<Object> singleDeviceList = Collections.singletonList(firstDevice);
    return singleDeviceList;
  }

  @DataProvider(name = GALENIUM_TEST_DEVICES)
  public static Object[][] provideTestDevices() {
    return TestNgProviderUtil.combine(getTestDevices());
  }

  private static String getDeviceName(BrowserType browserType, MediaQuery mediaQuery) {
    return browserType.name() + "_" + mediaQuery.getLowerBound();
  }

  private static Dimension getScreenSize(MediaQuery mediaQuery) {
    return new Dimension(mediaQuery.getLowerBound(), MEDIA_QUERY_HEIGHT);
  }

  private static TestDevice getTestDevice(BrowserType browserType, MediaQuery mediaQuery) {
    String name = getDeviceName(browserType, mediaQuery);
    Dimension screenSize = getScreenSize(mediaQuery);
    return new TestDevice(name, browserType, screenSize);
  }


}
