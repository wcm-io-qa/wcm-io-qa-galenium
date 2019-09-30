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
package io.wcm.qa.glnm.webdriver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;

class ChromeEmulatorOptionsProvider extends ChromeOptionsProvider {

  private String chromeEmulator;

  ChromeEmulatorOptionsProvider(String emulatorString) {
    setChromeEmulator(emulatorString);
  }

  public String getChromeEmulator() {
    return chromeEmulator;
  }

  public void setChromeEmulator(String chromeEmulator) {
    this.chromeEmulator = chromeEmulator;
  }

  @Override
  protected ChromeOptions getBrowserSpecificOptions() {
    getLogger().debug("setting up chrome emulator: " + getChromeEmulator());
    Map<String, String> mobileEmulation = new HashMap<String, String>();
    mobileEmulation.put("deviceName", getChromeEmulator());
    ChromeOptions browserSpecificOptions = super.getBrowserSpecificOptions();
    browserSpecificOptions.setCapability("mobileEmulation", mobileEmulation);
    return browserSpecificOptions;
  }

}
