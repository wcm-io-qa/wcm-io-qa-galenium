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
package io.wcm.qa.galenium.logging.logback;

import java.util.regex.Pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ExtentFilter extends Filter<ILoggingEvent> {

  private static final Pattern PATTERN_TESTNAME = Pattern.compile("^[a-zA-Z0-9]+IT/.*");

  @Override
  public FilterReply decide(ILoggingEvent event) {
    String loggerName = event.getLoggerName();
    if (PATTERN_TESTNAME.matcher(loggerName).matches()) {
      return FilterReply.ACCEPT;
    }
    return FilterReply.DENY;
  }

}
