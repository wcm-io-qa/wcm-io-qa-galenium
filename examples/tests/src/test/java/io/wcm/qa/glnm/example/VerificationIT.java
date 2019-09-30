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

import static io.wcm.qa.glnm.verification.util.Check.verify;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.differences.difference.driver.BrowserDifference;
import io.wcm.qa.glnm.differences.difference.driver.ScreenWidthDifference;
import io.wcm.qa.glnm.example.selectors.common.Page;
import io.wcm.qa.glnm.example.selectors.common.Page.Navigation;
import io.wcm.qa.glnm.example.selectors.homepage.Stage;
import io.wcm.qa.glnm.providers.TestDeviceProvider;
import io.wcm.qa.glnm.verification.base.Verification;
import io.wcm.qa.glnm.verification.driver.TitleAndUrlVerification;
import io.wcm.qa.glnm.verification.element.CssClassVerification;
import io.wcm.qa.glnm.verification.element.InvisibilityVerification;
import io.wcm.qa.glnm.verification.element.LinkTargetVerification;
import io.wcm.qa.glnm.verification.element.NoCssClassVerification;
import io.wcm.qa.glnm.verification.element.VisibilityVerification;
import io.wcm.qa.glnm.verification.element.VisualVerification;
import io.wcm.qa.glnm.verification.util.Check;
import io.wcm.qa.glnm.example.pageobjects.Homepage;

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

    verify(new TitleAndUrlVerification("Homepage"), new LogoVerification(), new VisibilityVerification(Stage.SELF));

    if (isMobile()) {
      verify(new InvisibilityVerification(Navigation.LINK_TO_HOMEPAGE),
          new InvisibilityVerification(Navigation.LINK_TO_CONFERENCE));
    }
    else {
      verify(new CssClassVerification(Navigation.LINK_TO_HOMEPAGE, CSS_CLASS_NAVLINK_ACTIVE),
          new NoCssClassVerification(Navigation.LINK_TO_CONFERENCE, CSS_CLASS_NAVLINK_ACTIVE));
    }

    Check.verify(new LinkTargetVerification(Page.LOGO));
  }

  @Override
  protected String getRelativePath() {
    return new Homepage().getRelativePath();
  }

  private static final class LogoVerification extends VisualVerification {

    private LogoVerification() {
      super(Page.LOGO);
      addDifference(new BrowserDifference());
      addDifference(new ScreenWidthDifference());
      setAllowedOffset(3);
    }
  }

}
