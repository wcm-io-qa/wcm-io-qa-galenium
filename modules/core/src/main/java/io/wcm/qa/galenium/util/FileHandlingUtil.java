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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.exceptions.GaleniumException;

public final class FileHandlingUtil {

  private FileHandlingUtil() {
    // do not instantiate
  }

  public static File constructRelativeFile(File rootDirectory, File file) {
    return new File(constructRelativePath(rootDirectory, file));
  }

  public static File constructRelativeFile(String rootPath, String filePath) {
    return new File(constructRelativePath(rootPath, filePath));
  }

  public static String constructRelativePath(File rootDirectory, File file) {
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("attempt making '" + file + "' relative to '" + rootDirectory + "'");
    }
    try {
      String rootPath = rootDirectory.getCanonicalPath();
      String filePath = file.getCanonicalPath();
      if (getLogger().isTraceEnabled()) {
        getLogger().trace("constructing path for '" + filePath + "' relative to '" + rootPath + "'");
      }
      String relativePath = constructRelativePath(rootPath, filePath);
      if (getLogger().isTraceEnabled()) {
        getLogger().trace("relative path: '" + relativePath + "'");
      }
      return relativePath;
    }
    catch (IOException ex) {
      throw new GaleniumException("when constructing relative file path.", ex);
    }
  }

  public static String constructRelativePath(String rootPath, String filePath) {
    return StringUtils.difference(rootPath, filePath);
  }

  public static void ensureParent(File file) throws IOException {
    File parentFile = file.getParentFile();
    if (!parentFile.isDirectory()) {
      getLogger().debug("creating directory: " + parentFile.getPath());
      FileUtils.forceMkdir(parentFile);
    }
  }

}