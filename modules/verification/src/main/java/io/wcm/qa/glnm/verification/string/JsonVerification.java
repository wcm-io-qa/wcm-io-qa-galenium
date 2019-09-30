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
package io.wcm.qa.glnm.verification.string;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.JsonSampler;
import io.wcm.qa.glnm.verification.base.CombinedVerification;
import io.wcm.qa.glnm.verification.base.SamplerBasedVerification;

/**
 * Abstract base class for implementations verifying JSON formatted inputs.
 * @param <S> sampler supplying raw JSON string
 */
public abstract class JsonVerification<S extends Sampler<String>> extends SamplerBasedVerification<JsonSampler<S>, Map<String, String>> {

  private static final Map<String, String> EMPTY_EXPECTED_VALUE = MapUtils.emptyIfNull(null);
  private static final String EXPECTED_KEY_PREFIX_JSON_VERIFICATION = "json";
  private CombinedVerification combinedVerification = new CombinedVerification();

  private String keyPrefix = EXPECTED_KEY_PREFIX_JSON_VERIFICATION;

  protected JsonVerification(String verificationName, S sampler) {
    super(verificationName, new JsonSampler<S>(sampler));
    setExpectedValue(EMPTY_EXPECTED_VALUE);
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  private void addCheck(StringVerification verification) {
    getCombinedVerification().addVerification(verification);
  }

  private String getEmptySuccessMessageSubsitute() {
    return toString() + " successfully verified JSON";
  }

  private StringVerification getStringVerification(String key, String value) {
    return new StringVerification(getExpectedAggregateKey(key), value);
  }

  private boolean verifyChecks() {
    return getCombinedVerification().verify();
  }

  @Override
  protected void afterVerification() {
    getLogger().trace("done verifying (" + toString() + ")");
  }

  @Override
  protected boolean doVerification() {
    populateChecks();
    return verifyChecks();
  }

  protected String getCombinedMessage() {
    return getCombinedVerification().getMessage();
  }

  protected CombinedVerification getCombinedVerification() {
    return combinedVerification;
  }

  protected String getExpectedAggregateKey(String key) {
    return getExpectedKey() + "." + key;
  }

  @Override
  protected String getExpectedKey() {
    return super.getExpectedKey() + "." + getKeyPrefix();
  }

  @Override
  protected String getFailureMessage() {
    return getCombinedMessage();
  }

  @Override
  protected String getSuccessMessage() {
    String message = getCombinedMessage();
    if (StringUtils.isNotBlank(message)) {
      return message;
    }
    return getEmptySuccessMessageSubsitute();
  }

  @Override
  protected Map<String, String> initExpectedValue() {
    throw new GaleniumException("there is no top level expected value, because everything is handled in combined verification.");
  }

  @Override
  protected void persistSample(String key, Map<String, String> newValue) {
    throw new GaleniumException("there is no top level sample persistence, because everything is handled in combined verification.");
  }

  protected void populateChecks() {
    Map<String, String> sample = getSampler().sampleValue();
    for (Entry<String, String> entry : sample.entrySet()) {
      addCheck(getStringVerification(entry.getKey(), entry.getValue()));
    }
  }

  protected void setCombinedVerification(CombinedVerification combinedVerification) {
    this.combinedVerification = combinedVerification;
  }

}
