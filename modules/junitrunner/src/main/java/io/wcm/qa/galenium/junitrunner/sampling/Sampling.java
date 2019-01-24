package io.wcm.qa.galenium.junitrunner.sampling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * makes the JUnit Test generate samples instead of testing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Sampling {
  // Just for flagging. no parameters
}
