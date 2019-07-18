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
package io.wcm.qa.glnm.verification.proxy;

import java.util.ArrayList;
import java.util.List;

import com.browserup.harreader.model.HarEntry;

import io.wcm.qa.glnm.sampling.proxy.HarEntrySampler;
import io.wcm.qa.glnm.verification.stability.Stability;

/**
 * Verifies stability of entries in Har. Succeeds if the same number of entries are in Har in two consequtive
 * verification attempts.
 */
public class HarStability extends Stability<List<HarEntry>> {

  /**
   * Constructor.
   */
  public HarStability() {
    super(new HarEntrySampler());
  }

  private boolean containsAllElements(List<HarEntry> list1, List<HarEntry> list2) {
    for (HarEntry harEntry : list2) {
      if (!list1.contains(harEntry)) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean checkForEquality(List<HarEntry> oldList, List<HarEntry> newList) {
    if (oldList.size() != newList.size()) {
      getLogger().debug(getClass().getSimpleName() + ": different number of entries (" + oldList.size() + " != " + newList.size() + ")");
      return false;
    }
    getLogger().debug("checking " + newList.size() + " entries.");
    if (!containsAllElements(oldList, newList)) {
      getLogger().debug(getClass().getSimpleName() + ": new list contains elements not in old list.");
      return false;
    }
    if (!containsAllElements(newList, oldList)) {
      getLogger().debug(getClass().getSimpleName() + ": old list contains elements not in new list.");
      return false;
    }
    getLogger().debug(getClass().getSimpleName() + ": stable entries");
    return true;
  }

  @Override
  protected void setOldSampleValue(List<HarEntry> oldSampleValue) {
    List<HarEntry> copy = new ArrayList<>();
    copy.addAll(oldSampleValue);
    super.setOldSampleValue(copy);
  }
}
