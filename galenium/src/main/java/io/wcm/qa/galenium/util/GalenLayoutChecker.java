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

import static io.wcm.qa.galenium.webdriver.WebDriverManager.getDriver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.api.Galen;
import com.galenframework.browser.Browser;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * Utility methods to run Galen layout checks from Selenium tests. Integration through {@link WebDriverManager}.
 */
public final class GalenLayoutChecker {

  private static final PageSpecReader PAGE_SPEC_READER = new PageSpecReader();
  private static final Properties EMPTY_PROPERTIES = new Properties();
  private static final List<String> EMPTY_TAG_LIST = Collections.emptyList();
  private static final Logger log = LoggerFactory.getLogger(GalenLayoutChecker.class);
  private static final Map<String, Object> EMPTY_JS_VARS = null;

  private GalenLayoutChecker() {
    // do not instantiate
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

    SectionFilter tags = getSectionFilter(device);
    PageSpec spec;
    try {
      spec = readSpec(specPath, device, tags);
    }
    catch (IOException ex) {
      throw new GalenLayoutException("IOException when reading spec", ex);
    }

    return checkLayout(testName, spec, device, tags, validationListener);
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
    LayoutReport layoutReport;
    try {
      layoutReport = Galen.checkLayout(getBrowser(device), spec, tags, validationListener);
    }
    catch (IOException ex) {
      log.error("IOException with layout checking", ex);
      throw new GalenLayoutException("IOException with layout checking", ex);
    }

    // Creating an object that will contain the information about the test
    GalenTestInfo test = GalenTestInfo.fromString("Layoutcheck " + testName + " " + device.toString());

    // Adding layout report to the test report
    test.getReport().layout(layoutReport, "check layout on " + getDriver(device).getCurrentUrl() + " with device: " + device.toString());

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
      log.error(msg);
    }

    return layoutReport;
  }

  private static Browser getBrowser(TestDevice device) {
    return new SeleniumBrowser(getDriver(device));
  }

  protected static SectionFilter getSectionFilter(TestDevice device) {
    return new SectionFilter(device.getTags(), EMPTY_TAG_LIST);
  }

  private static PageSpec readSpec(String specPath, TestDevice device, SectionFilter tags) throws IOException {
    PageSpec spec = PAGE_SPEC_READER.read(specPath, getBrowser(device).getPage(), tags, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
    return spec;
  }

  /**
   * Wrapper exception for Galen layout problems.
   */
  public static class GalenLayoutException extends RuntimeException {

    private static final long serialVersionUID = -152759653372481359L;

    /**
     * @see RuntimeException
     * @param message message
     * @param ex original exception
     */
    public GalenLayoutException(String message, Throwable ex) {
      super(message, ex);
    }

  }

}
