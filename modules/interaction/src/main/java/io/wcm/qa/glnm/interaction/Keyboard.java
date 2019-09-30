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
package io.wcm.qa.glnm.interaction;

import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * Keyboard interaction methods.
 */
public final class Keyboard {

  /**
   * Constructor.
   */
  private Keyboard() {
    // do not instantiate
  }

  /**
   * Sending keys to where the current focus is.
   * @param text keys to send
   */
  public static void sendKeys(String text) {
    getActions().sendKeys(text).perform();
  }

  /**
   * Sending keys to where the current focus is.
   * @param keys keys to send
   */
  public static void sendKeys(Keys... keys) {
    getActions().sendKeys(keys).perform();
  }

  private static Actions getActions() {
    return new Actions(getDriver());
  }

}
