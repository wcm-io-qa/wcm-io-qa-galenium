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

import java.time.Duration;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matcher waiting for chained matcher.
 *
 * @param <T> type to match
 * @since 5.0.0
 */
public class AfterWait<T> extends TypeSafeMatcher<T> {

  private static final int DEFAULT_WAIT_SECONDS = 5;

  private static final Logger LOG = LoggerFactory.getLogger(AfterWait.class);

  private Matcher<T> internalMatcher;
  private final int timeoutInSeconds;
  private String timeoutMessage;
  /**
   * Wait on Matcher for seconds.
   *
   * @param seconds to wait
   * @param matcher to wait for
   */
  public AfterWait(int seconds, Matcher<T> matcher) {
    timeoutInSeconds = seconds;
    internalMatcher = matcher;
  }

  /**
   * Wait for Matcher for 5 seconds.
   *
   * @param matcher to wait for
   */
  public AfterWait(Matcher<T> matcher) {
    this(DEFAULT_WAIT_SECONDS, matcher);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("after waiting ");
    description.appendValue(timeoutInSeconds);
    description.appendText(" seconds ");
    internalMatcher.describeTo(description);
  }

  private FluentWait<T> getFluentWait(T item) {
    FluentWait<T> fluentWait = new FluentWait<T>(item)
        .withTimeout(Duration.ofSeconds(timeoutInSeconds))
        .pollingEvery(Duration.ofMillis(timeoutInSeconds * 100));
    return fluentWait;
  }

  private Function<T, Boolean> getInternalMatcherAsFunction() {
    return new MatcherFunction<T>(internalMatcher);
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    super.describeMismatchSafely(item, mismatchDescription);
    if (StringUtils.isNotEmpty(timeoutMessage)) {
      mismatchDescription.appendText(System.lineSeparator());
      mismatchDescription.appendText(timeoutMessage);
    }
  }

  @Override
  protected boolean matchesSafely(T item) {
    try {
      timeoutMessage = "";
      FluentWait<T> wait = getFluentWait(item);
      return wait.until(getInternalMatcherAsFunction());
    }
    catch (TimeoutException ex) {
      timeoutMessage = ex.getMessage();
      if (LOG.isDebugEnabled()) {
        LOG.debug("when waiting for " + internalMatcher, ex);
      }
      return false;
    }
  }

  /**
   * Wait on Matcher for seconds.
   *
   * @param seconds to wait
   * @param matcher to wait for
   * @param <T> a T object.
   * @return a {@link org.hamcrest.Matcher} object.
   */
  public static <T> Matcher<T> afterWait(int seconds, Matcher<T> matcher) {
    return new AfterWait<T>(seconds, matcher);
  }


  /**
   * Wait on Matcher for 5 seconds.
   *
   * @param matcher to wait for
   * @param <T> a T object.
   * @return a {@link org.hamcrest.Matcher} object.
   */
  public static <T> Matcher<T> afterWait(Matcher<T> matcher) {
    return new AfterWait<T>(matcher);
  }

  private static final class MatcherFunction<T> implements Function<T, Boolean> {

    private final Matcher<T> matcher;

    MatcherFunction(Matcher<T> matcher) {
      this.matcher = matcher;
    }

    @Override
    public Boolean apply(T item) {
      return matcher.matches(item);
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append(matcher.toString())
          .append(" (")
          .append(matcher.getClass().getName())
          .append(")")
          .toString();
    }
  }
}
