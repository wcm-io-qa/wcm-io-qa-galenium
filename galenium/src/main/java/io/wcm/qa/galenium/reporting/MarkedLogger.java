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

  private Logger delegate;
  private Marker markerToAdd;

  public MarkedLogger(Logger delegateLogger, Marker additionalMarker) {
    delegate = delegateLogger;
    markerToAdd = additionalMarker;
  }

  private Marker mark(Marker marker) {
    Marker combined = mark();
    combined.add(marker);
    return combined;
  }

  private Marker mark() {
    Marker combiningMarker = MarkerFactory.getDetachedMarker("marked_logger");
    combiningMarker.add(markerToAdd);
    return combiningMarker;
  }

  public void debug(Marker marker, String msg) {
    delegate.debug(mark(marker), msg);
  }

  public void debug(Marker marker, String format, Object arg) {
    delegate.debug(mark(marker), format, arg);
  }

  public void debug(Marker marker, String format, Object... arguments) {
    delegate.debug(mark(marker), format, arguments);
  }

  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    delegate.debug(mark(marker), format, arg1, arg2);
  }

  public void debug(Marker marker, String msg, Throwable t) {
    delegate.debug(mark(marker), msg, t);
  }

  public void debug(String msg) {
    delegate.debug(mark(), msg);
  }

  public void debug(String format, Object arg) {
    delegate.debug(mark(), format, arg);
  }

  public void debug(String format, Object... arguments) {
    delegate.debug(mark(), format, arguments);
  }

  public void debug(String format, Object arg1, Object arg2) {
    delegate.debug(mark(), format, arg1, arg2);
  }

  public void debug(String msg, Throwable t) {
    delegate.debug(mark(), msg, t);
  }

  public void error(Marker marker, String msg) {
    delegate.error(mark(marker), msg);
  }

  public void error(Marker marker, String format, Object arg) {
    delegate.error(mark(marker), format, arg);
  }

  public void error(Marker marker, String format, Object... arguments) {
    delegate.error(mark(marker), format, arguments);
  }

  public void error(Marker marker, String format, Object arg1, Object arg2) {
    delegate.error(mark(marker), format, arg1, arg2);
  }

  public void error(Marker marker, String msg, Throwable t) {
    delegate.error(mark(marker), msg, t);
  }

  public void error(String msg) {
    delegate.error(mark(), msg);
  }

  public void error(String format, Object arg) {
    delegate.error(mark(), format, arg);
  }

  public void error(String format, Object... arguments) {
    delegate.error(mark(), format, arguments);
  }

  public void error(String format, Object arg1, Object arg2) {
    delegate.error(mark(), format, arg1, arg2);
  }

  public void error(String msg, Throwable t) {
    delegate.error(mark(), msg, t);
  }

  public String getName() {
    return delegate.getName();
  }

  public void info(Marker marker, String msg) {
    delegate.info(mark(marker), msg);
  }

  public void info(Marker marker, String format, Object arg) {
    delegate.info(mark(marker), format, arg);
  }

  public void info(Marker marker, String format, Object... arguments) {
    delegate.info(mark(marker), format, arguments);
  }

  public void info(Marker marker, String format, Object arg1, Object arg2) {
    delegate.info(mark(marker), format, arg1, arg2);
  }

  public void info(Marker marker, String msg, Throwable t) {
    delegate.info(mark(marker), msg, t);
  }

  public void info(String msg) {
    delegate.info(mark(), msg);
  }

  public void info(String format, Object arg) {
    delegate.info(mark(), format, arg);
  }

  public void info(String format, Object... arguments) {
    delegate.info(mark(), format, arguments);
  }

  public void info(String format, Object arg1, Object arg2) {
    delegate.info(mark(), format, arg1, arg2);
  }

  public void info(String msg, Throwable t) {
    delegate.info(mark(), msg, t);
  }

  public boolean isDebugEnabled() {
    return delegate.isDebugEnabled(mark());
  }

  public boolean isDebugEnabled(Marker marker) {
    return delegate.isDebugEnabled(mark(marker));
  }

  public boolean isErrorEnabled() {
    return delegate.isErrorEnabled(mark());
  }

  public boolean isErrorEnabled(Marker marker) {
    return delegate.isErrorEnabled(mark(marker));
  }

  public boolean isInfoEnabled() {
    return delegate.isInfoEnabled(mark());
  }

  public boolean isInfoEnabled(Marker marker) {
    return delegate.isInfoEnabled(mark(marker));
  }

  public boolean isTraceEnabled() {
    return delegate.isTraceEnabled(mark());
  }

  public boolean isTraceEnabled(Marker marker) {
    return delegate.isTraceEnabled(mark(marker));
  }

  public boolean isWarnEnabled() {
    return delegate.isWarnEnabled(mark());
  }

  public boolean isWarnEnabled(Marker marker) {
    return delegate.isWarnEnabled(mark(marker));
  }

  public void trace(Marker marker, String msg) {
    delegate.trace(mark(marker), msg);
  }

  public void trace(Marker marker, String format, Object arg) {
    delegate.trace(mark(marker), format, arg);
  }

  public void trace(Marker marker, String format, Object... argArray) {
    delegate.trace(mark(marker), format, argArray);
  }

  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    delegate.trace(mark(marker), format, arg1, arg2);
  }

  public void trace(Marker marker, String msg, Throwable t) {
    delegate.trace(mark(marker), msg, t);
  }

  public void trace(String msg) {
    delegate.trace(mark(), msg);
  }

  public void trace(String format, Object arg) {
    delegate.trace(mark(), format, arg);
  }

  public void trace(String format, Object... arguments) {
    delegate.trace(mark(), format, arguments);
  }

  public void trace(String format, Object arg1, Object arg2) {
    delegate.trace(mark(), format, arg1, arg2);
  }

  public void trace(String msg, Throwable t) {
    delegate.trace(mark(), msg, t);
  }

  public void warn(Marker marker, String msg) {
    delegate.warn(mark(marker), msg);
  }

  public void warn(Marker marker, String format, Object arg) {
    delegate.warn(mark(marker), format, arg);
  }

  public void warn(Marker marker, String format, Object... arguments) {
    delegate.warn(mark(marker), format, arguments);
  }

  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    delegate.warn(mark(marker), format, arg1, arg2);
  }

  public void warn(Marker marker, String msg, Throwable t) {
    delegate.warn(mark(marker), msg, t);
  }

  public void warn(String msg) {
    delegate.warn(mark(), msg);
  }

  public void warn(String format, Object arg) {
    delegate.warn(mark(), format, arg);
  }

  public void warn(String format, Object... arguments) {
    delegate.warn(mark(), format, arguments);
  }

  public void warn(String format, Object arg1, Object arg2) {
    delegate.warn(mark(), format, arg1, arg2);
  }

  public void warn(String msg, Throwable t) {
    delegate.warn(mark(), msg, t);
  }
}
