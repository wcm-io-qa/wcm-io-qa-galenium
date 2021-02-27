package io.wcm.qa.glnm.junit;

import static org.junit.platform.commons.util.ReflectionUtils.isInnerClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.Preconditions;

/** Will execute a method in a junitExtension class. useb by GaleniumExtension */
class MethodExecuter {

  public void executeMethod(ParameterResolver junitExtension, ExtensionContext extensionContext, Class<? extends Annotation> annotatedBy) {
    Arrays.stream(junitExtension.getClass().getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(annotatedBy)).findFirst()
        .ifPresent(method -> executeMethod(junitExtension, extensionContext, method));
  }

  private void executeMethod(ParameterResolver junitExtension, ExtensionContext extensionContext, Method method) {
    try {
      Parameter parameter = method.getParameters()[0];
      DefaultParameterContext parameterContext = new DefaultParameterContext(parameter, 0, Optional.empty());
      Object args = junitExtension.resolveParameter(parameterContext, extensionContext);
      method.invoke(junitExtension, args);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException("could not invoke method", e);
    }
  }

  private class DefaultParameterContext implements ParameterContext {

    private final Parameter parameter;
    private final int index;
    private final Optional<Object> target;

    DefaultParameterContext(Parameter parameter, int index, Optional<Object> target) {
      Preconditions.condition(index >= 0, "index must be greater than or equal to zero");
      this.parameter = Preconditions.notNull(parameter, "parameter must not be null");
      this.index = index;
      this.target = Preconditions.notNull(target, "target must not be null");
    }

    @Override
    public Parameter getParameter() {
      return this.parameter;
    }

    @Override
    public int getIndex() {
      return this.index;
    }

    @Override
    public Optional<Object> getTarget() {
      return this.target;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
      return AnnotationUtils.isAnnotated(getEffectiveAnnotatedParameter(), annotationType);
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationType) {
      return AnnotationUtils.findAnnotation(getEffectiveAnnotatedParameter(), annotationType);
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(Class<A> annotationType) {
      return AnnotationUtils.findRepeatableAnnotations(getEffectiveAnnotatedParameter(), annotationType);
    }

    private AnnotatedElement getEffectiveAnnotatedParameter() {
      Executable executable = getDeclaringExecutable();

      if (executable instanceof Constructor && isInnerClass(executable.getDeclaringClass())
          && executable.getParameterAnnotations().length == executable.getParameterCount() - 1) {

        Preconditions.condition(this.index != 0,
            "A ParameterContext should never be created for parameter index 0 in an inner class constructor");

        return executable.getParameters()[this.index - 1];
      }

      return this.parameter;
    }

  }

}
