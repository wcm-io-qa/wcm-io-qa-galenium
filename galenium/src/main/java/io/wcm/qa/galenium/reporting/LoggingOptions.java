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

import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.logging.Logs;

final class LoggingOptions extends AbstractLoggingBase<Options> implements Options {

  LoggingOptions(Options manage, GaleniumLogging logging) {
    super(manage, logging);
  }

  @Override
  public void addCookie(Cookie arg0) {
    debugStart("addCookie", arg0);
    getDelegate().addCookie(arg0);
    debugStop();
  }

  @Override
  public void deleteAllCookies() {
    debugStart("deleteAllCookies");
    getDelegate().deleteAllCookies();
    debugStop();
  }

  @Override
  public void deleteCookie(Cookie arg0) {
    debugStart("deleteCookie", arg0);
    getDelegate().deleteCookie(arg0);
    debugStop();
  }

  @Override
  public void deleteCookieNamed(String arg0) {
    debugStart("deleteCookieNamed", arg0);
    getDelegate().deleteCookieNamed(arg0);
    debugStop();
  }

  @Override
  public Cookie getCookieNamed(String arg0) {
    debugStart("getCookieNamed", arg0);
    Cookie cookieNamed = getDelegate().getCookieNamed(arg0);
    debugStop(cookieNamed);
    return cookieNamed;
  }

  @Override
  public Set<Cookie> getCookies() {
    debugStart("getCookies");
    Set<Cookie> cookies = getDelegate().getCookies();
    debugStop(cookies);
    return cookies;
  }

  @Override
  public ImeHandler ime() {
    debugStart("ime");
    ImeHandler ime = getDelegate().ime();
    debugStop(ime);
    return ime;
  }

  @Override
  public Logs logs() {
    debugStart("logs");
    Logs logs = getDelegate().logs();
    debugStop(logs);
    return logs;
  }

  @Override
  public Timeouts timeouts() {
    debugStart("timeouts");
    Timeouts timeouts = getDelegate().timeouts();
    debugStop(timeouts);
    return timeouts;
  }

  @Override
  public Window window() {
    debugStart("window");
    Window window = getDelegate().window();
    debugStop(window);
    return window;
  }

}
