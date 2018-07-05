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

import static io.wcm.qa.galenium.util.VerificationUtil.verify;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.example.pageobjects.Homepage;
import io.wcm.qa.galenium.providers.TestDeviceProvider;
import io.wcm.qa.galenium.sampling.differences.BrowserDifference;
import io.wcm.qa.galenium.sampling.differences.ScreenWidthDifference;
import io.wcm.qa.galenium.selectors.common.Logo;
import io.wcm.qa.galenium.selectors.common.Navigation;
import io.wcm.qa.galenium.selectors.homepage.Stage;
import io.wcm.qa.galenium.util.VerificationUtil;
import io.wcm.qa.galenium.verification.CssClassVerification;
import io.wcm.qa.galenium.verification.CurrentUrlVerification;
import io.wcm.qa.galenium.verification.InvisibilityVerification;
import io.wcm.qa.galenium.verification.LinkTargetVerification;
import io.wcm.qa.galenium.verification.NoCssClassVerification;
import io.wcm.qa.galenium.verification.PageTitleVerification;
import io.wcm.qa.galenium.verification.VisibilityVerification;
import io.wcm.qa.galenium.verification.VisualVerification;
import io.wcm.qa.galenium.verification.base.Verification;

/**
 * Showcase {@link Verification} approach.
 */
public class VerificationIT extends AbstractExampleBase {

  private static final String CSS_CLASS_NAVLINK_ACTIVE = "navlink-active";

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = TestDeviceProvider.GALENIUM_TEST_DEVICES_ALL)
  public VerificationIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test(groups = "dev")
  public void verificationTest() {
    loadStartUrl();
    verify(new CurrentUrlVerification("Homepage"), new PageTitleVerification("Homepage"), new LogoVerification(),
        new VisibilityVerification(Stage.SELF));
    if (isMobile()) {
      verify(new InvisibilityVerification(Navigation.LINK_TO_HOMEPAGE),
          new InvisibilityVerification(Navigation.LINK_TO_CONFERENCE));
    }
    else {
      verify(new CssClassVerification(Navigation.LINK_TO_HOMEPAGE, CSS_CLASS_NAVLINK_ACTIVE),
          new NoCssClassVerification(Navigation.LINK_TO_CONFERENCE, CSS_CLASS_NAVLINK_ACTIVE));
    }
    VerificationUtil.verify(new LinkTargetVerification(Logo.SELF));
  }

  @Override
  protected String getRelativePath() {
    return new Homepage().getRelativePath();
  }

  private static final class LogoVerification extends VisualVerification {

    private LogoVerification() {
      super(Logo.SELF);
      addDifference(new BrowserDifference());
      addDifference(new ScreenWidthDifference());
      setAllowedOffset(3);
    }
  }
}
