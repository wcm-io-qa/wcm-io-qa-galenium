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

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.galen.GalenLayoutChecker;
import io.wcm.qa.galenium.sampling.differences.BrowserDifference;
import io.wcm.qa.galenium.sampling.differences.ScreenWidthDifference;
import io.wcm.qa.galenium.sampling.images.ImageComparisonSpecFactory;
import io.wcm.qa.galenium.sampling.images.ImageComparisonValidationListener;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;

/**
 * Example of how to use the {@link ImageComparisonSpecFactory} to compare individual elements on a page.
 */
public class ImageComparisonExampleIT extends AbstractExampleBase {

  private static final Selector SELECTOR_LOGO = SelectorFactory.fromCss("Logo", "#top");
  private static final Selector SELECTOR_STAGE = SelectorFactory.fromCss("Stage", "#stage");

  @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = "devices")
  public ImageComparisonExampleIT(TestDevice testDevice) {
    super(testDevice);
  }

  @Test
  public void compareSomeImages() {
    loadStartUrl();
    checkVisually(SELECTOR_STAGE);
    checkVisually(SELECTOR_LOGO);
  }

  private void checkVisually(Selector selector) {
    // get factory for comparing element
    ImageComparisonSpecFactory factory = new ImageComparisonSpecFactory(selector);

    // add a no tolerance check at warning level
    factory.setZeroToleranceWarning(true);

    // allow offset
    factory.setAllowedOffset(2);

    // browser and viewport width will make a difference
    factory.addDifference(new BrowserDifference());
    factory.addDifference(new ScreenWidthDifference());

    // compare image using spec
    LayoutReport layoutReport = GalenLayoutChecker.checkLayout("Image comparison stage",
        factory.getPageSpecInstance(), getDevice(), new SectionFilter(getDevice().getTags(), null),
        getValidationListener());
    handleLayoutReport("image_comparison_" + selector.elementName() + ".gspec", layoutReport);
  }

  private ImageComparisonValidationListener getValidationListener() {
    return new ImageComparisonValidationListener();
  }

  @Override
  protected String getRelativePath() {
    return PATH_TO_HOMEPAGE;
  }

}
