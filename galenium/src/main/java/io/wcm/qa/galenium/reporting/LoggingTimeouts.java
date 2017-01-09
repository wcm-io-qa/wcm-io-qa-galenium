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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver.Timeouts;


class LoggingTimeouts extends AbstractLoggingBase<Timeouts> {

  LoggingTimeouts(Timeouts delegate, GaleniumLogging logging) {
    super(delegate, logging);
  }

  public Timeouts implicitlyWait(long arg0, TimeUnit arg1) {
    debugStart("implicitlyWait", arg0, arg1);
    Timeouts timeouts = getDelegate().implicitlyWait(arg0, arg1);
    debugStop(timeouts);
    return timeouts;
  }

  public Timeouts pageLoadTimeout(long arg0, TimeUnit arg1) {
    debugStart("pageLoadTimeout", arg0, arg1);
    Timeouts timeouts = getDelegate().pageLoadTimeout(arg0, arg1);
    debugStop(timeouts);
    return timeouts;
  }

  public Timeouts setScriptTimeout(long arg0, TimeUnit arg1) {
    debugStart("setScriptTimeout", arg0, arg1);
    Timeouts timeouts = getDelegate().setScriptTimeout(arg0, arg1);
    debugStop(timeouts);
    return timeouts;
  }

}
