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
package io.wcm.qa.glnm.sampling.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wnameless.json.flattener.JsonFlattener;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;

/**
 * Samples JSON formatted data into a flat map.
 *
 * @param <S> type of sampler supplying a JSON string
 * @since 1.0.0
 */
public class JsonSampler<S extends Sampler<String>> extends TransformationBasedSampler<S, String, Map<String, String>> {

  private static final Logger LOG = LoggerFactory.getLogger(JsonSampler.class);

  private static final String STRING_REPRESENTATION_NULL = "null";

  /**
   * <p>Constructor for JsonSampler.</p>
   *
   * @param inputSampler providing the JSON as String
   * @since 3.0.0
   */
  public JsonSampler(S inputSampler) {
    super(inputSampler);
  }

  /**
   * <p>
   * Constructor for JsonSampler which requires using setter to provide input sampler.
   * </p>
   *
   * @since 5.0.0
   */
  public JsonSampler() {
    super();
  }

  private String asString(Entry<String, Object> entry) {
    Object value = entry.getValue();
    if (value == null) {
      return STRING_REPRESENTATION_NULL;
    }
    return value.toString();
  }

  private Map<String, String> ensureOnlyStringValues(Map<String, Object> mapWithObjects) {
    Map<String, String> stringOnlyMap = new HashMap<String, String>();
    for (Entry<String, Object> entry : mapWithObjects.entrySet()) {
      stringOnlyMap.put(entry.getKey(), asString(entry));
    }
    return stringOnlyMap;
  }

  @Override
  protected Map<String, String> transform(String inputSample) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("JsonSampler: attempting to parse '" + inputSample + "'");
    }
    return ensureOnlyStringValues(JsonFlattener.flattenAsMap(inputSample));
  }

  /**
   * Factory method to avoid boiler plate when just wanting to flatten already retrieved JSON.
   *
   * @param json JSON as string
   * @return JSON as a flat String to String map
   * @since 5.0.0
   */
  public static Map<String, String> flatten(String json) {
    FixedStringSampler inputSampler = new FixedStringSampler(json);
    JsonSampler<FixedStringSampler> jsonSampler = new JsonSampler<FixedStringSampler>(inputSampler);
    return jsonSampler.sampleValue();
  }
}
