/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.Dimension;

/**
 * Basic Not-A-Browser test device.
 */
public class NoBrowser implements TestDevice {

  private static final List<String> NO_TAGS = Collections.emptyList();
  private static final Dimension NO_DIMENSION = new Dimension(-1, -1);

  @Override
  public BrowserType getBrowserType() {
    return BrowserType.NO_BROWSER;
  }

  @Override
  public String getChromeEmulator() {
    return null;
  }

  @Override
  public List<String> getTags() {
    return NO_TAGS;
  }

  @Override
  public String getName() {
    return getBrowserType().getBrowser();
  }

  @Override
  public Dimension getScreenSize() {
    return NO_DIMENSION;
  }

  /**
   * Factory method.
   * @return a test device
   */
  public static NoBrowser instance() {
    return new NoBrowser();
  }

}
