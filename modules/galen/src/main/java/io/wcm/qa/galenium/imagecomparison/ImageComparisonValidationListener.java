/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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

import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.getActualImagesDirectory;
import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.getExpectedImagesDirectory;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.FileHandlingUtil.constructRelativePath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.specs.Spec;
import com.galenframework.validation.CombinedValidationListener;
import com.galenframework.validation.ImageComparison;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.util.FileHandlingUtil;


/**
 * {@link CombinedValidationListener} to handle storing of sampled image files in ZIP file.
 */
public class ImageComparisonValidationListener extends CombinedValidationListener {

  private static final BufferedImage DUMMY_IMAGE = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);

  // Logger
  private static final Logger log = LoggerFactory.getLogger(ImageComparisonValidationListener.class);

  private static final String REGEX_IMAGE_FILENAME = ".*image file ([^,]*\\.png).*";

  @Override
  public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult result) {
    super.onSpecError(pageValidation, objectName, spec, result);
    trace("spec error triggered: " + objectName);
    if (GaleniumConfiguration.isSaveSampledImages()) {
      String text = spec.toText();
      if (StringUtils.contains(text, "image file ")) {
        trace("saving sample: " + objectName);
        logSpec(spec);
        Matcher matcher = getImagePathExtractionRegEx().matcher(text);
        if (matcher.matches() && matcher.groupCount() >= 1) {
          String imagePath = matcher.group(1);

          debug("image: " + imagePath);
          try {
            File source = getActualImage(result);
            if (source == null) {
              source = new File(imagePath);
            }
            File target = getNewSampleImageTargetFile(imagePath);
            trace("begin copying image '" + source + "' -> '" + target + "'");
            FileUtils.copyFile(source, target);
            trace("done copying image '" + source + "' -> '" + target + "'");
          }
          catch (GaleniumException | IOException ex) {
            String msg = "could not write image: " + imagePath;
            log.error(msg, ex);
            getLogger().error(msg, ex);
          }
        }
        else {
          String msg = "could not extract image name from: " + text;
          log.warn(msg);
          getLogger().warn(msg);
        }
      }
      else {
        trace("not saving sample. " + objectName);
      }
    }
    else {
      trace("not an image comparison spec");
    }
  }

  private void debug(String msg) {
    getLogger().debug(msg);
  }

  private void trace(String msg) {
    getLogger().trace(msg);
  }

  protected File getActualImage(ValidationResult result) {

    ValidationError error = result.getError();
    String msg;
    if (error != null) {
      ImageComparison imageComparison = error.getImageComparison();
      if (imageComparison != null) {
        File actualImage = imageComparison.getOriginalFilteredImage();
        if (actualImage != null) {
          return actualImage;
        }
        else {
          msg = "could not find sampled image in image comparison.";
        }
      }
      else {
        msg = "could not find image comparison in validation error.";
      }
    }
    else {
      msg = "could not find error in validation result.";
    }
    getLogger().debug(msg);

    return null;

  }

  protected File getNewSampleImageTargetFile(String imagePath) {
    String sampledImagesRootPath = getActualImagesDirectory();
    String path;
    if (StringUtils.isNotBlank(sampledImagesRootPath)) {
      File rootDirectory = new File(getExpectedImagesDirectory());
      String relativeImagePath = constructRelativePath(rootDirectory, new File(imagePath));
      path = sampledImagesRootPath + File.separator + relativeImagePath;
    }
    else {
      path = imagePath;
    }
    File imageFile = new File(path);
    FileHandlingUtil.ensureParent(imageFile);
    return imageFile;
  }

  protected Pattern getImagePathExtractionRegEx() {
    return Pattern.compile(REGEX_IMAGE_FILENAME);
  }

  protected void logSpec(Spec spec) {
    debug("checking for image file: " + spec.toText() + " (with regex: " + REGEX_IMAGE_FILENAME + ")");
  }

}
