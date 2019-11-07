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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;
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
    if (specRun.isFailed()) {
      throw new GaleniumException(errorMessage);
    }
  }

}
