/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.example.provider;

import org.testng.annotations.DataProvider;

import io.wcm.qa.glnm.providers.TestNgProviderUtil;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.aem.AllPagesForTemplateSampler;

/**
 * {@link DataProvider} for AEM content paths using JCR query on author.
 */
public final class ContentPathProvider {

  /**
   * All pages created with the example application.
   */
  public static final String ALL_PAGES_FOR_EXAMPLE_TEMPLATES = "AllPagesForTemplates";
  private static final String ROOT_PATH = "/content/wcm-io-samples";
  private static final String TEMPLATE_NAME_PATTERN = "/apps/wcm-io-samples/core/templates/%";

  private ContentPathProvider() {
    // do not instantiate
  }

  /**
   * @return all pages created with the example application
   */
  @DataProvider(name = ALL_PAGES_FOR_EXAMPLE_TEMPLATES)
  public static Object[][] allPages() {
    GaleniumReportUtil.getLogger().debug("Data providing: " + ALL_PAGES_FOR_EXAMPLE_TEMPLATES);
    Sampler<Iterable> sampler = new AllPagesForTemplateSampler(TEMPLATE_NAME_PATTERN, ROOT_PATH);
    return TestNgProviderUtil.fromSampler(sampler);
  }
}
