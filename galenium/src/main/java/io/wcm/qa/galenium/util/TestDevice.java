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
package io.wcm.qa.galenium.util;

import java.util.List;

import org.openqa.selenium.Dimension;

/**
 * DataModel for screenSizes and galen-tags
 */
public class TestDevice {

  private final String name;
  private final Dimension screenSize;
  private final List<String> tags;
  private final BrowserType browserType;
  private String chromeEmulator;

  /**
   *
   * @param name for display
   * @param browserType browser
   * @param screenSize size when not in emulator mode
   * @param tags galen tag
   * @param chromeEmulator emulator like in Device-combobox
   */
  public TestDevice(String name, BrowserType browserType, Dimension screenSize, List<String> tags, String chromeEmulator) {
    this.name = name;
    this.browserType = browserType;
    this.screenSize = screenSize;
    this.tags = tags;
    this.chromeEmulator = chromeEmulator;
  }

  public BrowserType getBrowserType() {
    return browserType;
  }

  public String getChromeEmulator() {
    return chromeEmulator;
  }

  public void setChromeEmulator(String chromeEmulator) {
    this.chromeEmulator = chromeEmulator;
  }

  public String getFullInfo() {
    return "TestDevice{" + browserType + " " + name + " " + screenSize + " " + tags + "}";
  }

  public String getName() {
    return name;
  }

  public Dimension getScreenSize() {
    return screenSize;
  }

  public List<String> getTags() {
    return tags;
  }

  @Override
  public String toString() {
    return "TestDevice{" + "name=" + name + ", screenSize=" + screenSize + ", tags=" + tags + ", browserType=" + browserType + ", chromeEmulator=" + chromeEmulator + '}';
  }

}
