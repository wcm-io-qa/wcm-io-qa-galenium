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
package io.wcm.qa.glnm.junit.seljup;

import static io.wcm.qa.glnm.junit.seljup.SeleniumJupiterUtil.asBrowserList;
import static io.wcm.qa.glnm.junit.seljup.SeleniumJupiterUtil.getSeleniumExtension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.github.bonigarcia.seljup.BrowserType;
import io.github.bonigarcia.seljup.SeleniumExtension;

class BrowserInjectionExtension
    implements
    BeforeEachCallback,
    ParameterResolver,
    AfterEachCallback,
    AfterAllCallback {

  private final BrowserType browserType;

  BrowserInjectionExtension(BrowserType browser) {
    browserType = browser;
  }

  /** {@inheritDoc} */
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    SeleniumExtension selJup = getSeleniumExtension();
    selJup.putBrowserList(context.getUniqueId(), asBrowserList(browserType));
  }

  /** {@inheritDoc} */
  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return getSeleniumExtension().supportsParameter(parameterContext, extensionContext);
  }

  /** {@inheritDoc} */
  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return getSeleniumExtension().resolveParameter(parameterContext, extensionContext);
  }

  /** {@inheritDoc} */
  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    getSeleniumExtension().afterAll(context);
  }

  /** {@inheritDoc} */
  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    getSeleniumExtension().afterEach(context);
  }

}
