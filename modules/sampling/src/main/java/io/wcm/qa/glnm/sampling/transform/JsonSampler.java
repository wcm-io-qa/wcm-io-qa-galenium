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

import com.github.wnameless.json.flattener.JsonFlattener;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;

/**
 * Samples JSON formatted data into a flat map.
 * @param <S>
 */
public class JsonSampler<S extends Sampler<String>> extends TransformationBasedSampler<S, String, Map<String, String>> {

  private static final String STRING_REPRESENTATION_NULL = "null";

  /**
   * @param inputSampler providing the JSON as String
   */
  public JsonSampler(S inputSampler) {
    super(inputSampler);
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
    getLogger().debug("JsonSampler: attempting to parse '" + inputSample + "'");
    return ensureOnlyStringValues(JsonFlattener.flattenAsMap(inputSample));
  }

}
