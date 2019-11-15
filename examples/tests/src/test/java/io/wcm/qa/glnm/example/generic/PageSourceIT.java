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
package io.wcm.qa.glnm.example.generic;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.example.AbstractExampleBase;
import io.wcm.qa.glnm.example.provider.ContentPathProvider;
import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.verification.driver.PageSourceVerification;
import io.wcm.qa.glnm.verification.util.Check;

public class PageSourceIT extends AbstractExampleBase {

  private String relativePath;

  @Factory(dataProviderClass = ContentPathProvider.class, dataProvider = ContentPathProvider.ALL_PAGES)
  public PageSourceIT(String relativePath) {
    super();
    setRelativePath("en.html");
  }

  @Test
  public void checkPageSource() {
    Browser.load(getStartUrl());
    Check.verify(new ErrorInSource());
  }

  private void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  @Override
  protected String getRelativePath() {
    return relativePath;
  }

  private static class ErrorInSource extends PageSourceVerification {

    ErrorInSource() {
      super("Source Check");
      mustNotContain("Exception", "Exception in source");
      mustNotContain("Error", "Error in source");
    }

  }

}
