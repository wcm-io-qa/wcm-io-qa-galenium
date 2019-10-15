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
package io.wcm.qa.glnm.differences.generic;

import java.io.File;

/**
 * Differences prepared to give path to properties file and key within properties file.
 *
 * @since 1.0.0
 */
public class TextPersistenceDifferences extends LayeredDifferences {

  /** {@inheritDoc} */
  @Override
  public String asFilePath() {
    return new File(getPrimary().asFilePath(), getSecondary().asPropertyKey()).getPath();
  }

  /** {@inheritDoc} */
  @Override
  public String asPropertyKey() {
    return getTertiary().asPropertyKey();
  }

}
