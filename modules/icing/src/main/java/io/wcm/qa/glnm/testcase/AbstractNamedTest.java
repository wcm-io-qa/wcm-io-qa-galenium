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
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.SkipException;

/**
 * Base class using
 * {@link io.wcm.qa.glnm.differences.specialized.TestNameDifferences} to
 * generate test name for TestNG's {@link org.testng.ITest} interface.
 *
 * @since 3.0.0
 */
public class AbstractNamedTest implements ITest {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractNamedTest.class);

  protected AbstractNamedTest() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public String getTestName() {
    return getClass().getName();
  }

  /**
   * @param skipMessage will be logged and added to {@link SkipException}
   */
  protected void skipTest(String skipMessage) {
    LOG.info("Skipping: " + skipMessage);
    throw new SkipException(skipMessage);
  }

  /**
<<<<<<< HEAD
   * Convenience method delegating to {@link io.wcm.qa.glnm.reporting.GaleniumReportUtil#getLogger()}.
   *
   * @return current logger
=======
   * @param skipMessage will be logged and added to {@link SkipException} as message
   * @param ex will be logged and added to {@link SkipException} as cause
>>>>>>> develop
   */
  protected void skipTest(String skipMessage, Throwable ex) {
    LOG.info("Skipping: " + skipMessage, ex);
    throw new SkipException(skipMessage, ex);
  }

}
