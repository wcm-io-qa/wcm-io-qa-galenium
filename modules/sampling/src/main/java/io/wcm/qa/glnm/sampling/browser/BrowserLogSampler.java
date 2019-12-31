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
package io.wcm.qa.glnm.sampling.browser;

import java.util.Collection;
import java.util.logging.Level;

import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.sampling.base.CachingBasedSampler;

/**
 * Samples browser log as separate strings.
 *
 * @since 4.0.0
 */
public class BrowserLogSampler extends CachingBasedSampler<Iterable<String>> {

  private Level level;

  /**
   * With default log level {@link java.util.logging.Level#SEVERE}.
   */
  public BrowserLogSampler() {
    this(Level.SEVERE);
  }

  /**
   * <p>
   * With custom level.
   * </p>
   *
   * @param level will sample only entries at or above this level
   */
  public BrowserLogSampler(Level level) {
    setLevel(level);
  }

  @Override
  protected Collection<String> freshSample() {
    return Browser.getLog().getMessages(getLevel());
  }

  /**
   * <p>Getter for the field <code>level</code>.</p>
   *
   * @return a {@link java.util.logging.Level} object.
   */
  public Level getLevel() {
    return level;
  }

  /**
   * <p>Setter for the field <code>level</code>.</p>
   *
   * @param level a {@link java.util.logging.Level} object.
   */
  public void setLevel(Level level) {
    this.level = level;
  }

}
