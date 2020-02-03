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
package io.wcm.qa.glnm.example.jupiter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.seljup.SeleniumExtension;
import io.wcm.qa.glnm.context.GaleniumContext;

@ExtendWith(SeleniumExtension.class)
public interface UsesChrome {

  default ChromeOptions chromeOptions() {
    ChromeOptions headlessOptions = new ChromeOptions();
    headlessOptions.setHeadless(true);
    return headlessOptions;
  }

  @BeforeEach
  default void initDriver(ChromeDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  @AfterEach
  default void releaseDriver() {
    GaleniumContext.getContext().setDriver(null);
  }

}
