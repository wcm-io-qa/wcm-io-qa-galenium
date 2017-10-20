/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.galenium.webdriver.WebDriverManager.getDriver;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.galenframework.api.Galen;
import com.galenframework.browser.Browser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationErrorException;
import com.galenframework.validation.ValidationListener;
import com.galenframework.validation.ValidationObject;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.galenium.exceptions.GalenLayoutException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.images.ImageComparisonSpecFactory;
import io.wcm.qa.galenium.sampling.images.ImageComparisonValidationListener;

/**
 * Utility methods to run Galen layout checks from Selenium tests. Integration via {@link GaleniumContext}.
 */
public final class GalenLayoutChecker {

  private GalenLayoutChecker() {
    // do not instantiate
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from section name of spec factory and used as folder name in reports
   * @param specFactory {@link ImageComparisonSpecFactory} to generate spec to check
   * @return report on spec test
   */
  public static LayoutReport checkLayout(ImageComparisonSpecFactory specFactory) {
    PageSpec spec = specFactory.getPageSpecInstance();
    return checkLayout(specFactory.getSectionName(), spec, getTestDevice(), GalenHelperUtil.getTags(), specFactory.getValidationListener());
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from first section of spec and used as folder name in reports
   * @param spec Galen spec to check
   * @return report on spec test
   */
  public static LayoutReport checkLayout(PageSpec spec) {
    return checkLayout(spec.getSections().get(0).getName(), spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   * @param testName test name used as folder name in reports
   * @param spec Galen spec to check
   * @return report on spec test
   */
  public static LayoutReport checkLayout(String testName, PageSpec spec) {
    return checkLayout(
        testName,
        spec,
        getTestDevice(),
        GalenHelperUtil.getTags(),
        new ImageComparisonValidationListener(),
        GalenHelperUtil.getBrowser(),
        GaleniumContext.getDriver());
  }

  /**
   * Checks Galen spec against current state of driver.
   * @param testName test name used as folder name in reports
   * @param spec Galen spec to check
   * @param device device to retrieve driver
   * @param tags tags to use
   * @param validationListener validation listener to use, can be null
   * @return report on spec test
   */
  public static LayoutReport checkLayout(String testName, PageSpec spec, TestDevice device,
      SectionFilter tags, ValidationListener validationListener) {
    return checkLayout(testName, spec, device, tags, validationListener, GalenHelperUtil.getBrowser(device), getDriver(device));
  }

  /**
   * Checks Galen spec against current state of driver.
   * @param testName test name used as folder name in reports
   * @param specPath path to spec file
   * @return report on spec test
   */
  public static LayoutReport checkLayout(String testName, String specPath) {
    PageSpec spec = GalenHelperUtil.readSpec(GalenHelperUtil.getBrowser(), specPath, GalenHelperUtil.getTags());
    return checkLayout(testName, spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   * @param testName testname, also used as foldernames in reports
   * @param specPath path to the Galen spec
   * @param device Device for the test
   * @param validationListener validation listener to use, can be null
   * @return report on spec test
   */
  public static LayoutReport checkLayout(String testName, String specPath, TestDevice device, ValidationListener validationListener) {

    SectionFilter tags = GalenHelperUtil.getSectionFilter(device);
    PageSpec spec;
      spec = GalenHelperUtil.readSpec(device, specPath, tags);

    return checkLayout(testName, spec, device, tags, validationListener);
  }

  /**
   * @param layoutReport Galen layout report
   * @param errorMessage message to use for errors and failures
   * @param successMessage message to use in case of success
   */
  public static void handleLayoutReport(LayoutReport layoutReport, String errorMessage, String successMessage) {
    if (!(layoutReport.errors() > 0 || layoutReport.warnings() > 0)) {
      getLogger().debug(MARKER_PASS, successMessage);
    }
    else {
      List<ValidationResult> validationErrorResults = layoutReport.getValidationErrorResults();
      for (ValidationResult validationResult : validationErrorResults) {
        ValidationError error = validationResult.getError();
        String errorMessages = StringUtils.join(error.getMessages(), "|");
        if (error.isOnlyWarn()) {
          getLogger().warn(errorMessages);
        }
        else {
          getLogger().warn(MARKER_FAIL, errorMessages);
        }
      }
      if (layoutReport.errors() > 0) {
        ValidationResult validationResult = layoutReport.getValidationErrorResults().get(0);
        List<String> messages = validationResult.getError().getMessages();
        List<ValidationObject> validationObjects = validationResult.getValidationObjects();
        ValidationErrorException ex = new ValidationErrorException(validationObjects, messages);
        throw new GalenLayoutException(errorMessage, ex);
      }
    }
  }

  private static LayoutReport checkLayout(String testName, PageSpec spec, TestDevice device, SectionFilter tags, ValidationListener validationListener,
      Browser browser, WebDriver driver) {
    LayoutReport layoutReport;
    try {
      layoutReport = Galen.checkLayout(browser, spec, tags, validationListener);
    }
    catch (IOException ex) {
      getLogger().error("IOException with layout checking", ex);
      throw new GalenLayoutException("IOException with layout checking", ex);
    }

    // Creating an object that will contain the information about the test
    GalenTestInfo test = GalenTestInfo.fromString("Layoutcheck " + testName + " " + device.getName());

    // Adding layout report to the test report
    test.getReport().layout(layoutReport, "check layout on " + driver.getCurrentUrl() + " with device: " + device.toString());

    GaleniumReportUtil.addGalenResult(test);

    if (layoutReport.errors() > 0) {
      String prettyStringResult;
      try {
        prettyStringResult = layoutReport.getValidationErrorResults().get(0).getSpec().getPlace().toPrettyString();
      }
      catch (NullPointerException ex) {
        prettyStringResult = "____NPE____";
      }
      String msg = "FAILED: Layoutcheck " + prettyStringResult + " with device "
          + device.toString();
      getLogger().error(msg);
    }

    return layoutReport;
  }

}
