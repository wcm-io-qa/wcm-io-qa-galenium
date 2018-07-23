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
package io.wcm.qa.galenium.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;


class FirefoxOptionsProvider extends OptionsProvider<FirefoxOptions> {

  private static final Level LOG_LEVEL_BROWSER = GaleniumConfiguration.getBrowserLogLevel();
  private static final Level LOG_LEVEL_SEVERE = Level.SEVERE;
  private static final String SYSTEM_PROPERTY_NAME_WEBDRIVER_FIREFOX_BIN = "webdriver.firefox.bin";

  private void addLoggingPreferences(FirefoxOptions options) {
    options.setLogLevel(FirefoxDriverLogLevel.WARN);

    // Request browser logging capabilities for capturing console.log output
    LoggingPreferences loggingPrefs = new LoggingPreferences();
    loggingPrefs.enable(LogType.BROWSER, LOG_LEVEL_BROWSER);
    loggingPrefs.enable(LogType.CLIENT, LOG_LEVEL_SEVERE);
    loggingPrefs.enable(LogType.DRIVER, LOG_LEVEL_SEVERE);
    loggingPrefs.enable(LogType.PERFORMANCE, LOG_LEVEL_SEVERE);
    loggingPrefs.enable(LogType.PROFILER, LOG_LEVEL_SEVERE);
    loggingPrefs.enable(LogType.SERVER, LOG_LEVEL_SEVERE);
    options.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);
  }

  private void addProfile(FirefoxOptions options) {
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    firefoxProfile.setAcceptUntrustedCertificates(true);
    firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
    options.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
  }

  private void logBinary(FirefoxOptions options) {
    FirefoxBinary binary = options.getBinary();
    getLogger().debug("Firefox binary: " + binary);
  }

  private void logLoggingPrefs(FirefoxOptions options) {
    if (getLogger().isTraceEnabled()) {
      Object capability = options.getCapability(CapabilityType.LOGGING_PREFS);
      if (capability instanceof LoggingPreferences) {
        LoggingPreferences loggingPrefs = (LoggingPreferences)capability;
        Set<String> enabledLogTypes = loggingPrefs.getEnabledLogTypes();
        for (String logType : enabledLogTypes) {
          Level level = loggingPrefs.getLevel(logType);
          getLogger().trace("Firefox logging enabled: " + logType + " -> " + level);
        }
      }
    }
  }

  private void logProfile(FirefoxOptions options) {
    FirefoxProfile profile = options.getProfile();
    if (profile != null) {
      try {
        getLogger().trace("Firefox profile: " + profile.toJson());
      }
      catch (IOException ex) {
        getLogger().debug("Exception when dumping Firefox profile to JSON.", ex);
      }
    }
  }

  private void setBinary(FirefoxOptions options) {
    String firefoxBin = System.getProperty(SYSTEM_PROPERTY_NAME_WEBDRIVER_FIREFOX_BIN);
    if (StringUtils.isNotBlank(firefoxBin)) {
      getLogger().debug("webdriver.firefox.bin: '" + firefoxBin + "'");
      File binaryFile = new File(firefoxBin);
      if (binaryFile.isFile()) {
        getLogger().trace("Setting explicit binary for Firefox: '" + binaryFile.getPath() + "'");
        FirefoxBinary binary = new FirefoxBinary(binaryFile);
        binary.addCommandLineOptions("-log", "fatal");
        options.setBinary(binary);
      }
      else {
        getLogger().warn("Skipping explicit binary for Firefox because it is not a file: '" + binaryFile.getPath() + "'");
      }
    }
  }

  private void setHeadless(FirefoxOptions options) {
    options.setHeadless(GaleniumConfiguration.isHeadless());
  }

  @Override
  protected FirefoxOptions getBrowserSpecificOptions() {
    getLogger().debug("creating capabilities for Firefox");
    FirefoxOptions options = newOptions();
    addProfile(options);
    addLoggingPreferences(options);
    setHeadless(options);
    setBinary(options);
    return options;
  }

  @Override
  protected void log(FirefoxOptions options) {
    getLogger().debug("Firefox options: " + options);
    logProfile(options);
    logBinary(options);
    logLoggingPrefs(options);
  }

  @Override
  protected FirefoxOptions newOptions() {
    return new FirefoxOptions();
  }

}
