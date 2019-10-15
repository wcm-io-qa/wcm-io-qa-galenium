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
package io.wcm.qa.glnm.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.utils.GalenUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Convenience methods for file and path handling.
 *
 * @since 1.0.0
 */
public final class FileHandlingUtil {

  private static final Logger LOG = LoggerFactory.getLogger(FileHandlingUtil.class);

  private FileHandlingUtil() {
    // do not instantiate
  }

  /**
   * <p>constructRelativeFile.</p>
   *
   * @param rootDirectory to be relative to
   * @param file to get relative path for
   * @return file with relative path
   * @since 3.0.0
   */
  public static File constructRelativeFile(File rootDirectory, File file) {
    return new File(constructRelativePath(rootDirectory, file));
  }

  /**
   * <p>constructRelativeFile.</p>
   *
   * @param rootPath to be relative to
   * @param filePath to make relative
   * @return file with relative path
   * @since 3.0.0
   */
  public static File constructRelativeFile(String rootPath, String filePath) {
    return new File(constructRelativePath(rootPath, filePath));
  }

  /**
   * <p>constructRelativePath.</p>
   *
   * @param rootDirectory to be relative to
   * @param file to get relative path for
   * @return relative path for file
   * @since 3.0.0
   */
  public static String constructRelativePath(File rootDirectory, File file) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("attempt making '" + file + "' relative to '" + rootDirectory + "'");
    }
    try {
      String rootPath = rootDirectory.getCanonicalPath();
      String filePath = file.getCanonicalPath();
      if (LOG.isTraceEnabled()) {
        LOG.trace("constructing path for '" + filePath + "' relative to '" + rootPath + "'");
      }
      String relativePath = constructRelativePath(rootPath, filePath);
      if (LOG.isTraceEnabled()) {
        LOG.trace("relative path: '" + relativePath + "'");
      }
      return relativePath;
    }
    catch (IOException ex) {
      throw new GaleniumException("when constructing relative file path.", ex);
    }
  }

  /**
   * <p>constructRelativePath.</p>
   *
   * @param rootPath to be relative to
   * @param filePath to make relative
   * @return file with relative path
   * @since 3.0.0
   */
  public static String constructRelativePath(String rootPath, String filePath) {
    return StringUtils.difference(rootPath, filePath);
  }

  /**
   * <p>ensureParent.</p>
   *
   * @param file will have an existing parent directory on success
   * @since 3.0.0
   */
  public static void ensureParent(File file) {
    File parentFile = file.getParentFile();
    if (!parentFile.isDirectory()) {
      try {
        LOG.debug("ensuring directory exists: " + parentFile.getPath());
        GalenUtils.makeSureFolderExists(parentFile);
      }
      catch (IOException ex) {
        throw new GaleniumException("problem when ensuring directory is present", ex);
      }
    }
  }

}
