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

import static java.util.Collections.singletonList;

import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.seljup.BrowserBuilder;
import io.github.bonigarcia.seljup.BrowserType;
import io.github.bonigarcia.seljup.BrowsersTemplate.Browser;
import io.github.bonigarcia.seljup.SeleniumExtension;

final class SeleniumJupiterUtil {

  private static final SeleniumExtension SELENIUM_EXTENSION = new SeleniumExtension();
  private static final Logger LOG = LoggerFactory.getLogger(SeleniumJupiterUtil.class);

  private SeleniumJupiterUtil() {
    // do not instantiate
  }

  static Object getDriverFromSelJup(ParameterContext driverParamContext, ExtensionContext context) {
    if (getSeleniumExtension().supportsParameter(driverParamContext, context)) {
      return getSeleniumExtension().resolveParameter(driverParamContext, context);
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("Selenium Jupiter Extension does not support driver parameter.");
    }
    return null;
  }

  static List<Browser> asBrowserList(BrowserType type) {
    BrowserBuilder builder;
    switch (type) {
      case EDGE:
        if (LOG.isDebugEnabled()) {
          LOG.debug("Using Edge.", type);
        }
        builder = BrowserBuilder.edge();
        break;
      case FIREFOX:
        if (LOG.isDebugEnabled()) {
          LOG.debug("Using Firefox.", type);
        }
        builder = BrowserBuilder.firefox();
        break;
      case CHROME:
        if (LOG.isDebugEnabled()) {
          LOG.debug("Using Chrome.", type);
        }
        builder = BrowserBuilder.chrome();
        break;
      default:
        if (LOG.isInfoEnabled()) {
          LOG.info("BrowserType {0} not supported. Using Chrome.", type);
        }
        builder = BrowserBuilder.chrome();
        break;
    }
    Browser browser = builder.browserName(type.name()).version("LATEST").build();

    return singletonList(browser);
  }

  static SeleniumExtension getSeleniumExtension() {
    return SELENIUM_EXTENSION;
  }

}
