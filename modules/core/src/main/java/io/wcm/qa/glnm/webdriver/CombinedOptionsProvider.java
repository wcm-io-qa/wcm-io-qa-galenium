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

import org.openqa.selenium.MutableCapabilities;

import io.wcm.qa.glnm.exceptions.GaleniumException;

class CombinedOptionsProvider extends OptionsProvider<MutableCapabilities> {

  private OptionsProvider p1;
  private OptionsProvider p2;

  CombinedOptionsProvider(OptionsProvider provider1, OptionsProvider provider2) {
    p1 = provider1;
    p2 = provider2;
    getLogger().debug("Creating combined provider with " + p1 + " and " + p2);
    if (p1 == null || p2 == null) {
      throw new GaleniumException("cannot combine null OptionsProvider.");
    }
  }

  @Override
  protected MutableCapabilities getBrowserSpecificOptions() {
    getLogger().debug("Combining capablities of " + p1 + " and " + p2);
    return p1.getOptions().merge(p2.getOptions());
  }

  @Override
  protected void log(MutableCapabilities options) {
    getLogger().trace("combined options: " + options);
  }

  @Override
  protected MutableCapabilities newOptions() {
    return p1.newOptions();
  }

}
