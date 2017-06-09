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

import org.openqa.selenium.remote.DesiredCapabilities;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

class HeadlessChromeCapabilityProvider extends ChromeCapabilityProvider {

  private static final String[] ARGUMENTS_HEADLESS = new String[] {
      // main headless arg
      "headless",
      // workaround (https://developers.google.com/web/updates/2017/04/headless-chrome#cli)
      "disable-gpu",
      // workaround for windows: there is still a window opened, so put it somewhere offscreen
      "window-position=10000,0"
  };

  @Override
  protected DesiredCapabilities getBrowserSpecificCapabilities() {
    GaleniumReportUtil.getLogger().debug("setting up headless chrome.");
    return addChromeOption(super.getBrowserSpecificCapabilities(), OPTIONS_KEY_ARGS, ARGUMENTS_HEADLESS);
  }

}
