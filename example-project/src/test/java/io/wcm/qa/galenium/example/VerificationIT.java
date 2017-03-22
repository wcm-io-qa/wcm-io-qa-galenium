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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.galenium.example.pageobjects.ConferencePage;
import io.wcm.qa.galenium.example.pageobjects.Homepage;
import io.wcm.qa.galenium.listeners.RetryAnalyzer;
import io.wcm.qa.galenium.sampling.differences.BrowserDifference;
import io.wcm.qa.galenium.sampling.differences.ScreenWidthDifference;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;
import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.verification.CssClassVerification;
import io.wcm.qa.galenium.verification.CurrentUrlVerification;
import io.wcm.qa.galenium.verification.InvisibilityVerification;
import io.wcm.qa.galenium.verification.LinkTargetVerification;
import io.wcm.qa.galenium.verification.NoCssClassVerification;
import io.wcm.qa.galenium.verification.PageTitleVerification;
import io.wcm.qa.galenium.verification.Verification;
import io.wcm.qa.galenium.verification.VisibilityVerification;
import io.wcm.qa.galenium.verification.VisualVerification;

public class VerificationIT extends AbstractExampleBase {

  private static final String CSS_CLASS_NAVLINK_ACTIVE = "navlink-active";
  private static final Selector SELECTOR_LOGO = SelectorFactory.fromCss("Logo", "#top");
  private static final Selector SELECTOR_NAV_LINK_CONFERENCE = SelectorFactory.fromCss(
      "Navlink Conference",
      ".navlist-main a.navlink-main[href$='" + ConferencePage.PATH_TO_CONFERENCE + "'");
  private static final Selector SELECTOR_NAV_LINK_HOME = SelectorFactory.fromCss(
      "Navlink Home",
      ".navlist-main a.navlink-main[href$='" + Homepage.PATH_TO_HOMEPAGE + "'");
  private static final Selector SELECTOR_STAGE = SelectorFactory.fromCss("Stage", "#stage");

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = "devices")
  public VerificationIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test(groups = "dev", retryAnalyzer = RetryAnalyzer.class)
  public void verificationTest() {
    loadStartUrl();
    verify(new CurrentUrlVerification(getStartUrl()));
    verify(new PageTitleVerification("wcm.io Sample Site"));
    verify(new LogoVerification(SELECTOR_LOGO));
    verify(new VisibilityVerification(SELECTOR_STAGE));
    if (isMobile()) {
      verify(new InvisibilityVerification(SELECTOR_NAV_LINK_HOME));
      verify(new InvisibilityVerification(SELECTOR_NAV_LINK_CONFERENCE));
    }
    else {
      verify(new CssClassVerification(SELECTOR_NAV_LINK_HOME, CSS_CLASS_NAVLINK_ACTIVE));
      verify(new NoCssClassVerification(SELECTOR_NAV_LINK_CONFERENCE, CSS_CLASS_NAVLINK_ACTIVE));
    }
    verify(new LinkTargetVerification(SELECTOR_LOGO, getDriver().getCurrentUrl()));
  }

  private void verify(Verification verification) {
    if (verification.verify()) {
      getLogger().info(MARKER_PASS, verification.getMessage());
    }
    else {
      fail(verification.getMessage(), verification.getException());
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
      setAllowedOffset(2);
    }
  }
}
