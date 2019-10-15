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
package io.wcm.qa.glnm.aem;

/**
 * Helper methods for handling AEM components.
 */
public final class AemComponentUtil {

  private AemComponentUtil() {
    // do not instantiate
  }

  /**
   * @return configurable builder for component URL
   */
  public static AemComponentUrlBuilder urlBuilder() {
    return new AemComponentUrlBuilder();
  }

  /**
   * @param contentPath to use in URL
   * @param componentName to use in URL
   * @return builder preconfigured for page and component
   */
  public static AemComponentUrlBuilder urlBuilder(String contentPath, String componentName) {
    return new AemComponentUrlBuilder().setContentPath(contentPath).setComponentName(componentName);
  }
}
