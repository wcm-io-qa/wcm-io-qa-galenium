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

import java.util.List;

import org.testng.annotations.DataProvider;

public final class TestDeviceProvider {

  public static final String GALENIUM_SINGLE_TEST_DEVICE = "galenium.testdevices.single";
  public static final String GALENIUM_TEST_DEVICES_ALL = "galenium.testdevices.all";
  public static final String GALENIUM_TEST_DEVICES_FROM_DEVICE_CONFIG = "galenium.testdevices.deviceConfig";
  public static final String GALENIUM_TEST_DEVICES_FROM_MQS = "galenium.testdevices.mq";

  private TestDeviceProvider() {
    // do not instantiate
  }

  @DataProvider(name = GALENIUM_SINGLE_TEST_DEVICE)
  public static Object[][] provideSingleTestDevice() {
    List<Object> singleDeviceList = TestDeviceUtil.getSingleTestDevice();
    return TestNgProviderUtil.combine(singleDeviceList);
  }

  @DataProvider(name = GALENIUM_TEST_DEVICES_ALL)
  public static Object[][] provideTestDevices() {
    return TestNgProviderUtil.combine(TestDeviceUtil.getTestDevicesForBrowsersAndMqs());
  }

  @DataProvider(name = GALENIUM_TEST_DEVICES_FROM_DEVICE_CONFIG)
  public static Object[][] provideTestDevicesFromDeviceCsv() {
    return TestNgProviderUtil.combine(TestDeviceUtil.getTestDevicesFromDevicesCsv());
  }

}
