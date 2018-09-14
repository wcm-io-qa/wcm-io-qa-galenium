/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.example;

import org.testng.ITest;
import org.testng.annotations.Test;

import io.wcm.qa.galenium.differences.specialized.TestNameDifferences;
import io.wcm.qa.galenium.interaction.Aem;
import io.wcm.qa.galenium.util.GaleniumContext;


public class HttpPostLogin implements ITest {

  private TestNameDifferences testNameDifferences;
  private static final String ROOT_PACKAGE_FOR_TESTS = "io.wcm.qa";

  @Test
  public void loginViaHttpPost() {
    String pass = "admin";
    String user = "admin";
    String baseUrl = "http://localhost:4502";
    GaleniumContext.getAssertion().assertTrue(Aem.loginToAuthorViaHttp(baseUrl, user, pass), "Logged in via HTTP");
  }

  @Override
  public String getTestName() {
    return getNameDifferences().asPropertyKey();
  }

  protected TestNameDifferences getNameDifferences() {
    if (testNameDifferences == null) {
      testNameDifferences = new TestNameDifferences();
      testNameDifferences.setClass(getClass());
      testNameDifferences.setRootPackage(ROOT_PACKAGE_FOR_TESTS);
    }
    return testNameDifferences;
  }


}
