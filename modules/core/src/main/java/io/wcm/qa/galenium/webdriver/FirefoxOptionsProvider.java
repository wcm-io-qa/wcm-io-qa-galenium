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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;


class FirefoxOptionsProvider extends OptionsProvider<FirefoxOptions> {

  private static final String SYSTEM_PROPERTY_NAME_WEBDRIVER_FIREFOX_BIN = "webdriver.firefox.bin";

  @Override
  protected FirefoxOptions getBrowserSpecificOptions() {
    getLogger().debug("creating capabilities for Firefox");
    FirefoxOptions options = newOptions();
    addProfile(options);
    setHeadless(options);
    setBinary(options);
    return options;
  }

  private void setBinary(FirefoxOptions options) {
    String firefoxBin = System.getProperty(SYSTEM_PROPERTY_NAME_WEBDRIVER_FIREFOX_BIN);
    if (StringUtils.isNotBlank(firefoxBin)) {
      getLogger().debug("webdriver.firefox.bin: '" + firefoxBin + "'");
      File binaryFile = new File(firefoxBin);
      if (binaryFile.isFile()) {
        getLogger().trace("Setting explicit binary for Firefox: '" + binaryFile.getPath() + "'");
        FirefoxBinary binary = new FirefoxBinary(binaryFile);
        options.setBinary(binary);
      }
      else {
        getLogger().warn("Skipping explicit binary for Firefox because it is not a file: '" + binaryFile.getPath() + "'");
      }
    }
  }

  private void addProfile(FirefoxOptions options) {
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    firefoxProfile.setAcceptUntrustedCertificates(true);
    firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
    options.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
  }

  private void setHeadless(FirefoxOptions options) {
    boolean headless = GaleniumConfiguration.isHeadless();
    options.setHeadless(headless);
  }

  @Override
  protected FirefoxOptions newOptions() {
    return new FirefoxOptions();
  }

}
