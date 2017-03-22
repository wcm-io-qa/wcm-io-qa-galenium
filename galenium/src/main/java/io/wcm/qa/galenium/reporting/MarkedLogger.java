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

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MarkedLogger {

  private Logger logger;
  private Marker markerToAdd;

  public MarkedLogger(Logger delegateLogger, Marker additionalMarker) {
    logger = delegateLogger;
    markerToAdd = additionalMarker;
  }

  private Marker combine(Marker marker) {
    Marker combined = combine();
    combined.add(marker);
    return combined;
  }

  protected Marker combine() {
    Marker combiningMarker = MarkerFactory.getDetachedMarker("marked_logger");
    combiningMarker.add(markerToAdd);
    return combiningMarker;
  }

  public void debug(Marker marker, String msg) {
    logger.debug(combine(marker), msg);
  }

  public void debug(Marker marker, String format, Object arg) {
    logger.debug(combine(marker), format, arg);
  }

  public void debug(Marker marker, String format, Object... arguments) {
    logger.debug(combine(marker), format, arguments);
  }

  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    logger.debug(combine(marker), format, arg1, arg2);
  }

  public void debug(Marker marker, String msg, Throwable t) {
    logger.debug(combine(marker), msg, t);
  }

  public void debug(String msg) {
    logger.debug(combine(), msg);
  }

  public void debug(String format, Object arg) {
    logger.debug(combine(), format, arg);
  }

  public void debug(String format, Object... arguments) {
    logger.debug(combine(), format, arguments);
  }

  public void debug(String format, Object arg1, Object arg2) {
    logger.debug(combine(), format, arg1, arg2);
  }

  public void debug(String msg, Throwable t) {
    logger.debug(combine(), msg, t);
  }

  public void error(Marker marker, String msg) {
    logger.error(combine(marker), msg);
  }

  public void error(Marker marker, String format, Object arg) {
    logger.error(combine(marker), format, arg);
  }

  public void error(Marker marker, String format, Object... arguments) {
    logger.error(combine(marker), format, arguments);
  }

  public void error(Marker marker, String format, Object arg1, Object arg2) {
    logger.error(combine(marker), format, arg1, arg2);
  }

  public void error(Marker marker, String msg, Throwable t) {
    logger.error(combine(marker), msg, t);
  }

  public void error(String msg) {
    logger.error(combine(), msg);
  }

  public void error(String format, Object arg) {
    logger.error(combine(), format, arg);
  }

  public void error(String format, Object... arguments) {
    logger.error(combine(), format, arguments);
  }

  public void error(String format, Object arg1, Object arg2) {
    logger.error(combine(), format, arg1, arg2);
  }

  public void error(String msg, Throwable t) {
    logger.error(combine(), msg, t);
  }

  public String getName() {
    return logger.getName();
  }

  public void info(Marker marker, String msg) {
    logger.info(combine(marker), msg);
  }

  public void info(Marker marker, String format, Object arg) {
    logger.info(combine(marker), format, arg);
  }

  public void info(Marker marker, String format, Object... arguments) {
    logger.info(combine(marker), format, arguments);
  }

  public void info(Marker marker, String format, Object arg1, Object arg2) {
    logger.info(combine(marker), format, arg1, arg2);
  }

  public void info(Marker marker, String msg, Throwable t) {
    logger.info(combine(marker), msg, t);
  }

  public void info(String msg) {
    logger.info(combine(), msg);
  }

  public void info(String format, Object arg) {
    logger.info(combine(), format, arg);
  }

  public void info(String format, Object... arguments) {
    logger.info(combine(), format, arguments);
  }

  public void info(String format, Object arg1, Object arg2) {
    logger.info(combine(), format, arg1, arg2);
  }

  public void info(String msg, Throwable t) {
    logger.info(combine(), msg, t);
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled(combine());
  }

  public boolean isDebugEnabled(Marker marker) {
    return logger.isDebugEnabled(combine(marker));
  }

  public boolean isErrorEnabled() {
    return logger.isErrorEnabled(combine());
  }

  public boolean isErrorEnabled(Marker marker) {
    return logger.isErrorEnabled(combine(marker));
  }

  public boolean isInfoEnabled() {
    return logger.isInfoEnabled(combine());
  }

  public boolean isInfoEnabled(Marker marker) {
    return logger.isInfoEnabled(combine(marker));
  }

  public boolean isTraceEnabled() {
    return logger.isTraceEnabled(combine());
  }

  public boolean isTraceEnabled(Marker marker) {
    return logger.isTraceEnabled(combine(marker));
  }

  public boolean isWarnEnabled() {
    return logger.isWarnEnabled(combine());
  }

  public boolean isWarnEnabled(Marker marker) {
    return logger.isWarnEnabled(combine(marker));
  }

  public void trace(Marker marker, String msg) {
    logger.trace(combine(marker), msg);
  }

  public void trace(Marker marker, String format, Object arg) {
    logger.trace(combine(marker), format, arg);
  }

  public void trace(Marker marker, String format, Object... argArray) {
    logger.trace(combine(marker), format, argArray);
  }

  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    logger.trace(combine(marker), format, arg1, arg2);
  }

  public void trace(Marker marker, String msg, Throwable t) {
    logger.trace(combine(marker), msg, t);
  }

  public void trace(String msg) {
    logger.trace(combine(), msg);
  }

  public void trace(String format, Object arg) {
    logger.trace(combine(), format, arg);
  }

  public void trace(String format, Object... arguments) {
    logger.trace(combine(), format, arguments);
  }

  public void trace(String format, Object arg1, Object arg2) {
    logger.trace(combine(), format, arg1, arg2);
  }

  public void trace(String msg, Throwable t) {
    logger.trace(combine(), msg, t);
  }

  public void warn(Marker marker, String msg) {
    logger.warn(combine(marker), msg);
  }

  public void warn(Marker marker, String format, Object arg) {
    logger.warn(combine(marker), format, arg);
  }

  public void warn(Marker marker, String format, Object... arguments) {
    logger.warn(combine(marker), format, arguments);
  }

  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    logger.warn(combine(marker), format, arg1, arg2);
  }

  public void warn(Marker marker, String msg, Throwable t) {
    logger.warn(combine(marker), msg, t);
  }

  public void warn(String msg) {
    logger.warn(combine(), msg);
  }

  public void warn(String format, Object arg) {
    logger.warn(combine(), format, arg);
  }

  public void warn(String format, Object... arguments) {
    logger.warn(combine(), format, arguments);
  }

  public void warn(String format, Object arg1, Object arg2) {
    logger.warn(combine(), format, arg1, arg2);
  }

  public void warn(String msg, Throwable t) {
    logger.warn(combine(), msg, t);
  }
}
