/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Facilitates soft asserts for Hamcrest. Adds assertion step to Allure Report.
 *
 * @since 5.0.0
 */
@Aspect
public class AssertAspect {

  private static final Logger LOG = LoggerFactory.getLogger(AssertAspect.class);

  /**
   * Ignores exceptions, when {@link io.wcm.qa.glnm.configuration.GaleniumConfiguration#isSamplingVerificationIgnore()} is
   * true.
   *
   * @param jp join point to be handled
   * @return null or proceed value (advised method returns void)
   */
  @Around(value = "hamcrestMatcherAssert()")
  public Object aroundAssert(final ProceedingJoinPoint jp) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("assert aspect <" + jp + ">");
    }
    String stepUuid = beginAssert(jp);
    try {
      Object proceed = jp.proceed();
      doneAsserting(stepUuid, jp);
      return proceed;
    }
    catch (Throwable ex) {
      failedAssert(stepUuid, jp, ex);
      if (LOG.isDebugEnabled()) {
        LOG.debug("when asserting.", ex);
      }
      if (GaleniumConfiguration.isSamplingVerificationIgnore()) {
        return null;
      }
      throw new GaleniumException("assert: " + ex.getMessage(), ex);
    }
  }

  /**
   * Pointcut definition. No functionality.
   */
  @Pointcut("call(public static void org.hamcrest.MatcherAssert.assertThat(String, Object, org.hamcrest.Matcher))")
  public void hamcrestMatcherAssert() {
    // pointcut body, should be empty
  }

  /**
   * Adds step to report.
   * @param joinPoint only used for logging
   * @return UUID for new report step
   */
  private static String beginAssert(final JoinPoint joinPoint) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("asserting: " + joinPoint.getSignature().toLongString());
    }
    return GaleniumReportUtil.startStep("assert");
  }

  /**
   * Passes step and updates description.
   * @param stepUuid to pass and stop
   * @param joinPoint to supply description
   */
  private static void doneAsserting(String stepUuid, final JoinPoint joinPoint) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("asserted: " + joinPoint.getSignature().toLongString());
    }
    String description = generateAssertDescription(joinPoint);
    GaleniumReportUtil.updateStepName(stepUuid, description);
    GaleniumReportUtil.passStep(stepUuid);
    GaleniumReportUtil.stopStep();
  }

  /**
   * Fails step and updates description.
   * @param stepUuid to pass and stop
   * @param joinPoint to supply description
   * @param e exception caught from MatcherAssert
   */
  private static void failedAssert(String stepUuid, final JoinPoint joinPoint, final Throwable e) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("failed asserting: " + joinPoint.getSignature().toLongString(), e);
    }
    String assertDescription = generateAssertDescription(joinPoint);
    GaleniumReportUtil.updateStepName(stepUuid, assertDescription);
    GaleniumReportUtil.failStep(stepUuid);
    GaleniumReportUtil.stopStep();
  }

  /**
   * Generates description from assert method call in join point.
   * @param joinPoint to extract matcher from
   * @return description from matcher and assert
   */
  private static String generateAssertDescription(final JoinPoint joinPoint) {
    Object reason = joinPoint.getArgs()[0];
    Object actual = joinPoint.getArgs()[1];
    String matcherDescription = generateMatcherDescription(joinPoint);
    StringBuilder assertDescription = new StringBuilder()
        .append("assert: ")
        .append(reason)
        .append(" ")
        .append(actual)
        .append(" ")
        .append(matcherDescription);
    return assertDescription.toString();
  }

  /**
   * Generates description from matcher in join point.
   * @param joinPoint to extract matcher from
   * @return description from matcher
   */
  private static String generateMatcherDescription(final JoinPoint joinPoint) {
    Matcher matcher = (Matcher)joinPoint.getArgs()[2];
    Description matcherDescription = new StringDescription();
    matcher.describeTo(matcherDescription);
    return matcherDescription.toString();
  }

}
