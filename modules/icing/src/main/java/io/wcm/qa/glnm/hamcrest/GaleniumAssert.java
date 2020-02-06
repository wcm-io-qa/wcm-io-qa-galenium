/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;

import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.interaction.logs.LogEntryUtil;
import io.wcm.qa.glnm.interaction.logs.ResponseEntry;

/**
 * Galenium's equivalent to Hamcrest's MatcherAssert.
 *
 * @since 5.0.0
 */
public final class GaleniumAssert {

  private GaleniumAssert() {
    // do not instantiate
  }

  /**
   * Assert WebDriver matcher matches.
   *
   * @param driverMatcher a webdriver matcher
   */
  public static void assertThat(Matcher<? super WebDriver> driverMatcher) {
    MatcherAssert.assertThat(GaleniumContext.getDriver(), driverMatcher);
  }

  /**
   * <p>assertResponses.</p>
   *
   * @param responseMatcher will be matching response entries from performance log
   */
  public static void assertResponses(Matcher<? super Iterable<ResponseEntry>> responseMatcher) {
    MatcherAssert.assertThat(LogEntryUtil.getResponseEntries(), responseMatcher);
  }

}
