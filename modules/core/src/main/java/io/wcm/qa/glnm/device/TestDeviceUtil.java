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
package io.wcm.qa.glnm.device;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.CsvUtil;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.mediaquery.MediaQuery;
import io.wcm.qa.glnm.mediaquery.MediaQueryUtil;

/**
 * Convenience methods around test devices.
 *
 * @since 1.0.0
 */
public final class TestDeviceUtil {

  private static final File CSV_FILE_DEVICES = new File(GaleniumConfiguration.getDeviceCsvFilePath());
  private static final Logger LOG = LoggerFactory.getLogger(TestDeviceUtil.class);
  private static final Integer MEDIA_QUERY_HEIGHT = GaleniumConfiguration.getMediaQueryHeight();

  private TestDeviceUtil() {
    // do not instantiate
  }

  /**
   * <p>
   * getSingleTestDevice.
   * </p>
   *
   * @return one of the configured test devices as a singleton list
   * @since 3.0.0
   */
  public static List<Object> getSingleTestDeviceAsList() {
    Object singleDevice = getSingleTestDevice();
    List<Object> singleDeviceList = Collections.singletonList(singleDevice);
    return singleDeviceList;
  }

  /**
   * <p>
   * getSingleTestDevice.
   * </p>
   *
   * @return one of the configured test devices
   * @since 4.0.0
   */
  public static TestDevice getSingleTestDevice() {
    Collection<TestDevice> testDevices = TestDeviceUtil.getTestDevicesForBrowsersAndMqs();
    if (testDevices == null || testDevices.isEmpty()) {
      throw new GaleniumException("no configured devices found, when trying to get single test device.");
    }
    int middle = testDevices.size() / 2;
    TestDevice singleDevice = IterableUtils.get(testDevices, middle);
    return singleDevice;
  }

  /**
   * Test device for upper bound of media query.
   *
   * @param browserType browser to use
   * @param mediaQuery media query to get upper bound from
   * @return test device
   * @since 3.0.0
   */
  public static TestDevice getTestDeviceForUpperBound(BrowserType browserType, MediaQuery mediaQuery) {
    int upperBound = mediaQuery.getUpperBound();
    String mediaQueryName = mediaQuery.getName();
    return TestDeviceUtil.getTestDevice(browserType, mediaQueryName, upperBound);
  }

  /**
   * Test devices using the configured Browsers and upper bounds of the configured media queries.
   *
   * @return configured test devices
   * @since 3.0.0
   */
  public static Collection<TestDevice> getTestDevicesForBrowsersAndMqs() {
    Collection<TestDevice> testDevices = new ArrayList<>();
    for (MediaQuery mediaQuery : MediaQueryUtil.getMediaQueries()) {
      for (BrowserType browserType : GaleniumConfiguration.getBrowserTypes()) {
        TestDevice testDevice = getTestDeviceForUpperBound(browserType, mediaQuery);
        testDevices.add(testDevice);
      }
    }
    if (testDevices.isEmpty()) {
      throw new GaleniumException("no test devices configured");
    }
    return testDevices;
  }

  /**
   * <p>getTestDevicesFromDevicesCsv.</p>
   *
   * @return all test devices defined in CSV
   * @since 3.0.0
   */
  public static Collection<TestDevice> getTestDevicesFromDevicesCsv() {
    Collection<TestDevice> testDevices = new ArrayList<>();
    Collection<DeviceProfile> profiles = CsvUtil.<DeviceProfile>parseToBeans(CSV_FILE_DEVICES, DeviceProfile.class);
    for (DeviceProfile deviceProfile : profiles) {
      if (isProfileMatchesBrowsers(deviceProfile)) {
        LOG.debug("adding device: " + deviceProfile);
        testDevices.add(getTestDevice(deviceProfile));
      }
      else {
        LOG.debug("skipping device: " + deviceProfile);
      }
    }
    return testDevices;
  }

  private static String getDeviceName(BrowserType browserType, int screenWidth) {
    return browserType.name() + "_" + screenWidth;
  }

  private static List<String> getIncludeTags(BrowserType browserType, String mediaQueryName) {
    List<String> tags = new ArrayList<>();
    tags.add(mediaQueryName);
    tags.add(browserType.name());
    return tags;
  }

  private static Dimension getScreenSize(int width) {
    return new Dimension(width, TestDeviceUtil.MEDIA_QUERY_HEIGHT);
  }

  private static TestDevice getTestDevice(BrowserType browserType, String mediaQueryName, int width) {
    String name = getDeviceName(browserType, width);
    Dimension screenSize = getScreenSize(width);
    TestDeviceImpl testDevice = new TestDeviceImpl(name, browserType, screenSize);
    setIncludeTags(testDevice, browserType, mediaQueryName);
    return testDevice;
  }

  private static TestDevice getTestDevice(DeviceProfile deviceProfile) {
    TestDeviceImpl testDevice = new TestDeviceImpl(deviceProfile);
    MediaQuery mediaQuery = MediaQueryUtil.getMatchingMediaQuery(testDevice);
    BrowserType browserType = testDevice.getBrowserType();
    setIncludeTags(testDevice, browserType, mediaQuery.getName());
    return testDevice;
  }

  private static boolean isProfileMatchesBrowsers(DeviceProfile deviceProfile) {
    List<BrowserType> browserTypes = GaleniumConfiguration.getBrowserTypes();
    return browserTypes.contains(deviceProfile.getBrowserType());
  }

  private static void setIncludeTags(TestDeviceImpl testDevice, BrowserType browserType, String mediaQueryName) {
    testDevice.setTags(getIncludeTags(browserType, mediaQueryName));
  }

  static Dimension getDimensionFromProfile(DeviceProfile profile) {
    return new Dimension(profile.getWidth(), profile.getHeight());
  }

  static List<String> getIncludeTags(DeviceProfile profile) {
    return Collections.singletonList(profile.getBrowser());
  }

}
