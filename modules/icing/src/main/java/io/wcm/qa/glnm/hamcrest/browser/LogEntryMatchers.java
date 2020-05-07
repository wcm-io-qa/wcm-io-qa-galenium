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
package io.wcm.qa.glnm.hamcrest.browser;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.wcm.qa.glnm.interaction.logs.ResponseEntry;

/**
 * <p>LogEntryMatchers class.</p>
 *
 * @since 5.0.0
 */
public final class LogEntryMatchers {

  private LogEntryMatchers() {
    // do not instantiate
  }

  /**
   * <p>hasResponseStatus.</p>
   *
   * @param status a int.
   * @return a {@link org.hamcrest.Matcher} object.
   * @since 5.0.0
   */
  public static Matcher<ResponseEntry> hasStatus(int status) {
    return new TypeSafeMatcher<ResponseEntry>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("response has status ");
        description.appendValue(status);
      }

      @Override
      protected boolean matchesSafely(ResponseEntry item) {
        return item.getResponseStatus() == status;
      }
    };
  }
}
