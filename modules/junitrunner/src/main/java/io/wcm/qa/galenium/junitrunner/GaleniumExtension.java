package io.wcm.qa.galenium.junitrunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.testng.annotations.Factory;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.junitrunner.sampling.Sampling;
import io.wcm.qa.galenium.listeners.SamplePersistenceListener;

/**
 * Extension to make it possible to start simple galenium tests from your IDE<br>
 * Set the environment variable "developer.chrome.driver" if you do not have the OS specified in FALLBACK_DRIVER
 */
public class GaleniumExtension implements TestTemplateInvocationContextProvider, AfterAllCallback, TestInstancePostProcessor {

  /**
   * @see GaleniumExtension
   */
  public GaleniumExtension() {
    System.setProperty("selenium.runmode", "local");
    System.setProperty("webdriver.chrome.driver", StringUtils.defaultString(System.getenv("developer.chrome.driver"), getFallBackDriver()));

    setSystemVariable("io.wcm.qa.http.user");
    setSystemVariable("io.wcm.qa.http.pass");

    System.setProperty("galenium.sampling.text.directory.expected", "./target/test-classes/text");
    System.setProperty("galenium.sampling.text.file", "expected.properties");

    System.setProperty("galenium.sampling.image.directory.expected", "./target/test-classes/images");
    System.setProperty("galenium.sampling.image.directory.actual", "./target/sampled/images");
    System.setProperty("galenium.sampling.image.save", "true");

    System.setProperty("galenium.retryMax", "0");

    System.setProperty("galenium.browsermob.proxy", "true");
  }

  protected void setSystemVariable(String propertyName) {
    String userValue = System.getenv(propertyName);
    if (StringUtils.isNotEmpty(userValue)) {
      System.setProperty(propertyName, userValue);
    }
  }

  private String getFallBackDriver() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return "target/driverbinaries/windows/googlechrome/64bit/chromedriver.exe";
    }
    else if (SystemUtils.IS_OS_LINUX) {
      return "target/driverbinaries/linux/googlechrome/64bit/chromedriver";
    }
    else if (SystemUtils.IS_OS_MAC) {
      return "target/driverbinaries/mac/googlechrome/64bit/chromedriver";
    }
    throw new RuntimeException("Could not detect operation system from name '" + SystemUtils.OS_NAME + "'");
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    if (isSamplingMode(testInstance.getClass())) {
      System.setProperty("galenium.sampling.verification.ignoreErrors", "true");
      System.out.println("starting sampling...");
    }
  }

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return true;
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

    Object[][] providedData = getProvidedData(context.getRequiredTestClass());

    List<TestTemplateInvocationContext> tests = new ArrayList<>();
    if (isSamplingMode(context.getRequiredTestClass())) {
      for (Object[] testcase : providedData) {
        tests.add(createTest(testcase));
      }
    }
    else {
      tests.add(createTest(providedData[0]));
    }

    return tests.stream();
  }

  private SingleTest createTest(Object[] testcase) {
    TestDevice testDevice = (TestDevice)testcase[0];
    String url = (String)testcase[1];
    return new SingleTest(testDevice, url);
  }

  private Object[][] getProvidedData(Class<?> testClass) {

    try {
      Constructor<? extends Object> constructor = testClass.getConstructor(TestDevice.class, String.class);
      Factory annotation = constructor.getAnnotation(Factory.class);

      Method providerMethod = Arrays.stream(annotation.dataProviderClass().getDeclaredMethods())
          .filter(method -> method.isAnnotationPresent(org.testng.annotations.DataProvider.class))
          .filter(method -> method.getAnnotation(org.testng.annotations.DataProvider.class).name().equals(annotation.dataProvider()))
          .findFirst().get();

      if (providerMethod.getParameterTypes().length == 0) {
        return (Object[][])providerMethod.invoke(null);
      }
      else if (providerMethod.getParameterTypes()[0].equals(Constructor.class)) {
        return (Object[][])providerMethod.invoke(null, constructor);
      }
      else {
        throw new RuntimeException("unrecognised parameter in provider method: " + providerMethod.getParameterTypes()[0]);
      }

    }
    catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      throw new RuntimeException("could not read provided data for tests", ex);
    }
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    if (StringUtils.equals(System.getProperty("galenium.sampling.verification.ignoreErrors"), "true")) {
      try {
        writeToExpectedProperties();
      }
      catch (IOException ex) {
        throw new RuntimeException("could not write to expected properties", ex);
      }
    }
  }

  /** Write the sample results to the expected results */
  private void writeToExpectedProperties() throws IOException {
    Path samplesFolder = Paths.get("./target/galenium-reports/");
    Path expectedFolder = Paths.get("./src/test/resources/text/");

    getPropertyFilesInFolder(samplesFolder).forEach(file -> {
      try {
        Files.deleteIfExists(file);
      }
      catch (IOException ex) {
        throw new RuntimeException("could not delete files", ex);
      }
    });

    new SamplePersistenceListener().onFinish(null);

    getPropertyFilesInFolder(samplesFolder).filter(file -> Files.exists(file)).forEach(file -> {
      try {
        Files.move(file, expectedFolder.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("generates expected properties: " + file);
      }
      catch (IOException ex) {
        throw new RuntimeException("could not generate expected properties", ex);
      }
    });

  }

  private boolean isSamplingMode(Class<?> testClazz) {
    return testClazz.getAnnotation(Sampling.class) != null;
  }

  private Stream<Path> getPropertyFilesInFolder(Path samplesFolder) throws IOException {
    return Files.walk(samplesFolder).filter(s -> s.toString().endsWith(".properties"));
  }

}
