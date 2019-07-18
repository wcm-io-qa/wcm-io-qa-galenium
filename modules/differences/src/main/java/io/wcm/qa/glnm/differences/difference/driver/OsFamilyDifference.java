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
package io.wcm.qa.glnm.differences.difference.driver;

import org.apache.commons.lang3.SystemUtils;

import io.wcm.qa.glnm.differences.base.DifferenceBase;

/**
 * Uses {@link SystemUtils} to tell apart the big three OSes:
 * <ul>
 * <li>LINUX</li>
 * <li>OSX</li>
 * <li>WINDOWS</li>
 * </ul>
 */
public class OsFamilyDifference extends DifferenceBase {

  private enum OsFamily {
    LINUX, OSX, WINDOWS, UNKNOWN
  }

  @Override
  protected String getRawTag() {
    return getOsFamily().name();
  }

  private OsFamily getOsFamily() {
    if (SystemUtils.IS_OS_LINUX) {
      return OsFamily.LINUX;
    }
    if (SystemUtils.IS_OS_MAC_OSX) {
      return OsFamily.OSX;
    }
    if (SystemUtils.IS_OS_WINDOWS) {
      return OsFamily.WINDOWS;
    }
    return OsFamily.UNKNOWN;
  }

}
