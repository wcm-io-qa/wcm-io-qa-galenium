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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.galenium.webdriver.WebDriverManager.getDriver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.galenframework.browser.Browser;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.exceptions.GalenLayoutException;

public final class GalenHelperUtil {

  private static final Map<String, Object> EMPTY_JS_VARS = null;
  private static final Properties EMPTY_PROPERTIES = new Properties();
  private static final List<String> EMPTY_TAG_LIST = Collections.emptyList();
  private static final PageSpecReader PAGE_SPEC_READER = new PageSpecReader();

  private GalenHelperUtil() {
    // do not instantiate
  }


  public static Browser getBrowser() {
    return new SeleniumBrowser(GaleniumContext.getDriver());
  }

  public static Browser getBrowser(TestDevice device) {
    return new SeleniumBrowser(getDriver(device));
  }

  public static SectionFilter getSectionFilter(TestDevice device) {
    return new SectionFilter(device.getTags(), EMPTY_TAG_LIST);
  }

  public static SectionFilter getTags() {
    SectionFilter tags = new SectionFilter(getTestDevice().getTags(), Collections.emptyList());
    return tags;
  }

  public static PageSpec readSpec(Browser browser, String specPath, SectionFilter tags) {
    try {
      return PAGE_SPEC_READER.read(specPath, browser.getPage(), tags, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
    }
    catch (IOException ex) {
      throw new GalenLayoutException("IOException when reading spec", ex);
    }
  }

  public static PageSpec readSpec(String specPath) {
    return readSpec(getBrowser(), specPath, getTags());
  }

  public static PageSpec readSpec(TestDevice device, String specPath, SectionFilter tags) {
    return readSpec(getBrowser(device), specPath, tags);
  }


}
