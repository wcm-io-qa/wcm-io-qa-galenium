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
package io.wcm.qa.glnm.galen.validation;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.galen.specs.AbstractGalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecParsingProvider;

/**
 * <p>FileBasedGalenSpec class.</p>
 *
 * @since 4.0.0
 */
class FileBasedGalenSpec extends AbstractGalenSpec {

  private static final Logger LOG = LoggerFactory.getLogger(FileBasedGalenSpec.class);
  /**
   * <p>
   * Constructor for FileBasedGalenSpec.
   * </p>
   *
   * @param specFile a {@link java.io.File} object.
   * @param tags include tags
   */
  FileBasedGalenSpec(File specFile, String... tags) {
    this(specFile.getPath(), tags);
  }

  /**
   * <p>
   * Constructor for FileBasedGalenSpec.
   * </p>
   *
   * @param specPath a {@link java.lang.String} object.
   * @param tags include tags
   */
  FileBasedGalenSpec(String specPath, String... tags) {
    super(new GalenSpecParsingProvider(specPath, tags));
    setName(FilenameUtils.getBaseName(specPath));
  }

  @Override
  protected ValidationListener getValidationListener() {
    if (LOG.isTraceEnabled()) {
      return new TracingValidationListener(getUuid());
    }
    return new AllureValidationListener(getUuid());
  }

}
