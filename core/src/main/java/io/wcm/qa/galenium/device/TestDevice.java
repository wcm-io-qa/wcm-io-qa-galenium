/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.galenium.device;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * DataModel for browser types, screen sizes and Galen tags. Used to initialize {@link WebDriver}.
 */
public class TestDevice {

  private final BrowserType browserType;
  private String chromeEmulator;
  private final String name;
  private final Dimension screenSize;
  private final List<String> tags;

  /**
   * @param name for display
   * @param browserType browser
   * @param screenSize size when not in emulator mode
   */
  public TestDevice(String name, BrowserType browserType, Dimension screenSize) {
    this(name, browserType, screenSize, null, null);
  }

  /**
   * @param name for display
   * @param browserType browser
   * @param screenSize size when not in emulator mode
   * @param tags Galen tags
   * @param chromeEmulator emulator like in Device-combobox
   */
  public TestDevice(String name, BrowserType browserType, Dimension screenSize, List<String> tags, String chromeEmulator) {
    this.name = name;
    this.browserType = browserType;
    this.screenSize = screenSize;
    this.tags = ListUtils.emptyIfNull(tags);
    this.chromeEmulator = chromeEmulator;
  }

  /**
   * @return browser to use for this device
   */
  public BrowserType getBrowserType() {
    return browserType;
  }

  /**
   * @return chrome emulator string if set or null
   */
  public String getChromeEmulator() {
    return chromeEmulator;
  }

  /**
   * @return test device name for use in logging and test case names
   */
  public String getName() {
    return name;
  }

  /**
   * @return viewport size for this test device
   */
  public Dimension getScreenSize() {
    return screenSize;
  }

  /**
   * @return tags to use in Galen tests and specs
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * @param chromeEmulator chrome emulator string to use in web driver
   */
  public void setChromeEmulator(String chromeEmulator) {
    this.chromeEmulator = chromeEmulator;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("TestDevice{");
    stringBuilder.append(name);
    stringBuilder.append("_");
    stringBuilder.append(screenSize);
    stringBuilder.append("_[");
    StringUtils.join(getTags().toArray(), "|");
    stringBuilder.append("]_");
    stringBuilder.append(browserType);
    if (chromeEmulator != null) {
      stringBuilder.append("_");
      stringBuilder.append(chromeEmulator);
    }
    stringBuilder.append('}');
    return stringBuilder.toString();
  }

}
