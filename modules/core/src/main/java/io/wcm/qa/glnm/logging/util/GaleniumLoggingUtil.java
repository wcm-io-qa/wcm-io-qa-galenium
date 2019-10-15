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
package io.wcm.qa.glnm.logging.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.logging.logback.MarkedLogger;

/**
 * Utility methods and constants around logging.
 */
public final class GaleniumLoggingUtil {

  /**
   * For special ERROR log status.
   * @deprecated Use Allure compatible markers or use SLF4J log level {@link ch.qos.logback.core.status.Status#ERROR}.
   */
  @Deprecated
  public static final Marker MARKER_ERROR = getMarker("ERROR");

  /**
   * For all special ExtentReports events.
   * @deprecated Use Allure compatible markers.
   */
  @Deprecated
  public static final Marker MARKER_EXTENT_REPORT = MarkerFactory.getMarker("EXTENT_REPORT");

  /** For special FAIL log status. */
  public static final Marker MARKER_FAIL = getMarker(Status.FAILED);

  /**
   * For special FATAL log status.
   * @deprecated Use Allure compatible markers or use SLF4J log levels.
   */
  @Deprecated
  public static final Marker MARKER_FATAL = getMarker("FATAL");

  /**
   * For special INFO log status.
   * @deprecated Use Allure compatible markers or use SLF4J log level {@link ch.qos.logback.core.status.Status#INFO}.
   */
  @Deprecated
  public static final Marker MARKER_INFO = getMarker("INFO");

  /** For special PASS log status. */
  public static final Marker MARKER_PASS = getMarker(Status.PASSED);

  /** For special SKIP log status. */
  public static final Marker MARKER_SKIP = getMarker(Status.SKIPPED);

  /**
   * For special WARN log status.
   * @deprecated Use Allure compatible markers or use SLF4J log level {@link ch.qos.logback.core.status.Status#WARN}.
   */
  @Deprecated
  public static final Marker MARKER_WARN = getMarker("WARN");

  private static final String MDC_PARAM_GLNM_TESTNAME = "glnm.testname";

  private static final File TEST_LOG_ROOT = new File(GaleniumConfiguration.getTestLogDirectory());

  private static final IOFileFilter TRUE_FILE_FILTER = FileFilterUtils.trueFileFilter();

  private static final Logger LOG = LoggerFactory.getLogger(GaleniumLoggingUtil.class);

  private GaleniumLoggingUtil() {
    // do not instantiate
  }

  /**
   * Gets a logger which marks every entry with the passed {@link Marker}.
   * @param marker to use with this logger
   * @param logger to wrap in marked logger
   * @return a {@link MarkedLogger} using the marker
   */
  public static Logger getMarkedLogger(Marker marker, Logger logger) {
    return new MarkedLogger(logger, marker);
  }

  /**
   * Gets a logger which marks every entry with a {@link Marker} using the passed string.
   * @param marker to use with this logger
   * @param logger to wrap in marked logger
   * @return a {@link MarkedLogger} using the marker
   */
  public static Logger getMarkedLogger(String marker, Logger logger) {
    return getMarkedLogger(getMarker(marker), logger);
  }

  /**
   * @param name marker name
   * @return marker for use with marked logger
   */
  public static Marker getMarker(String name) {
    Marker marker = MarkerFactory.getMarker(name);
    return marker;
  }

  /**
   * Initializes logging per test.
   */
  public static void startTestLogging() {
    String currentTestCaseId = Allure.getLifecycle().getCurrentTestCase().orElse("NO_TEST_ID");
    LOG.info("Stasrting test specific logging for: " + currentTestCaseId);
    MDC.put(MDC_PARAM_GLNM_TESTNAME, currentTestCaseId);
  }

  /**
   * Stops test specific logging.
   */
  public static void stopTestLogging() {
    String testIdOfFinishedTest = MDC.get(MDC_PARAM_GLNM_TESTNAME);
    LOG.info("Stopping test specific logging for: " + testIdOfFinishedTest);
    addLogsToAllure(testIdOfFinishedTest);
    MDC.remove(MDC_PARAM_GLNM_TESTNAME);
  }

  private static void addLogsToAllure(String testIdOfFinishedTest) {
    if (StringUtils.isNotBlank(testIdOfFinishedTest)) {
      Collection<File> logFiles = getLogFiles(testIdOfFinishedTest);
      for (File logFile : logFiles) {
        LOG.trace("Attaching " + logFile + " to Allure report.");
        Allure.addAttachment(logFile.getName(), asString(logFile));
      }
    }
  }

  private static String asString(File logFile) {
    String logFileContentAsString;
    try {
      logFileContentAsString = FileUtils.readFileToString(logFile, StandardCharsets.UTF_8);
    }
    catch (IOException ex) {
      LOG.info("could not read log file.", ex);
      logFileContentAsString = "Could not read log file: '" + logFile.getPath() + "' (" + ex.getMessage() + ")";
    }
    return logFileContentAsString;
  }

  private static Collection<File> getLogFiles(String testIdOfFinishedTest) {

    // starting with test ID and ending in 'log'
    IOFileFilter logFileFilter = FileFilterUtils.and(
        FileFilterUtils.prefixFileFilter(testIdOfFinishedTest),
        FileFilterUtils.suffixFileFilter("log"));

    Collection<File> logFiles = FileUtils.listFiles(TEST_LOG_ROOT, logFileFilter, TRUE_FILE_FILTER);
    LOG.debug("Found " + logFiles.size() + " log files for test id: '" + testIdOfFinishedTest + "'");
    return logFiles;
  }

  private static Marker getMarker(Status logStatus) {
    return getMarker(logStatus.name());
  }

}
