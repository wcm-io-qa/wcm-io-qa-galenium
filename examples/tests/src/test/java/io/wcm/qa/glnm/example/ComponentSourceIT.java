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
import java.util.Map;

import org.testng.annotations.Test;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.aem.AemAuthorLoginSampler;
import io.wcm.qa.glnm.sampling.aem.AemComponentHtmlSampler;
import io.wcm.qa.glnm.testcase.AbstractNamedTest;
import io.wcm.qa.glnm.verification.diff.StringDiffVerification;
import io.wcm.qa.glnm.verification.util.Check;

public class ComponentSourceIT extends AbstractNamedTest {

  @Test
  public void checkStageSource() {

    AemComponentHtmlSampler sampler = new AemComponentHtmlSampler(getRelativePath(), "stage");
    sampler.setRequestCookies(getLoginCookies());
    StringDiffVerification<Sampler<List<String>>,
        Sampler<String>> verification = new StringDiffVerification<Sampler<List<String>>, Sampler<String>>("sample stage", sampler);

    verification.setCaching(true);
    Check.verify(verification);
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> getLoginCookies() {
    return (Map<String, String>)new AemAuthorLoginSampler().sampleValue();
  }

  protected String getRelativePath() {
    return "/content/wcm-io-samples/en";
  }
}
