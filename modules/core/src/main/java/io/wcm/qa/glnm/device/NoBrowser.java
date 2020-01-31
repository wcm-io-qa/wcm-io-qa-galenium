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
 *
 * @since 3.0.0
 */
public class NoBrowser implements TestDevice {

  private static final List<String> NO_TAGS = Collections.emptyList();
  private static final Dimension NO_DIMENSION = new Dimension(-1, -1);

  /** {@inheritDoc} */
  @Override
  public BrowserType getBrowserType() {
    return BrowserType.NO_BROWSER;
  }

  /** {@inheritDoc} */
  @Override
  public String getChromeEmulator() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public List<String> getTags() {
    return NO_TAGS;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return getBrowserType().getBrowser();
  }

  /** {@inheritDoc} */
  @Override
  public Dimension getScreenSize() {
    return NO_DIMENSION;
  }

  /**
   * Factory method.
   *
   * @return a test device
   */
  public static NoBrowser instance() {
    return new NoBrowser();
  }

}
