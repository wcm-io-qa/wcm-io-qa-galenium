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
package io.wcm.qa.glnm.galen.reports;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationErrorException;
import com.galenframework.validation.ValidationObject;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.glnm.exceptions.GalenLayoutException;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;

/**
 * <p>GalenReports class.</p>
 *
 * @since 4.0.0
 */
public final class GalenReports {

  private static final Logger LOG = LoggerFactory.getLogger(GalenReports.class);

  private GalenReports() {
    // do not instantiate
  }

  /**
   * <p>handleGalenSpecRun.</p>
   *
   * @param specRun a {@link io.wcm.qa.glnm.galen.specs.GalenSpecRun} object.
   * @param errorMessage a {@link java.lang.String} object.
   * @param successMessage a {@link java.lang.String} object.
   */
  public static void handleGalenSpecRun(GalenSpecRun specRun, String errorMessage, String successMessage) {
    if (specRun.isClean()) {
      LOG.debug(successMessage);
      return;
    }
    logMessages(specRun.getValidationErrors());
    if (specRun.isFailed()) {
      handleErrors(errorMessage, specRun.getValidationErrors());
    }
  }

  /**
   * <p>handleLayoutReport.</p>
   *
   * @param layoutReport Galen layout report
   * @param errorMessage message to use for errors and failures
   * @param successMessage message to use in case of success
   * @since 4.0.0
   */
  public static void handleLayoutReport(LayoutReport layoutReport, String errorMessage, String successMessage) {
    if (!(layoutReport.errors() > 0 || layoutReport.warnings() > 0)) {
      LOG.debug(successMessage);
      return;
    }
      List<ValidationResult> validationErrorResults = layoutReport.getValidationErrorResults();
      logMessages(validationErrorResults);
      if (layoutReport.errors() > 0) {
        handleErrors(errorMessage, validationErrorResults);
    }
  }

  private static void handleErrors(String errorMessage, List<ValidationResult> validationErrorResults) {
    ValidationResult validationResult = validationErrorResults.get(0);
    List<String> messages = validationResult.getError().getMessages();
    List<ValidationObject> validationObjects = validationResult.getValidationObjects();
    ValidationErrorException ex = new ValidationErrorException(validationObjects, messages);
    throw new GalenLayoutException(errorMessage, ex);
  }

  private static void logMessages(List<ValidationResult> validationErrorResults) {
    for (ValidationResult validationResult : validationErrorResults) {
      ValidationError error = validationResult.getError();
      String errorMessages = StringUtils.join(error.getMessages(), "|");
      LOG.warn(errorMessages);
    }
  }

}
