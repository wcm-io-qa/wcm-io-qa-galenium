package io.wcm.qa.glnm.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
/**
 * <p>
 * MatcherAspect class.
 * </p>
 *
 * @since 5.0.0
 */
@Aspect("perthis(execution(* *..*.matches(..)))")
public class MatcherAspect {

  private static final Logger LOG = LoggerFactory.getLogger(MatcherAspect.class);
  private String startStepUuid;

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

  private void failStep(final JoinPoint joinPoint, Matcher matcher) {
    StringDescription description = descriptionFor(matcher);
    description
        .appendText(System.lineSeparator())
        .appendText("     but: ");
    Object matchedItem = joinPoint.getArgs()[0];
    matcher.describeMismatch(matchedItem, description);
    GaleniumReportUtil.updateStepName(startStepUuid, description.toString());
    GaleniumReportUtil.failStep(startStepUuid);
    GaleniumReportUtil.stopStep();
  }

  private void passStep(Matcher matcher) {
    GaleniumReportUtil.updateStepName(startStepUuid, descriptionFor(matcher).toString());
    GaleniumReportUtil.passStep(startStepUuid);
    GaleniumReportUtil.stopStep();
  }

  private StringDescription descriptionFor(Matcher matcher) {
    StringDescription description = new StringDescription();
    description
        .appendText("Expected: ")
        .appendDescriptionOf(matcher);
    return description;
  }


}
