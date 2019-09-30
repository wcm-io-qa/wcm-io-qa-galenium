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
package io.wcm.qa.glnm.providers;

import java.util.List;

import org.testng.annotations.DataProvider;

import io.wcm.qa.glnm.device.TestDeviceUtil;

/**
 * TestNG data provider for test devices.
 */
public final class TestDeviceProvider {

  /** Single device. */
  public static final String GALENIUM_SINGLE_TEST_DEVICE = "galenium.testdevices.single";
  /** All devices. */
  public static final String GALENIUM_TEST_DEVICES_ALL = "galenium.testdevices.all";
  /** All devices from config. */
  public static final String GALENIUM_TEST_DEVICES_FROM_DEVICE_CONFIG = "galenium.testdevices.deviceConfig";

  private TestDeviceProvider() {
    // do not instantiate
  }

  /**
   * @return single test device
   */
  @DataProvider(name = GALENIUM_SINGLE_TEST_DEVICE)
  public static Object[][] provideSingleTestDevice() {
    List<Object> singleDeviceList = TestDeviceUtil.getSingleTestDevice();
    return TestNgProviderUtil.combine(singleDeviceList);
  }

  /**
   * @return all test devices
   */
  @DataProvider(name = GALENIUM_TEST_DEVICES_ALL)
  public static Object[][] provideTestDevices() {
    return TestNgProviderUtil.combine(TestDeviceUtil.getTestDevicesForBrowsersAndMqs());
  }

  /**
   * @return all test devices from device config
   */
  @DataProvider(name = GALENIUM_TEST_DEVICES_FROM_DEVICE_CONFIG)
  public static Object[][] provideTestDevicesFromDeviceCsv() {
    return TestNgProviderUtil.combine(TestDeviceUtil.getTestDevicesFromDevicesCsv());
  }

}
