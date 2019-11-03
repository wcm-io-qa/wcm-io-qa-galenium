/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.glnm.galen.specs.imagecomparison;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.specs.Spec;
import com.galenframework.validation.CombinedValidationListener;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationResult;

/**
 * {@link com.galenframework.validation.CombinedValidationListener} to handle storing of sampled image files in ZIP file.
 *
 * @since 2.0.0
 */
public class IcValidationListener extends CombinedValidationListener {

  private static final Logger LOG = LoggerFactory.getLogger(IcValidationListener.class);

  /** {@inheritDoc} */
  @Override
  public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult result) {
    super.onSpecError(pageValidation, objectName, spec, result);
    LOG.trace("spec error triggered: " + objectName);
    if (IcUtil.isImageComparisonSpec(spec)) {
      IcUtil.saveSample(objectName, spec, result);
    }
    else {
      LOG.trace("not an image comparison spec");
    }
  }

}
