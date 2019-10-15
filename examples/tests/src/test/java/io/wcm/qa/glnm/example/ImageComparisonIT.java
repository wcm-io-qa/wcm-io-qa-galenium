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
package io.wcm.qa.glnm.example;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.differences.difference.driver.BrowserDifference;
import io.wcm.qa.glnm.differences.difference.driver.ScreenWidthDifference;
import io.wcm.qa.glnm.example.selectors.common.Page;
import io.wcm.qa.glnm.example.selectors.homepage.Stage;
import io.wcm.qa.glnm.providers.TestDeviceProvider;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.VisualVerification;
import io.wcm.qa.glnm.verification.util.Check;

/**
 * Example of how to use the {@link VisualVerification} to compare individual elements on a page.
 */
public class ImageComparisonIT extends AbstractExampleBase {

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = TestDeviceProvider.GALENIUM_TEST_DEVICES_ALL)
  public ImageComparisonIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test
  public void compareSomeImages() {
    loadStartUrl();
    checkVisually(Stage.SELF);
    checkVisually(Page.LOGO);
  }

  private void checkVisually(Selector selector) {
    // get factory for comparing element
    VisualVerification verification = new VisualVerification(selector);

    // add a no tolerance check at warning level
    verification.setZeroToleranceWarning(true);

    // allow error percent
    verification.setAllowedErrorPercent(2.0);

    // allow offset
    verification.setAllowedOffset(2);

    // browser and viewport width will make a difference
    verification.addDifference(new BrowserDifference());
    verification.addDifference(new ScreenWidthDifference());

    // compare image using spec
    Check.verify(verification);
  }

  @Override
  protected String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

}
