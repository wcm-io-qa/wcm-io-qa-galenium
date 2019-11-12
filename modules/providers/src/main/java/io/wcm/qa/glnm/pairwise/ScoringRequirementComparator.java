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
package io.wcm.qa.glnm.pairwise;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class ScoringRequirementComparator implements Comparator<Requirement> {

  private Map<Value, Integer> scoreMap;

  ScoringRequirementComparator(Collection<Requirement> allReqs) {
    scoreMap = generateScoreMap(allReqs);
  }

  private static Map<Value, Integer> generateScoreMap(Collection<Requirement> allReqs) {
    Map<Value, Integer> scoreMap = new HashMap<Value, Integer>();
    for (Requirement requirement : allReqs) {
      updateScoreMap(scoreMap, requirement);
    }
    return scoreMap;
  }

  private static void updateScoreMap(Map<Value, Integer> map, Requirement requirement) {
    updateScoreMap(map, requirement.getValueA());
    updateScoreMap(map, requirement.getValueB());
  }

  private static void updateScoreMap(Map<Value, Integer> map, Value value) {
    if (map.containsKey(value)) {
      Integer currentScore = map.get(value);
      map.put(value, currentScore + 1);
      return;
    }
    map.put(value, 1);
  }

  private int getScoreFor(Requirement req) {
    return scoreMap.get(req.getValueA()) + scoreMap.get(req.getValueB());
  }

  /** {@inheritDoc} */
  @Override
  public int compare(Requirement req0, Requirement req1) {
    return Integer.compare(getScoreFor(req0), getScoreFor(req1));
  }

}
