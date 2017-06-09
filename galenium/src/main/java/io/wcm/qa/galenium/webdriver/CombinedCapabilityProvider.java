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


class CombinedCapabilityProvider extends CapabilityProvider {


  private CapabilityProvider p1;
  private CapabilityProvider p2;

  public CombinedCapabilityProvider(CapabilityProvider provider1, CapabilityProvider provider2) {
    getLogger().debug("Creating combined provider with " + p1 + " and " + p2);
    p1 = provider1;
    p2 = provider2;
  }

  @Override
  protected DesiredCapabilities getBrowserSpecificCapabilities() {
    getLogger().debug("Combining capablities of " + p1 + " and " + p2);
    return p1.getDesiredCapabilities().merge(p2.getDesiredCapabilities());
  }

}
