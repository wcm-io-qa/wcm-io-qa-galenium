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
package io.wcm.qa.galenium.appender.log4j;


///**
// * {@link Appender} logging to {@link ExtentReports}.
// */
//public class ExtentAppender extends AbstractAppender {
//
//  /**
//   * Constructor that defaults to suppressing exceptions.
//   * @param name The Appender name.
//   * @param filter The Filter to associate with the Appender.
//   * @param layout The layout to use to format the event.
//   */
//  protected ExtentAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
//    super(name, filter, layout);
//  }
//
//  /**
//   * Constructor.
//   * @param name The Appender name.
//   * @param filter The Filter to associate with the Appender.
//   * @param layout The layout to use to format the event.
//   * @param ignoreExceptions If true, exceptions will be logged and suppressed. If false errors will be logged and
//   *          then passed to the application.
//   */
//  protected ExtentAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
//      final boolean ignoreExceptions) {
//    super(name, filter, layout, ignoreExceptions);
//  }
//
//  @Override
//  public void append(LogEvent event) {
//    LogStatus status = getStatusFromEvent(event);
//    getTest(event).log(status, event.getMessage().getFormattedMessage());
//  }
//
//  private LogStatus getStatusFromEvent(LogEvent event) {
//    if (isPass(event)) {
//      return LogStatus.PASS;
//    }
//    if (isSkip(event)) {
//      return LogStatus.SKIP;
//    }
//    if (isFail(event)) {
//      return LogStatus.FAIL;
//    }
//    if (isFatal(event)) {
//      return LogStatus.FATAL;
//    }
//    StandardLevel standardLevel = event.getLevel().getStandardLevel();
//    switch (standardLevel) {
//      case ERROR:
//        return LogStatus.ERROR;
//      case FATAL:
//        return LogStatus.FATAL;
//      case INFO:
//        return LogStatus.INFO;
//      case WARN:
//        return LogStatus.WARNING;
//      case TRACE:
//      case DEBUG:
//      case ALL:
//      case OFF:
//      default:
//        return LogStatus.UNKNOWN;
//    }
//  }
//
//  private boolean isFail(LogEvent event) {
//    return hasMarker(event, GalenReportUtil.MARKER_FAIL);
//  }
//
//  private boolean hasMarker(LogEvent event, Marker marker) {
//    return event.getMarker().isInstanceOf(marker.getName());
//  }
//
//  private boolean isSkip(LogEvent event) {
//    return hasMarker(event, GalenReportUtil.MARKER_SKIP);
//  }
//
//  private boolean isPass(LogEvent event) {
//    return hasMarker(event, GalenReportUtil.MARKER_PASS);
//  }
//
//  private boolean isFatal(LogEvent event) {
//    return hasMarker(event, GalenReportUtil.MARKER_FATAL);
//  }
//
//  private ExtentTest getTest(LogEvent event) {
//    return GalenReportUtil.getExtentTest(event.getLoggerName());
//  }
//
//}