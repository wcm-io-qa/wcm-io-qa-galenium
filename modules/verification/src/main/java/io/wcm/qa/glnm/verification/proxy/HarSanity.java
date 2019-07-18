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

import java.util.List;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarRequest;
import com.browserup.harreader.model.HarResponse;

/**
 * Verifies stability and then checks for unfinished requests.
 */
public class HarSanity extends HarStability {

  private boolean checkSanity(List<HarEntry> newList) {
    if (newList.isEmpty()) {
      // we want samples
      return false;
    }
    for (HarEntry harEntry : newList) {
      HarResponse response = harEntry.getResponse();
      if (response.getStatus() == 0) {
        // zero status means request not finished
        HarRequest request = harEntry.getRequest();
        if (request == null) {
          getLogger().debug("found entry with response status 0 and NULL request.");
        }
        else {
          getLogger().debug("found entry with response status 0: " + request.getMethod() + "('" + request.getUrl() + "')");
        return false;
        }
      }
    }
    return true;
  }

  @Override
  protected boolean checkForEquality(List<HarEntry> oldList, List<HarEntry> newList) {
    if (super.checkForEquality(oldList, newList)) {
      getLogger().debug("Har is stable. Now checking sanity.");
      return checkSanity(newList);
    }
    return false;
  }
}
