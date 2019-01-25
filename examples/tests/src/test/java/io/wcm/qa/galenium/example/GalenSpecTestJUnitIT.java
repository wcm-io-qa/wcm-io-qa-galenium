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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.device.TestDeviceUtil;
import io.wcm.qa.galenium.interaction.Browser;
import io.wcm.qa.galenium.junitrunner.GaleniumExtension;
import io.wcm.qa.galenium.providers.TestNgProviderUtil;
import io.wcm.qa.galenium.testcase.AbstractGaleniumBase;
import io.wcm.qa.galenium.util.GaleniumContext;

/**
 * Example of how to write Test, that can be executed with JUnit
 */
@ExtendWith(GaleniumExtension.class)
public class GalenSpecTestJUnitIT extends AbstractGaleniumBase {

  public static final String PROVIDER = "provider";

  private String url;

  @Factory(dataProviderClass = GalenSpecTestJUnitIT.class, dataProvider = PROVIDER)
  public GalenSpecTestJUnitIT(TestDevice testDevice, String url) {
    super(testDevice);
    this.url = url;
  }

  @TestTemplate
  public void checkJUnitExecution() {
    getLogger().info("Testing url " + url);
    Browser.load(url);

    GaleniumContext.getAssertion().assertNotNull(Browser.getCurrentUrl());
  }

  @DataProvider(name = PROVIDER)
  public static Object[][] provideTestDevices() {
    List<String> testUrls = Arrays.asList("http://localhost");
    return TestNgProviderUtil.combine(TestDeviceUtil.getTestDevicesForBrowsersAndMqs(), testUrls);
  }

}
