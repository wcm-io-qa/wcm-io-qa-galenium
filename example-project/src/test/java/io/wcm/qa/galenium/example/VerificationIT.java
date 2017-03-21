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
package io.wcm.qa.galenium.example;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.galenium.example.pageobjects.Homepage;
import io.wcm.qa.galenium.listeners.RetryAnalyzer;
import io.wcm.qa.galenium.sampling.differences.BrowserDifference;
import io.wcm.qa.galenium.sampling.differences.ScreenWidthDifference;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;
import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.verification.Verification;
import io.wcm.qa.galenium.verification.VisibilityVerification;
import io.wcm.qa.galenium.verification.VisualVerification;

public class VerificationIT extends AbstractExampleBase {

  private static final Selector SELECTOR_LOGO = SelectorFactory.fromCss("Logo", "#top");
  private static final Selector SELECTOR_STAGE = SelectorFactory.fromCss("Stage", "#stage");
  private static final Verification VERIFICATION_STAGE_VISIBLE = new VisibilityVerification(SELECTOR_STAGE);

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = "devices")
  public VerificationIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test(groups = "dev", retryAnalyzer = RetryAnalyzer.class)
  public void verificationTest() {
    loadStartUrl();
    verify(new LogoVerification(SELECTOR_LOGO));
    verify(VERIFICATION_STAGE_VISIBLE);
  }

  private void verify(Verification verification) {
    if (verification.verify()) {
      getLogger().info(MARKER_PASS, verification.getMessage());
    }
    else {
      getLogger().warn(MARKER_FAIL, verification.getMessage());
    }
  }

  @Override
  protected String getRelativePath() {
    return new Homepage().getRelativePath();
  }

  private static final class LogoVerification extends VisualVerification {

    private LogoVerification(Selector selector) {
      super(selector);
      addDifference(new BrowserDifference());
      addDifference(new ScreenWidthDifference());
    }
  }
}
