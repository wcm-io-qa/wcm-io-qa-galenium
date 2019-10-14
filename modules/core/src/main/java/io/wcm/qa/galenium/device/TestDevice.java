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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * DataModel for browser types, screen sizes and Galen tags. Used to initialize {@link org.openqa.selenium.WebDriver}.
 *
 * @since 1.0.0
 */
public interface TestDevice {

  /**
   * <p>getBrowserType.</p>
   *
   * @return browser to use for this device
   */
  BrowserType getBrowserType();

  /**
   * <p>getChromeEmulator.</p>
   *
   * @return chrome emulator string if set or null
   */
  String getChromeEmulator();

  /**
   * <p>getTags.</p>
   *
   * @return include tags to use in Galen tests and specs
   */
  List<String> getTags();

  /**
   * <p>getName.</p>
   *
   * @return test device name for use in logging and test case names
   */
  String getName();

  /**
   * <p>getScreenSize.</p>
   *
   * @return viewport size for this test device
   */
  Dimension getScreenSize();

}
