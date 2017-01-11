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
package io.wcm.qa.galenium.log4j.appender;

import io.wcm.qa.galenium.reporting.GalenReportUtil;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.spi.StandardLevel;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * {@link Appender} logging to {@link ExtentReports}.
 */
public class ExtentAppender extends AbstractAppender {

  private static final Level CUSTOM_LEVEL_PASS = Level.forName(LogStatus.PASS.name(), 280);
  private static final Level CUSTOM_LEVEL_SKIP = Level.forName(LogStatus.SKIP.name(), 250);
  private static final Level CUSTOM_LEVEL_FAIL = Level.forName(LogStatus.FAIL.name(), 220);
  private ExtentTest extentTest;

  /**
   * Constructor that defaults to suppressing exceptions.
   * @param name The Appender name.
   * @param filter The Filter to associate with the Appender.
   * @param layout The layout to use to format the event.
   */
  protected ExtentAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout);
  }

  /**
   * Constructor.
   * @param name The Appender name.
   * @param filter The Filter to associate with the Appender.
   * @param layout The layout to use to format the event.
   * @param ignoreExceptions If true, exceptions will be logged and suppressed. If false errors will be logged and
   *          then passed to the application.
   */
  protected ExtentAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
      final boolean ignoreExceptions) {
    super(name, filter, layout, ignoreExceptions);
  }


  @Override
  public void append(LogEvent event) {
    LogStatus status = getStatusFromEvent(event);
    String message = event.getMessage().getFormattedMessage();
    event.getMarker();
    getTest().log(status, message);
  }

  private LogStatus getStatusFromEvent(LogEvent event) {
    if (event.getMarker() != null) {
      Marker marker = event.getMarker();

    }
    Level level = event.getLevel();
    if (isPass(level)) {
      return LogStatus.PASS;
    }
    if (isSkip(level)) {
      return LogStatus.SKIP;
    }
    if (isFail(level)) {
      return LogStatus.FAIL;
    }
    StandardLevel standardLevel = level.getStandardLevel();
    switch (standardLevel) {
      case DEBUG:
        return LogStatus.UNKNOWN;
      case ERROR:
        return LogStatus.ERROR;
      case FATAL:
        return LogStatus.FATAL;
      case INFO:
        return LogStatus.INFO;
      case TRACE:
        return LogStatus.UNKNOWN;
      case WARN:
        return LogStatus.WARNING;
      case ALL:
      case OFF:
      default:
        return LogStatus.UNKNOWN;
    }
  }

  private LogStatus getStatusFromEvent(Level level) {
    if (isPass(level)) {
      return LogStatus.PASS;
    }
    if (isSkip(level)) {
      return LogStatus.SKIP;
    }
    if (isFail(level)) {
      return LogStatus.FAIL;
    }
    StandardLevel standardLevel = level.getStandardLevel();
    switch (standardLevel) {
      case DEBUG:
        return LogStatus.UNKNOWN;
      case ERROR:
        return LogStatus.ERROR;
      case FATAL:
        return LogStatus.FATAL;
      case INFO:
        return LogStatus.INFO;
      case TRACE:
        return LogStatus.UNKNOWN;
      case WARN:
        return LogStatus.WARNING;
      case ALL:
      case OFF:
      default:
        return LogStatus.UNKNOWN;
    }
  }

  private boolean isFail(Level level) {
    return level == CUSTOM_LEVEL_FAIL;
  }

  private boolean isSkip(Level level) {
    return level == CUSTOM_LEVEL_SKIP;
  }

  private boolean isPass(Level level) {
    return level == CUSTOM_LEVEL_PASS;
  }

  private ExtentTest getTest() {
    if (extentTest == null) {
      extentTest = GalenReportUtil.getExtentReports().startTest(getName(), getDescription());
    }
    return extentTest;
  }

  protected String getDescription() {
    return "";
  }


}
