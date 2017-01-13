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

import io.wcm.qa.galenium.reporting.GalenReportUtil;

import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Logback appender for writing to {@link ExtentReports}.
 */
public class ExtentAppender extends AppenderBase<ILoggingEvent> {

  @Override
  protected void append(ILoggingEvent event) {
    ExtentTest extentTest = GalenReportUtil.getExtentTest(event.getLoggerName());
    extentTest.log(extractLogStatus(event), event.getFormattedMessage());
  }

  private LogStatus extractLogStatus(ILoggingEvent event) {
    if (isExtentReportSpecial(event)) {
      if (isPass(event)) {
        return LogStatus.PASS;
      }
      if (isSkip(event)) {
        return LogStatus.SKIP;
      }
      if (isFail(event)) {
        return LogStatus.FAIL;
      }
      if (isFatal(event)) {
        return LogStatus.FATAL;
      }
    }
    if (isError(event)) {
      return LogStatus.ERROR;
    }
    if (isWarn(event)) {
      return LogStatus.WARNING;
    }
    if (isInfo(event)) {
      return LogStatus.INFO;
    }
    return LogStatus.UNKNOWN;
  }

  private boolean hasMarker(ILoggingEvent event, Marker marker) {
    return event.getMarker().contains(marker);
  }

  private boolean isError(ILoggingEvent event) {
    return isLevel(event, Level.ERROR);
  }

  private boolean isExtentReportSpecial(ILoggingEvent event) {
    return hasMarker(event, GalenReportUtil.MARKER_EXTENT_REPORT);
  }

  private boolean isFail(ILoggingEvent event) {
    return hasMarker(event, GalenReportUtil.MARKER_FAIL);
  }

  private boolean isFatal(ILoggingEvent event) {
    return hasMarker(event, GalenReportUtil.MARKER_FAIL);
  }

  private boolean isInfo(ILoggingEvent event) {
    return isLevel(event, Level.INFO);
  }

  private boolean isLevel(ILoggingEvent event, Level info) {
    return event.getLevel().isGreaterOrEqual(info);
  }

  private boolean isPass(ILoggingEvent event) {
    return hasMarker(event, GalenReportUtil.MARKER_PASS);
  }

  private boolean isSkip(ILoggingEvent event) {
    return hasMarker(event, GalenReportUtil.MARKER_SKIP);
  }

  private boolean isWarn(ILoggingEvent event) {
    return isLevel(event, Level.WARN);
  }

}
