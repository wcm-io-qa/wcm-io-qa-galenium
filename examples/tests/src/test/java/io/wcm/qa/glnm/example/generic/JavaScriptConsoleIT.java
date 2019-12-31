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

import java.util.logging.Level;

import org.testng.annotations.Test;

import io.wcm.qa.glnm.example.AbstractExampleBase;
import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.verification.browser.BrowserLogVerification;
import io.wcm.qa.glnm.verification.util.Check;

public class JavaScriptConsoleIT extends AbstractExampleBase {

  @Test
  public void checkConsole() {
    Browser.load(getStartUrl());
    Check.verify(new BrowserLogVerification(Level.WARNING));
  }

  @Override
  protected String getRelativePath() {
    return "/en.html";
  }

}
