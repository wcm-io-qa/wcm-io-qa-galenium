package io.wcm.qa.glnm.aspectj;

import static org.apache.commons.lang3.StringUtils.abbreviateMiddle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * <p>
 * Adds matches to Allure Report with correct pass/fail status.
 * </p>
 * @since 5.0.0
 */
@Aspect("perthis(execution(* *..*.matches(..)))")
public class MatcherAspect {

  private static final Logger LOG = LoggerFactory.getLogger(MatcherAspect.class);
  private String startStepUuid;

  /**
   * <p>doneMatching.</p>
   *
   * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object.
   * @param result a boolean.
   */
  @AfterReturning(pointcut = "execution(boolean org.hamcrest.Matcher.matches(..))", returning = "result")
  public void doneMatching(final JoinPoint joinPoint, boolean result) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("done: " + joinPoint.getSignature().toLongString());
    }
    Matcher matcher = (Matcher)joinPoint.getTarget();
    if (result) {
      passStep(matcher);
    }
    else {
      failStep(joinPoint, matcher);
    }
  }

  /**
   * <p>failedMatching.</p>
   *
   * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object.
   * @param e a {@link java.lang.Throwable} object.
   */
  @AfterThrowing(pointcut = "execution(boolean org.hamcrest.Matcher.matches(..))", throwing = "e")
  public void failedMatching(final JoinPoint joinPoint, final Throwable e) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("failed: " + joinPoint.getSignature().toLongString());
    }
    StringDescription stringDescription = new StringDescription();
    Matcher target = (Matcher)joinPoint.getTarget();
    target.describeTo(stringDescription);
    stringDescription.appendText(e.getMessage());

  }

  /**
   * <p>
   * hamcrestMatcherMatch.
   * </p>
   */
  @Pointcut("execution(boolean org.hamcrest.Matcher.matches(..))")
  public void hamcrestMatcherMatch() {
    // pointcut body, should be empty
  }

  /**
   * <p>
   * startMatch.
   * </p>
   *
   * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object.
   */
  @Before("execution(boolean org.hamcrest.Matcher.matches(..))")
  public void startMatching(final JoinPoint joinPoint) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("before: " + joinPoint.getSignature().toLongString());
    }
    startStepUuid = GaleniumReportUtil.startStep("matching");
  }

  private StringDescription descriptionFor(Matcher matcher) {
    StringDescription description = new StringDescription();
    description
        .appendText("Expected: ")
        .appendDescriptionOf(matcher);
    return description;
  }

  private void failStep(final JoinPoint joinPoint, Matcher matcher) {
    StringDescription description = descriptionFor(matcher);
    description
        .appendText(System.lineSeparator())
        .appendText("     but: ");
    Object matchedItem = joinPoint.getArgs()[0];
    describeMismatch(matcher, description, matchedItem);
    GaleniumReportUtil.updateStepName(startStepUuid, description.toString());
    GaleniumReportUtil.failStep(startStepUuid);
    screenshot(matchedItem);
    GaleniumReportUtil.stopStep();
  }

  private void passStep(Matcher matcher) {
    GaleniumReportUtil.updateStepName(startStepUuid, descriptionFor(matcher).toString());
    GaleniumReportUtil.passStep(startStepUuid);
    GaleniumReportUtil.stopStep();
  }

  private static void describeMismatch(Matcher matcher, StringDescription description, Object matchedItem) {
    if (matchedItem instanceof String) {
      matcher.describeMismatch(abbreviateMiddle((String)matchedItem, "[...]", 200), description);
    }
    else {
      matcher.describeMismatch(matchedItem, description);
    }
  }

  private static void screenshot(Object matchedItem) {
    if (matchedItem == null) {
      return;
    }
    if (matchedItem instanceof TakesScreenshot) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("taking screenshot of " + matchedItem);
      }
      GaleniumReportUtil.takeScreenshot("matcher-screenshot", (TakesScreenshot)matchedItem, false);
      return;
    }
  }

}
