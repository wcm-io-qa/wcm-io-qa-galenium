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
package io.wcm.qa.galenium.verification.general;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.github.wnameless.json.flattener.JsonFlattener;

import io.wcm.qa.galenium.sampling.StringSampler;
import io.wcm.qa.galenium.verification.base.CombinedVerification;

public class JsonVerification extends StringVerification {

  private static final String EXPECTED_KEY_PREFIX_JSON_VERIFICATION = "json.";

  private CombinedVerification jsonLeafVerifications = new CombinedVerification();
  private String keyPrefix = EXPECTED_KEY_PREFIX_JSON_VERIFICATION;

  public JsonVerification(String verificationName, String sample) {
    super(verificationName, sample);
  }

  public JsonVerification(String verificationName, StringSampler sampler) {
    super(verificationName, sampler);
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  private StringVerification getVerification(String key, String valueAsString) {
    return new StringVerification(getPreprocessedKey(key), valueAsString);
  }

  private void populateLeafVerifications(String jsonAsString) {
    getLogger().debug("generating JSON verifications for: '" + jsonAsString + "'");
    Map<String, Object> flattenedMap = JsonFlattener.flattenAsMap(jsonAsString);
    for (Entry<String, Object> entry : flattenedMap.entrySet()) {
      Object jsonValue = entry.getValue();
      String valueAsString;
      if (jsonValue != null) {
        valueAsString = jsonValue.toString();
      }
      else {
        valueAsString = "null";
      }
      jsonLeafVerifications.addVerification(getVerification(entry.getKey(), valueAsString));
    }
  }

  private boolean verifyLeafVerifications() {
    return jsonLeafVerifications.verify();
  }

  @Override
  protected Boolean doVerification() {
    populateLeafVerifications(getActualValue());
    return verifyLeafVerifications();
  }

  protected String getPreprocessedKey(String key) {
    return getKeyPrefix() + key;
  }

  @Override
  protected String initExpectedValue() {
    return "NO_EXPECTATIONS_TOWARDS_WHOLE_JSON_STRING";
  }

  @Override
  protected String getSuccessMessage() {
    String message = jsonLeafVerifications.getMessage();
    if (StringUtils.isBlank(message)) {
      return "Checked JSON for '" + getVerificationName() + "' successful";
    }
    return message;
  }

  @Override
  protected String getFailureMessage() {
    return jsonLeafVerifications.getMessage();
  }
}
