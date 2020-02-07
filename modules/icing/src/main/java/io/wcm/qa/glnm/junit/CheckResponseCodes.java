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
package io.wcm.qa.glnm.junit;

import static io.wcm.qa.glnm.hamcrest.GaleniumAssert.assertResponses;
import static io.wcm.qa.glnm.hamcrest.LogEntryMatchers.hasStatus;
import static org.hamcrest.Matchers.everyItem;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.interaction.Wait;

/**
 * Test implementation checking the performance log.
 *
 * @since 5.0.0
 */
public interface CheckResponseCodes extends UsesChrome {

  /**
   * <p>
   * checkLogEntries.
   * </p>
   *
   * @param url a {@link java.lang.String} object.
   * @since 5.0.0
   */
  @ParameterizedTest
  @CsvFileSource(resources = { "performance-urls.csv" })
  default void checkLogEntries(String url) {
    Browser.load(url);
    Wait.forDomReady(5);
    assertResponses(everyItem(hasStatus(200)));
  }

}
