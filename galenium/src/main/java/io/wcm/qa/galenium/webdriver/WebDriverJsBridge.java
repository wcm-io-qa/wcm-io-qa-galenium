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
package io.wcm.qa.galenium.webdriver;

import java.util.List;

import org.openqa.selenium.WebDriver;

import io.wcm.qa.galenium.util.BrowserType;
import io.wcm.qa.galenium.util.BrowserUtil;
import io.wcm.qa.galenium.util.TestDevice;

public class WebDriverJsBridge {

  //getDriver(url, name, size, tags, browserType);
  public static WebDriver getDriver(String url, String deviceName, String sizeString, List<String> tags, String browser) {
    BrowserType browserType = BrowserType.valueOf(browser.trim().toUpperCase());
    TestDevice testDevice = new TestDevice(deviceName, browserType, BrowserUtil.getDimension(sizeString), tags, null);
    WebDriverManager.getLogger().trace("new webdriver from (" + url + ", " + sizeString + ", " + browser + ")");
    WebDriverManager.getLogger().trace("new webdriver from " + testDevice);
    return WebDriverManager.getDriver(testDevice);
  }

}
