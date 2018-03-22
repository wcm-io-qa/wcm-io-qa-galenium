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

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;


class FirefoxCapabilityProvider extends CapabilityProvider {

  @Override
  protected DesiredCapabilities getBrowserSpecificCapabilities() {
    getLogger().debug("creating capabilities for Firefox");
    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    firefoxProfile.setAcceptUntrustedCertificates(true);
    firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
    capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
    return capabilities;
  }

}
