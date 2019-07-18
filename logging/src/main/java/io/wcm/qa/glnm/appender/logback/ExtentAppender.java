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
package io.wcm.qa.glnm.appender.logback;

import org.slf4j.Marker;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Logback appender for writing to {@link ExtentReports}.
 */
public class ExtentAppender extends AppenderBase<ILoggingEvent> {

  private LogStatus extractLogStatus(ILoggingEvent event) {
    if (isExtentReportSpecial(event)) {
      if (hasMarkerFail(event)) {
        return LogStatus.FAIL;
      }
      if (hasMarkerFatal(event)) {
        return LogStatus.FATAL;
      }
      if (hasErrorMarker(event)) {
        return LogStatus.ERROR;
      }
      if (hasMarkerSkip(event)) {
        return LogStatus.SKIP;
      }
      if (hasWarnMarker(event)) {
        return LogStatus.WARNING;
      }
      if (hasMarkerPass(event)) {
        return LogStatus.PASS;
      }
      if (hasInfoMarker(event)) {
        return LogStatus.INFO;
      }
    }
    if (isLevelError(event)) {
      return LogStatus.ERROR;
    }
    if (isLevelWarn(event)) {
      return LogStatus.WARNING;
    }
    if (isLevelInfo(event)) {
      return LogStatus.INFO;
    }
    return LogStatus.UNKNOWN;
  }

  private String getStackTrace(IThrowableProxy throwableProxy) {
    StringBuilder stacktrace = new StringBuilder();
    ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(stacktrace, throwableProxy);
    stacktrace.append(CoreConstants.LINE_SEPARATOR);
    ThrowableProxyUtil.subjoinSTEPArray(stacktrace, 2, throwableProxy);
    return stacktrace.toString();
  }

  private boolean hasMarker(ILoggingEvent event, Marker marker) {
    if (event.getMarker() != null) {
      return event.getMarker().contains(marker);
    }
    return false;
  }

  private boolean hasMarkerFail(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_FAIL);
  }

  private boolean hasMarkerFatal(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_FAIL);
  }

  private boolean hasMarkerPass(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_PASS);
  }

  private boolean hasMarkerSkip(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_SKIP);
  }

  private boolean isExtentReportSpecial(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_EXTENT_REPORT);
  }

  private boolean isLevel(ILoggingEvent event, Level level) {
    if (event.getLevel() != null) {
      return event.getLevel().isGreaterOrEqual(level);
    }
    return false;
  }

  @Override
  protected void append(ILoggingEvent event) {
    ExtentTest extentTest = GaleniumReportUtil.getExtentTest(event.getLoggerName());
    StringBuilder formattedMessage = new StringBuilder()
        .append(event.getFormattedMessage());
    IThrowableProxy throwableProxy = event.getThrowableProxy();
    if (throwableProxy != null) {
      String stacktrace = getStackTrace(throwableProxy);
      formattedMessage
          .append("<pre>")
          .append(stacktrace)
          .append("</pre>");
    }
    extentTest.log(extractLogStatus(event), formattedMessage.toString());
  }

  protected boolean hasErrorMarker(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_ERROR);
  }

  protected boolean hasInfoMarker(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_INFO);
  }

  protected boolean hasWarnMarker(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_WARN);
  }

  protected boolean isLevelError(ILoggingEvent event) {
    return isLevel(event, Level.ERROR);
  }

  protected boolean isLevelInfo(ILoggingEvent event) {
    return isLevel(event, Level.INFO);
  }

  protected boolean isLevelWarn(ILoggingEvent event) {
    return isLevel(event, Level.WARN);
  }

}
