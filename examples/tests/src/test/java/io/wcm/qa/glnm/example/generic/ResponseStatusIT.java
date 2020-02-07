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
package io.wcm.qa.glnm.example.generic;

import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.seljup.Options;
import io.wcm.qa.glnm.junit.CheckResponseCodes;
import io.wcm.qa.glnm.junit.ChromeOptionsUtil;

public class ResponseStatusIT implements CheckResponseCodes {

  /**
   * Chrome options with performance logging will be used when initializing chrome driver.
   */
  @Options
  ChromeOptions chromeOptions = ChromeOptionsUtil.withPerformanceLog();

}
