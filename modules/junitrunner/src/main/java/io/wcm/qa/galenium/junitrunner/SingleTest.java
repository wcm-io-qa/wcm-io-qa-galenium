package io.wcm.qa.galenium.junitrunner;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.openqa.selenium.WebDriver;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/** represents a single test in parameterized junit5 tests */
public class SingleTest implements TestTemplateInvocationContext {

  private final String displayName;
  private final TestDevice testDevice;
  private final String url;

  /** @see SingleTest */
  public SingleTest(TestDevice testDevice, String url) {
    this.testDevice = testDevice;
    this.url = url;
    this.displayName = url + "_" + testDevice.getScreenSize().getWidth();
  }

  @Override
  public String getDisplayName(int invocationIndex) {
    return displayName;
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections.singletonList(new ParameterResolver() {

      @Override
      public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> type = parameterContext.getParameter().getType();
        if (type.isAssignableFrom(TestDevice.class)) {
          return true;
        }
        else {
          return type.isAssignableFrom(String.class);
        }
      }

      @Override
      public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> type = parameterContext.getParameter().getType();
        if (type.isAssignableFrom(TestDevice.class)) {
          WebDriver driver = WebDriverManager.getDriver(testDevice);
          GaleniumContext.getContext().setTestDevice(testDevice);
          GaleniumContext.getContext().setDriver(driver);

          GaleniumReportUtil.getLogger().debug("using device '{}' and url '{}' for test", testDevice, url);

          return testDevice;
        }
        if (type.isAssignableFrom(String.class)) {
          return url;
        }

        return null;
      }

    });

  }

}
