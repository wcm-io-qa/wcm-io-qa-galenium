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
package io.wcm.qa.glnm.testcase;


import org.slf4j.Logger;
import org.testng.ITest;
import org.testng.SkipException;

import io.wcm.qa.glnm.differences.specialized.TestNameDifferences;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Base class using {@link TestNameDifferences} to generate test name for TestNG's {@link ITest} interface.
 */
public class AbstractNamedTest implements ITest {

  private TestNameDifferences nameDifferences = new TestNameDifferences();

  protected AbstractNamedTest() {
    super();
    getNameDifferences().setClass(getClass());
    getNameDifferences().setClassNameMaxLength(30);
  }

  @Override
  public String getTestName() {
    return getNameDifferences().asPropertyKey();
  }

  protected TestNameDifferences getNameDifferences() {
    return nameDifferences;
  }

  protected void setNameDifferences(TestNameDifferences nameDifferences) {
    this.nameDifferences = nameDifferences;
  }

  protected void skipTest(String skipMessage) {
    getLogger().info(GaleniumReportUtil.MARKER_SKIP, "Skipping: " + skipMessage);
    throw new SkipException(skipMessage);
  }

  protected void skipTest(String skipMessage, Throwable ex) {
    getLogger().info(GaleniumReportUtil.MARKER_SKIP, "Skipping: " + getTestName(), ex);
    throw new SkipException(skipMessage, ex);
  }

  /**
   * Convenience method delegating to {@link GaleniumReportUtil#getLogger()}.
   * @return current logger
   */
  public Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

}
