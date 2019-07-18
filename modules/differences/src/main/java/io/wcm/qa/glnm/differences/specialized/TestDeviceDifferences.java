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
package io.wcm.qa.glnm.differences.specialized;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.difference.IntegerDifference;
import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Browser and horizontal viewport size as differences.
 */
public class TestDeviceDifferences implements Differences {

  private static final StringDifference NO_DEVICE_DIFFERENCE = new StringDifference("no-device");
  private TestDevice device;
  private MutableDifferences differences;

  /**
   * Constructor.
   */
  public TestDeviceDifferences() {
    super();
  }

  /**
   * Constructor.
   * @param device to base differences on
   */
  public TestDeviceDifferences(TestDevice device) {
    setDevice(device);
  }

  @Override
  public String asFilePath() {
    return getDifferences().asFilePath();
  }

  @Override
  public String asPropertyKey() {
    return getDifferences().asPropertyKey();
  }

  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("TestDevice(browser=");
    stringBuilder.append(getBrowserDifference());
    stringBuilder.append("|screen=");
    stringBuilder.append(getScreenSizeDifference());
    stringBuilder.append("|emulator=");
    stringBuilder.append(getEmulatorDifference());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

  private Difference getBrowserDifference() {
    return new StringDifference(getDevice().getBrowserType().name());
  }

  private Difference getEmulatorDifference() {
    return new StringDifference(getDevice().getChromeEmulator());
  }

  private Difference getScreenSizeDifference() {
    return new IntegerDifference(getDevice().getScreenSize().getWidth());
  }

  private boolean hasDevice() {
    return getDevice() != null;
  }

  private void initializeSubDifferences() {
    differences = new MutableDifferences();
    if (hasDevice()) {
      differences.add(getBrowserDifference());
      if (StringUtils.isNotBlank(getDevice().getChromeEmulator())) {
        differences.add(getEmulatorDifference());
      }
      differences.add(getScreenSizeDifference());
    }
    else {
      differences.add(NO_DEVICE_DIFFERENCE);
    }
  }

  protected TestDevice getDevice() {
    if (device == null) {
      device = GaleniumContext.getTestDevice();
    }
    return device;
  }

  protected MutableDifferences getDifferences() {
    if (differences == null) {
      initializeSubDifferences();
    }
    return differences;
  }

  protected void setDevice(TestDevice device) {
    this.device = device;
  }
}
