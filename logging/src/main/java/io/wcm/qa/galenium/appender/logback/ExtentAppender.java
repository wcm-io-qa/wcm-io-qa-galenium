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
package io.wcm.qa.galenium.appender.logback;

import org.slf4j.Marker;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Logback appender for writing to {@link ExtentReports}.
 */
public class ExtentAppender extends AppenderBase<ILoggingEvent> {

  @Override
  protected void append(ILoggingEvent event) {
    ExtentTest extentTest = GaleniumReportUtil.getExtentTest(event.getLoggerName());
    String formattedMessage = event.getFormattedMessage();
    IThrowableProxy throwableProxy = event.getThrowableProxy();
    if (throwableProxy != null) {
      StringBuilder sb = new StringBuilder();
      ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(sb, throwableProxy);
      sb.append(CoreConstants.LINE_SEPARATOR);
      ThrowableProxyUtil.subjoinSTEPArray(sb, 2, throwableProxy);
      String stacktrace = sb.toString();
      formattedMessage += "<pre>" + stacktrace + "</pre>";
    }
    extentTest.log(extractStatus(event), formattedMessage);
  }

  private Status extractStatus(ILoggingEvent event) {
    if (isExtentReportSpecial(event)) {
      if (isPass(event)) {
        return Status.PASS;
      }
      if (isSkip(event)) {
        return Status.SKIP;
      }
      if (isFail(event)) {
        return Status.FAIL;
      }
      if (isFatal(event)) {
        return Status.FATAL;
      }
      if (isError(event)) {
        return Status.ERROR;
      }
      if (isWarn(event)) {
        return Status.WARNING;
      }
      if (isInfo(event)) {
        return Status.INFO;
      }
    }
    if (isError(event)) {
      return Status.ERROR;
    }
    if (isWarn(event)) {
      return Status.WARNING;
    }
    if (isInfo(event)) {
      return Status.INFO;
    }

    // default to INFO
    return Status.INFO;
  }

  private boolean hasMarker(ILoggingEvent event, Marker marker) {
    if (event.getMarker() != null) {
      return event.getMarker().contains(marker);
    }
    return false;
  }

  private boolean isError(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_ERROR) || isLevel(event, Level.ERROR);
  }

  private boolean isExtentReportSpecial(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_EXTENT_REPORT);
  }

  private boolean isFail(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_FAIL);
  }

  private boolean isFatal(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_FAIL);
  }

  private boolean isInfo(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_INFO) || isLevel(event, Level.INFO);
  }

  private boolean isLevel(ILoggingEvent event, Level level) {
    if (event.getLevel() != null) {
      return event.getLevel().isGreaterOrEqual(level);
    }
    return false;
  }

  private boolean isPass(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_PASS);
  }

  private boolean isSkip(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_SKIP);
  }

  private boolean isWarn(ILoggingEvent event) {
    return hasMarker(event, GaleniumReportUtil.MARKER_WARN) || isLevel(event, Level.WARN);
  }

}
