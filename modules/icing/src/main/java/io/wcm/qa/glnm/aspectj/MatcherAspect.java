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
@Aspect("perthis(execution(boolean org.hamcrest.Matcher.matches(Object)))")
public class MatcherAspect {

  private static final String HAMCREST_MATCHER_MATCH = "hamcrestMatcherMatch()";
  private static final Logger LOG = LoggerFactory.getLogger(MatcherAspect.class);
  private String startStepUuid;

  /**
   * <p>
   * hamcrestMatcherMatch.
   * </p>
   */
  @Pointcut("execution(boolean org.hamcrest.Matcher.matches(Object))")
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
  @Before("execution(boolean org.hamcrest.Matcher.matches(Object))")
  public void startMatching(final JoinPoint joinPoint) {
    LOG.info(joinPoint.getSignature().toLongString());
    startStepUuid = GaleniumReportUtil.startStep("matching");
  }

  /**
   * <p>failedMatching.</p>
   *
   * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object.
   * @param e a {@link java.lang.Throwable} object.
   */
  @AfterThrowing(value = "execution(boolean org.hamcrest.Matcher.matches(Object))", throwing = "e")
  public void failedMatching(final JoinPoint joinPoint, final Throwable e) {
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
  @AfterReturning(pointcut = "execution(boolean org.hamcrest.Matcher.matches(Object))", returning = "result")
  public void doneMatching(final JoinPoint joinPoint, boolean result) {
    Matcher target = (Matcher)joinPoint.getTarget();
    StringDescription stringDescription = new StringDescription();
    target.describeTo(stringDescription);
    if (result) {
      GaleniumReportUtil.updateStepName(startStepUuid, stringDescription.toString());
      GaleniumReportUtil.passStep(startStepUuid);
      GaleniumReportUtil.stopStep();
    }
    else {
      target.describeMismatch(joinPoint.getArgs()[0], stringDescription);
      GaleniumReportUtil.updateStepName(startStepUuid, stringDescription.toString());
      GaleniumReportUtil.failStep(startStepUuid);
      GaleniumReportUtil.stopStep();
    }
  }

}
