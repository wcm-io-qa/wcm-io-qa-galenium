/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.glnm.selenium;

/**
 * Run modes are set externally to enable running in different scenarios.
 */
public enum RunMode {

  /**
   * Run a subset of tests to facilitate fast turnover during development.
   */
  DEV("dev"),
  /**
   * Run tests locally.
   */
  LOCAL("local"),
  /**
   * Run tests on remote grid.
   */
  REMOTE("remote");

  private final String mRunMode;

  RunMode(String pRunMode) {
    this.mRunMode = pRunMode;
  }

  /**
   * @return string representation of run mode.
   */
  public String getRunMode() {
    return mRunMode;
  }
}
