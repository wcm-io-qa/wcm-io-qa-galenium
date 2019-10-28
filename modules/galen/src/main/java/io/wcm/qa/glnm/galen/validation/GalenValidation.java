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
package io.wcm.qa.glnm.galen.validation;

import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getDriver;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.api.Galen;
import com.galenframework.browser.Browser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GalenLayoutException;
import io.wcm.qa.glnm.galen.GalenHelperUtil;
import io.wcm.qa.glnm.galen.imagecomparison.IcValidationListener;
import io.wcm.qa.glnm.galen.imagecomparison.IcsDefinition;
import io.wcm.qa.glnm.galen.imagecomparison.IcsFactory;
import io.wcm.qa.glnm.galen.specs.GalenSpecUtil;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility methods to run Galen layout checks from Selenium tests. Integration via
 * {@link io.wcm.qa.glnm.util.GaleniumContext}.
 * @since 4.0.0
 */
public final class GalenValidation {

  private static final Logger LOG = LoggerFactory.getLogger(GalenValidation.class);

  private GalenValidation() {
    // do not instantiate
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from section name of spec factory and used in reports
   *
   * @param specDefinition {@link io.wcm.qa.glnm.galen.imagecomparison.IcsDefinition} to generate spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport imageComparison(IcsDefinition specDefinition) {
    PageSpec spec = IcsFactory.getPageSpec(specDefinition);
    return check(specDefinition.getSectionName(), spec, getTestDevice(), GalenSpecUtil.getTags(), new IcValidationListener());
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from first section of spec and used in reports
   *
   * @param spec Galen spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(PageSpec spec) {
    return check(spec.getSections().get(0).getName(), spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName test name used in reports
   * @param spec Galen spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(String testName, PageSpec spec) {
    SectionFilter tags = GalenSpecUtil.getTags();
    return check(testName, spec, tags);
  }

  /**
   * Checks Galen spec against current state of driver using the tags.
   *
   * @param testName test name used in reports
   * @param spec Galen spec to check
   * @param tags tags to use to filter rules
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(String testName, PageSpec spec, SectionFilter tags) {
    ValidationListener validationListener = getValidationListener();
    return check(
        testName,
        spec,
        getTestDevice(),
        tags,
        validationListener,
        GalenHelperUtil.getBrowser(),
        GaleniumContext.getDriver());
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName test name in reports
   * @param spec Galen spec to check
   * @param device device to retrieve driver
   * @param tags tags to use
   * @param validationListener validation listener to use, can be null
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(String testName, PageSpec spec, TestDevice device,
      SectionFilter tags, ValidationListener validationListener) {
    return check(testName, spec, device, tags, validationListener, GalenHelperUtil.getBrowser(device), getDriver(device));
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName test name used in reports
   * @param specPath path to spec file
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(String testName, String specPath) {
    PageSpec spec = GalenSpecUtil.readSpec(GalenHelperUtil.getBrowser(), specPath, GalenSpecUtil.getTags());
    return check(testName, spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName testname, also used as foldernames in reports
   * @param specPath path to the Galen spec
   * @param device Device for the test
   * @param validationListener validation listener to use, can be null
   * @return report on spec test
   * @since 4.0.0
   */
  public static LayoutReport check(String testName, String specPath, TestDevice device, ValidationListener validationListener) {

    SectionFilter tags = GalenSpecUtil.getSectionFilter(device);
    PageSpec spec;
    spec = GalenSpecUtil.readSpec(device, specPath, tags);

    return check(testName, spec, device, tags, validationListener);
  }

  private static LayoutReport check(String testName, PageSpec spec, TestDevice device, SectionFilter tags, ValidationListener validationListener,
      Browser browser, WebDriver driver) {
    if (LOG.isDebugEnabled()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("checking layout with: ('name' : '");
      stringBuilder.append(testName);
      stringBuilder.append("', 'spec' : '");
      stringBuilder.append(spec);
      stringBuilder.append("', 'device' : '");
      stringBuilder.append(device);
      stringBuilder.append("', 'included' : '");
      stringBuilder.append(tags.getIncludedTags());
      stringBuilder.append("', 'excluded' : '");
      stringBuilder.append(tags.getExcludedTags());
      stringBuilder.append("', 'listener' : '");
      stringBuilder.append(validationListener);
      stringBuilder.append("')<br/>on URL: '");
      stringBuilder.append(GaleniumContext.getDriver().getCurrentUrl());
      stringBuilder.append("'");
      LOG.debug(stringBuilder.toString());
    }
    LayoutReport layoutReport;
    try {
      layoutReport = Galen.checkLayout(browser, spec, tags, validationListener);
    }
    catch (IOException ex) {
      LOG.error("IOException with layout checking", ex);
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
      LOG.error(msg);
    }

    return layoutReport;
  }

  private static ValidationListener getValidationListener() {
    if (LOG.isTraceEnabled()) {
      return new TracingValidationListener();
    }
    return new NoOpValidationListener();
  }

}
