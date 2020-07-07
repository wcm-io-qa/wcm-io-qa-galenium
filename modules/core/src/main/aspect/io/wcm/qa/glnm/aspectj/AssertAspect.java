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
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Facilitates soft asserts for Hamcrest. Adds assertion step to Allure Report.
 */
@Aspect("percflow(call(* *..*.assertThat(..)))")
public class AssertAspect {

  private static final Logger LOG = LoggerFactory.getLogger(AssertAspect.class);
  private String startStepUuid;

  @AfterReturning(pointcut = "call(public static void org.hamcrest.MatcherAssert.assertThat(String, Object, org.hamcrest.Matcher))")
  public void doneAsserting(final JoinPoint joinPoint) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("asserted: " + joinPoint.getSignature().toLongString());
    }
    String assertDescription = assertDescriptionFromJoinPoint(joinPoint);
    GaleniumReportUtil.updateStepName(startStepUuid, assertDescription);
    GaleniumReportUtil.passStep(startStepUuid);
    GaleniumReportUtil.stopStep();
  }

  @AfterThrowing(
      pointcut = "call(public static void org.hamcrest.MatcherAssert.assertThat(String, Object, org.hamcrest.Matcher))",
      throwing = "e")
  public void failedAssert(final JoinPoint joinPoint, final Throwable e) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("failed asserting: " + joinPoint.getSignature().toLongString(), e);
    }
    String assertDescription = assertDescriptionFromJoinPoint(joinPoint);
    GaleniumReportUtil.updateStepName(startStepUuid, assertDescription);
    GaleniumReportUtil.failStep(startStepUuid);
    GaleniumReportUtil.stopStep();
  }

  @Pointcut("call(public static void org.hamcrest.MatcherAssert.assertThat(String, Object, org.hamcrest.Matcher))")
  public void hamcrestMatcherAssert() {
    // pointcut body, should be empty
  }

  @Before(value = "call(public static void org.hamcrest.MatcherAssert.assertThat(String, *, org.hamcrest.Matcher))")
  public void beginAssert(final JoinPoint joinPoint) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("asserting: " + joinPoint.getSignature().toLongString());
    }
    startStepUuid = GaleniumReportUtil.startStep("assert");
  }

  private String assertDescriptionFromJoinPoint(final JoinPoint joinPoint) {
    Object reason = joinPoint.getArgs()[0];
    Object actual = joinPoint.getArgs()[1];
    Description matcherDescription = new StringDescription();
    Matcher matcher = (Matcher)joinPoint.getArgs()[2];
    matcher.describeTo(matcherDescription);
    StringBuilder stringBuilder = new StringBuilder()
        .append("assert: ")
        .append(reason)
        .append(" ")
        .append(actual)
        .append(" ")
        .append(matcherDescription.toString());
    return stringBuilder.toString();
  }

}
