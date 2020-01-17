/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.interaction;

import static io.wcm.qa.glnm.context.GaleniumContext.getDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

/**
 * Represents browser log. This is where JavaScript output and errors will appear.
 *
 * @since 4.0.0
 */
public class BrowserLog {

  private LogEntries entries;

  /**
   * <p>Constructor for BrowserLog.</p>
   */
  public BrowserLog() {
    setEntries(getDriver().manage().logs().get(LogType.BROWSER));
  }

  /**
   * Whether log contains errors.
   *
   * @return whether at least one log entry has level SEVERE
   */
  public boolean containsErrors() {
    for (LogEntry logEntry : getEntries()) {
      if (logEntry.getLevel() == Level.SEVERE) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>
   * Get error messages.
   * </p>
   *
   * @return all messages with SEVERE level
   */
  public Collection<String> getErrors() {
    return getMessages(Level.SEVERE);
  }

  /**
   * <p>
   * Get messages with a certain log level. Only messages with the same or higher
   * log level are returned.
   * </p>
   *
   * @param minLevel to filter messages
   * @return all messages with high enough log level
   */
  public Collection<String> getMessages(Level minLevel) {
    Collection<String> messages = new ArrayList<String>();
    for (LogEntry logEntry : getEntries()) {
      if (logEntry.getLevel().intValue() >= minLevel.intValue()) {
        messages.add(logEntry.getMessage());
      }
    }
    return messages;
  }

  protected LogEntries getEntries() {
    return entries;
  }

  protected void setEntries(LogEntries entries) {
    this.entries = entries;
  }
}
