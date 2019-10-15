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
package io.wcm.qa.glnm.sampling.aem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;

/**
 * Fetches all pages for
 */
public class AllPagesForTemplateSampler extends TransformationBasedSampler<Sampler<Map<String, String>>, Map<String, String>, Iterable> {

  private static final String PATH_FRAGMENT_JCR_CONTENT = "/jcr:content";
  private static final String PROPERTY_NAME_CQ_TEMPLATE = "cq:template";

  /**
   * @param namePattern used to constrain template names using jcr:like syntax
   * @param rootPath to constrain where to look for pages
   */
  public AllPagesForTemplateSampler(String namePattern, String rootPath) {
    super(buildInputSampler(namePattern, rootPath));
  }



  @Override
  protected Iterable transform(Map<String, String> inputSample) {
    List<String> result = new ArrayList<String>();
    Collection<String> contentPaths = inputSample.values();
    for (String path : contentPaths) {
      result.add(cleanPath(path));
    }
    return result;
  }

  protected String cleanPath(String path) {
    return StringUtils.removeEnd(path, PATH_FRAGMENT_JCR_CONTENT);
  }

  public JcrQuerySampler getQuerySampler() {
    return (JcrQuerySampler)getInput();
  }

  private static JcrQuerySampler buildInputSampler(String templateNamePattern, String rootPath) {
    return new JcrQuerySampler()
        .addLikeProperty(PROPERTY_NAME_CQ_TEMPLATE, templateNamePattern)
        .setPath(rootPath);
  }

}
