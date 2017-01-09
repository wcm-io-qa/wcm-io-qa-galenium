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
package io.wcm.qa.galenium.reporting;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;


class AbstractLoggingBase<D> {

  private D delegate;
  private StopWatch stopWatch = new StopWatch();
  protected GaleniumLogging loggingDelegate;

  /**
   * @param delegate object to delegate to
   * @param logging used for logging
   */
  AbstractLoggingBase(D delegate, GaleniumLogging logging) {
    setDelegate(delegate);
    loggingDelegate = logging;
    debugInfo("Started logging " + delegate.getClass().getSimpleName() + " for " + delegate);
  }

  private void debugInfo(String msg) {
    loggingDelegate.debugInfo(msg);
  }

  private void setDelegate(D delegate) {
    this.delegate = delegate;
  }

  private void startTimer() {
    stopWatch.reset();
    stopWatch.start();
  }

  private String stopTimer() {
    stopWatch.stop();
    String timeString = stopWatch.toString();
    String nanoSeconds = String.format("%03d", stopWatch.getNanoTime() % 1000);
    String resultString = timeString + nanoSeconds;
    long time = stopWatch.getTime();
    if (time < 1) {
      resultString = "<em style=\"color:lightgray;\">" + resultString + "</em>";
    }
    else if (time > 10000) {
      resultString = "<b style=\"color:red;\">" + resultString + "</b>";
    }
    else if (time > 2000) {
      resultString = "<b>" + resultString + "</b>";
    }
    else if (time > 1000) {
      resultString = "<i>" + resultString + "</i>";
    }
    else if (time > 500) {
      resultString = "<em style=\"color:gray;\">" + resultString + "</em>";
    }
    else {
      resultString = "<em style=\"color:darkgray;\">" + resultString + "</em>";
    }

    return resultString;
  }

  protected void debugStart(String methodName, Object... args) {
    startTimer();
    String argsString = StringUtils.join(args, ", ");
    debugInfo(getDelegate().getClass().getSimpleName() + "." + methodName + "(" + argsString + ")");
  }

  protected void debugStop() {
    debugStop(null);
  }

  protected void debugStop(Object returnValue) {
    String returnValueString = StringUtils.EMPTY;
    if (returnValue != null) {
      returnValueString = " -> '" + returnValue + "'";
    }
    debugInfo(stopTimer() + returnValueString);
  }

  protected D getDelegate() {
    return delegate;
  }

}
