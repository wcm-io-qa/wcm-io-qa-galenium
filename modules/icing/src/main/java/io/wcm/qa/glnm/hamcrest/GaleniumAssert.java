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

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;

import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.interaction.logs.LogEntryUtil;
import io.wcm.qa.glnm.interaction.logs.ResponseEntry;
import io.wcm.qa.glnm.persistence.Persistence;
import io.wcm.qa.glnm.sampling.Sampler;

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
   * @since 5.0.0
   */
  public static void assertThat(Matcher<? super WebDriver> driverMatcher) {
    MatcherAssert.assertThat(GaleniumContext.getDriver(), driverMatcher);
  }

  /**
   * Assert matcher and persist boolean sample on failure.
   *
   * @param sampler to obtain sample
   * @param matcher matching and differentiating
   * @since 5.0.0
   */
  public static void assertBoolean(Sampler<Boolean> sampler, DifferentiatingMatcher<Boolean> matcher) {
    Boolean sampleValue = sampler.sampleValue();
    try {
      MatcherAssert.assertThat(sampleValue, matcher);
    }
    catch (AssertionError e) {
      Persistence.forBoolean(matcher.getClass()).writer().writeSample(matcher, sampleValue);
      throw e;
    }
  }

  /**
   * Assert matcher and persist integer sample on failure.
   *
   * @param sampler to obtain sample
   * @param matcher matching and differentiating
   * @since 5.0.0
   */
  public static void assertInteger(Sampler<Integer> sampler, DifferentiatingMatcher<Integer> matcher) {
    Integer sampleValue = sampler.sampleValue();
    try {
      MatcherAssert.assertThat(sampleValue, matcher);
    }
    catch (AssertionError e) {
      Persistence.forInteger(matcher.getClass()).writer().writeSample(matcher, sampleValue);
      throw e;
    }
  }

  /**
   * Assert matcher and persist string sample on failure.
   *
   * @param sampler to obtain sample
   * @param matcher matching and differentiating
   * @since 5.0.0
   */
  public static void assertString(Sampler<String> sampler, DifferentiatingMatcher<String> matcher) {
    String sampleValue = sampler.sampleValue();
    try {
      MatcherAssert.assertThat(sampleValue, matcher);
    }
    catch (AssertionError e) {
      Persistence.forString(matcher.getClass()).writer().writeSample(matcher, sampleValue);
      throw e;
    }
  }

  /**
   * Assert matcher and persist string list sample on failure.
   *
   * @param sampler to obtain sample
   * @param matcher matching and differentiating
   * @since 5.0.0
   */
  public static void assertStringList(Sampler<List<String>> sampler, DifferentiatingMatcher<List<String>> matcher) {
    List<String> sampleValue = sampler.sampleValue();
    try {
      MatcherAssert.assertThat(sampleValue, matcher);
    }
    catch (AssertionError e) {
      Persistence.forStringList(matcher.getClass()).writer().writeSample(matcher, sampleValue);
      throw e;
    }
  }

  /**
   * <p>
   * assertResponses.
   * </p>
   *
   * @param responseMatcher will be matching response entries from performance log
   * @since 5.0.0
   */
  public static void assertResponses(Matcher<? super Iterable<ResponseEntry>> responseMatcher) {
    MatcherAssert.assertThat(LogEntryUtil.getResponseEntries(), responseMatcher);
  }

}
