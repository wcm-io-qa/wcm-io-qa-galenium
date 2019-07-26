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
package io.wcm.qa.glnm.example;

import java.util.List;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.device.NoBrowser;
import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.example.provider.ContentPathProvider;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.aem.AemAuthorLoginSampler;
import io.wcm.qa.glnm.sampling.network.JsoupRawStringSampler;
import io.wcm.qa.glnm.verification.diff.StringDiffVerification;
import io.wcm.qa.glnm.verification.util.Check;

/**
 *
 */
public class PageSourceIT extends AbstractExampleBase {

  private String relativePath;


  @Factory(dataProviderClass = ContentPathProvider.class, dataProvider = ContentPathProvider.ALL_PAGES_FOR_EXAMPLE_TEMPLATES)
  public PageSourceIT(String contentPath) {
    super(NoBrowser.instance());
    setRelativePath(contentPath + ".html");
  }

  @Override
  protected String getRelativePath() {
    return relativePath;
  }

  @Test
  public void testPageSource() {
    StringDiffVerification<Sampler<List<String>>,
        Sampler<String>> verification = new StringDiffVerification<Sampler<List<String>>, Sampler<String>>("sample stage", getSampler());

    verification.setCaching(true);
    verification.addDifference(new StringDifference(getRelativePath()));
    Check.verify(verification);
  }

  @SuppressWarnings("unchecked")
  private JsoupRawStringSampler getSampler() {
    JsoupRawStringSampler sampler = new JsoupRawStringSampler("http://localhost:4502" + getRelativePath());
    sampler.setCookieSampler(new AemAuthorLoginSampler());
    return sampler;
  }

  protected void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

}