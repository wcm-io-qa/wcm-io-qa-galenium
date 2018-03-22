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
package io.wcm.qa.galenium.sampling.images;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_WARN;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getActualImagesDirectory;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getExpectedImagesDirectory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.page.PageElement;
import com.galenframework.page.Rect;
import com.galenframework.specs.Spec;
import com.galenframework.validation.CombinedValidationListener;
import com.galenframework.validation.ImageComparison;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.InteractionUtil;


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
          BufferedImage actualImage = getActualImage(result);
          if (actualImage == DUMMY_IMAGE) {
            trace("actual image sample could not be retrieved: " + objectName);
            BufferedImage pageElementImage = getPageElementScreenshot(pageValidation, objectName);
            if (pageElementImage != null) {
              trace("made secondary image sample: " + objectName);
              actualImage = pageElementImage;
            }
            else {
              getLogger().debug(MARKER_WARN, "failed to make secondary image sample: " + objectName);
            }
          }
          debug("image: " + imagePath + " (" + actualImage.getWidth() + "x" + actualImage.getHeight() + ")");
          try {
            File imageFile = getImageFile(imagePath);
            trace("begin writing image '" + imageFile.getCanonicalPath());
            ImageIO.write(actualImage, "png", imageFile);
            trace("done writing image '" + imageFile.getCanonicalPath());
          }
          catch (IOException ex) {
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

  private void debugError(String string, Exception ex) {
    getLogger().debug(GaleniumReportUtil.MARKER_ERROR, string, ex);
  }

  private BufferedImage getPageElementScreenshot(PageValidation pageValidation, String objectName) {
    BufferedImage wholePageImage = pageValidation.getPage().getScreenshotImage();
    Long scrollYPosition = InteractionUtil.getScrollYPosition();
    trace("browser is scrolled to position: " + scrollYPosition);
    PageElement element = pageValidation.findPageElement(objectName);
    if (element != null) {
      Rect area = element.getArea();
      trace("found element '" + objectName + "': " + area);
      try {
        BufferedImage elementImage = wholePageImage.getSubimage(area.getLeft(), area.getTop(), area.getWidth(), area.getHeight());
        return elementImage;
      }
      catch (RuntimeException ex) {
        debugError("exception when extracting secondary sample image.", ex);
      }
    }
    return null;
  }

  private void trace(String msg) {
    getLogger().trace(msg);
  }

  protected BufferedImage getActualImage(ValidationResult result) {

    ValidationError error = result.getError();
    if (error != null) {
      ImageComparison imageComparison = error.getImageComparison();
      if (imageComparison != null) {
        BufferedImage actualImage = imageComparison.getOriginalFilteredImage();
        if (actualImage != null) {
          return actualImage;
        }
        else {
          trace("could not find sampled image in image comparison.");
        }
      }
      else {
        trace("could not find image comparison in validation error.");
      }
    }
    else {
      trace("could not find error in validation result.");
    }

    return DUMMY_IMAGE;
  }

  protected File getImageFile(String imagePath) throws IOException {
    String sampledImagesDirectory = getActualImagesDirectory();
    String path;
    if (StringUtils.isNotBlank(sampledImagesDirectory)) {
      String canonical1 = new File(getExpectedImagesDirectory()).getCanonicalPath();
      String canonical2 = new File(imagePath).getCanonicalPath();
      String difference = StringUtils.difference(canonical1, canonical2);
      trace("image path construction image dir: " + canonical1);
      trace("image path construction image path: " + canonical2);
      trace("image path construction difference: " + difference);
      path = sampledImagesDirectory + File.separator + difference;
    }
    else {
      path = imagePath;
    }
    File imageFile = new File(path);
    File parentFile = imageFile.getParentFile();
    if (!parentFile.isDirectory()) {
      debug("creating directory: " + parentFile.getPath());
      FileUtils.forceMkdir(parentFile);
    }
    return imageFile;
  }

  protected Pattern getImagePathExtractionRegEx() {
    return Pattern.compile(REGEX_IMAGE_FILENAME);
  }

  protected void logSpec(Spec spec) {
    debug("checking for image file: " + spec.toText() + " (with regex: " + REGEX_IMAGE_FILENAME + ")");
  }

}
