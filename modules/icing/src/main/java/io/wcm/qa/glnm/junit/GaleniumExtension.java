package io.wcm.qa.glnm.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.seljup.Arguments;
import io.github.bonigarcia.seljup.SeleniumExtension;
import io.wcm.qa.glnm.context.GaleniumContext;

/**
 * Galenium extension for Jupiter (JUnit 5) tests.
 * Will initialize Galenium with a ChromeDriver 
 */
public class GaleniumExtension extends SeleniumExtension implements BeforeEachCallback, AfterEachCallback {

  @BeforeEach
  void initBrowser(@Arguments("--headless") ChromeDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    new MethodExecuter().executeMethod(this, extensionContext, org.junit.jupiter.api.BeforeEach.class);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    super.afterEach(extensionContext);
    GaleniumContext.getContext().setDriver(null);
  }

}
