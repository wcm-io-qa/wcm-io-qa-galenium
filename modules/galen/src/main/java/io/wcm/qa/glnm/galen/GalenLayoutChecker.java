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
package io.wcm.qa.glnm.galen;


import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getDriver;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.galenframework.api.Galen;
import com.galenframework.browser.Browser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.Spec;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.suite.GalenPageAction;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationErrorException;
import com.galenframework.validation.ValidationListener;
import com.galenframework.validation.ValidationObject;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GalenLayoutException;
import io.wcm.qa.glnm.imagecomparison.IcValidationListener;
import io.wcm.qa.glnm.imagecomparison.IcsDefinition;
import io.wcm.qa.glnm.imagecomparison.IcsFactory;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

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
   * @param specDefinition {@link IcsDefinition} to generate spec to check
   * @return report on spec test
   */
  public static LayoutReport checkLayout(IcsDefinition specDefinition) {
    PageSpec spec = IcsFactory.getPageSpec(specDefinition);
    return checkLayout(specDefinition.getSectionName(), spec, getTestDevice(), GalenHelperUtil.getTags(), new IcValidationListener());
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
    SectionFilter tags = GalenHelperUtil.getTags();
    return checkLayout(testName, spec, tags);
  }

  /**
   * Checks Galen spec against current state of driver using the tags.
   * @param testName test name used as folder name in reports
   * @param spec Galen spec to check
   * @param tags tags to use to filter rules
   * @return report on spec test
   */
  public static LayoutReport checkLayout(String testName, PageSpec spec, SectionFilter tags) {
    ValidationListener validationListener = getValidationListener();
    return checkLayout(
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
    if (getLogger().isDebugEnabled()) {
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
      getLogger().debug(stringBuilder.toString());
    }
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

  private static ValidationListener getValidationListener() {
    if (getLogger().isTraceEnabled()) {
      return new TracingValidationListener();
    }
    return new DummyValidationListener();
  }

  private static final class DummyValidationListener implements ValidationListener {

    @Override
    public void onAfterObject(PageValidation pageValidation, String objectName) {
      // noop
    }

    @Override
    public void onAfterPageAction(GalenPageAction action) {
      // noop
    }

    @Override
    public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
      // noop
    }

    @Override
    public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
      // noop
    }

    @Override
    public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
      // noop
    }

    @Override
    public void onBeforePageAction(GalenPageAction action) {
      // noop
    }

    @Override
    public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
      // noop
    }

    @Override
    public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
      // noop
    }

    @Override
    public void onGlobalError(Exception e) {
      // noop
    }

    @Override
    public void onObject(PageValidation pageValidation, String objectName) {
      // noop
    }

    @Override
    public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
      // noop
    }

    @Override
    public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
      // noop
    }

    @Override
    public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
      // noop
    }

    @Override
    public void onSubLayout(PageValidation pageValidation, String objectName) {
      // noop
    }

    @Override
    public String toString() {
      return "Dummy Validation Listener";
    }
  }

  private static final class TracingValidationListener implements ValidationListener {

    @Override
    public void onAfterObject(PageValidation pageValidation, String objectName) {
      getLogger().trace("AfterObject(PageValidation pageValidation, String objectName)");
    }

    @Override
    public void onAfterPageAction(GalenPageAction action) {
      getLogger().trace("AfterPageAction(GalenPageAction action)");
    }

    @Override
    public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
      getLogger().trace("AfterSection(PageValidation pageValidation, PageSection pageSection)");
    }

    @Override
    public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
      getLogger().trace("AfterSpecGroup(PageValidation pageValidation, String specGroupName)");
    }

    @Override
    public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
      getLogger().trace("AfterSubLayout(PageValidation pageValidation, String objectName)");
    }

    @Override
    public void onBeforePageAction(GalenPageAction action) {
      getLogger().trace("BeforePageAction(GalenPageAction action)");
    }

    @Override
    public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
      getLogger().trace("BeforeSection(PageValidation pageValidation, PageSection pageSection)");
    }

    @Override
    public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
      getLogger().trace("BeforeSpec(PageValidation pageValidation, String objectName, Spec spec)");
    }

    @Override
    public void onGlobalError(Exception e) {
      getLogger().trace("GlobalError(Exception e)");
    }

    @Override
    public void onObject(PageValidation pageValidation, String objectName) {
      getLogger().trace("Object(PageValidation pageValidation, String objectName)");
    }

    @Override
    public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
      getLogger().trace("SpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult)");
    }

    @Override
    public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
      getLogger().trace("SpecGroup(PageValidation pageValidation, String specGroupName)");
    }

    @Override
    public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
      getLogger().trace("SpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult)");
    }

    @Override
    public void onSubLayout(PageValidation pageValidation, String objectName) {
      getLogger().trace("SubLayout(PageValidation pageValidation, String objectName)");
    }

    @Override
    public String toString() {
      return "Tracing Validation Listener";
    }
  }

}
