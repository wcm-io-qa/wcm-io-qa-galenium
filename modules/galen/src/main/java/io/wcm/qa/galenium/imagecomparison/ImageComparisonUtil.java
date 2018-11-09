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
package io.wcm.qa.galenium.imagecomparison;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.util.FileHandlingUtil;

/**
 * Utility methods for Galenium image comparison via Galen.
 */
final class ImageComparisonUtil {

  private static final BufferedImage DUMMY_IMAGE = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);

  private ImageComparisonUtil() {
    // do not instantiate
  }

  static File writeDummySample() {
    try {
      File dummySample = File.createTempFile("dummy-sample", "png");
      return writeDummySample(dummySample);
    }
    catch (IOException ex) {
      throw new GaleniumException("could create temp file for dummy image.", ex);
    }
  }

  static File writeDummySample(File targetFile) {
    try {
      getLogger().trace("begin writing image '" + targetFile);
      FileHandlingUtil.ensureParent(targetFile);
      ImageIO.write(DUMMY_IMAGE, "png", targetFile);
      getLogger().trace("done writing image '" + targetFile);
      return targetFile;
    }
    catch (IOException ex) {
      throw new GaleniumException("could not write dummy image.", ex);
    }
  }

}
