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

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;


/**
 * Implementation of TestDevice.
 */
public class TestDeviceImpl implements TestDevice {

  private final BrowserType browserType;
  private String chromeEmulator;
  private List<String> includeTags;
  private final String name;
  private final Dimension screenSize;

  /**
   * @param profile defining the device
   */
  public TestDeviceImpl(DeviceProfile profile) {
    this(
        profile.getName(),
        profile.getBrowserType(),
        TestDeviceUtil.getDimensionFromProfile(profile),
        TestDeviceUtil.getIncludeTags(profile),
        profile.getEmulator());
  }

  /**
   * @param name for display
   * @param browserType browser
   * @param screenSize size when not in emulator mode
   */
  public TestDeviceImpl(String name, BrowserType browserType, Dimension screenSize) {
    this(name, browserType, screenSize, null, null);
  }

  /**
   * @param name for display
   * @param browserType browser
   * @param screenSize size when not in emulator mode
   * @param include Galen tags
   * @param chromeEmulator emulator like in Device-combobox
   */
  TestDeviceImpl(String name, BrowserType browserType, Dimension screenSize, List<String> include, String chromeEmulator) {
    this.name = name;
    this.browserType = browserType;
    this.screenSize = screenSize;
    setTags(include);
    this.chromeEmulator = chromeEmulator;
  }

  /**
   * Overwrite all existing tags with new list.
   * @param tags new tags or null to reset
   */
  public void setTags(List<String> tags) {
    includeTags = ListUtils.emptyIfNull(tags);
  }

  /**
   * @param tags
   */
  public void addTags(String... tags) {
    CollectionUtils.addAll(includeTags, tags);
  }


  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.device.ITestDevice#getBrowserType()
   */
  @Override
  public BrowserType getBrowserType() {
    return browserType;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.device.ITestDevice#getChromeEmulator()
   */
  @Override
  public String getChromeEmulator() {
    return chromeEmulator;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.device.ITestDevice#getTags()
   */
  @Override
  public List<String> getTags() {
    return includeTags;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.device.ITestDevice#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.device.ITestDevice#getScreenSize()
   */
  @Override
  public Dimension getScreenSize() {
    return screenSize;
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
