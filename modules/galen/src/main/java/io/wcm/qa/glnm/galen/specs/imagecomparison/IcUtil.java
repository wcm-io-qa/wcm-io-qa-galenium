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
package io.wcm.qa.glnm.galen.specs.imagecomparison;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getActualImagesDirectory;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getExpectedImagesDirectory;
import static io.wcm.qa.glnm.util.FileHandlingUtil.constructRelativePath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.parser.SyntaxException;
import com.galenframework.speclang2.specs.SpecReader;
import com.galenframework.specs.Spec;
import com.galenframework.validation.ImageComparison;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.FileHandlingUtil;

/**
 * Utility methods for Galenium image comparison via Galen.
 */
final class IcUtil {

  private static final String DUMMY_IMAGE_FORMAT = "png";

  private static final BufferedImage DUMMY_IMAGE = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);

  private static final Logger LOG = LoggerFactory.getLogger(IcUtil.class);

  private static final String REGEX_IMAGE_FILENAME = ".*image file ([^,]*\\.png).*";
  static final Pattern REGEX_PATTERN_IMAGE_FILENAME = Pattern.compile(REGEX_IMAGE_FILENAME);

  private IcUtil() {
    // do not instantiate
  }

  static void createDummyIfSampleDoesNotExist(String fullFilePath) {
    if (IcUtil.isExpectedImageSampleMissing(fullFilePath)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Cannot find sample. Substituting dummy for '" + fullFilePath + "'");
      }

      // if image is missing, we'll substitute a dummy to force Galen to at least sample the page
      File targetFile = new File(fullFilePath);

      writeDummySample(targetFile);
    }
  }

  private static String getImageComparisonSpecText(String folder, String fileName, String error, int offset, List<Selector> toIgnore) {
    StringBuilder specText = new StringBuilder()
    // boiler plate
        .append("image file ")
    // image file
        .append(getImageOrDummySamplePath(folder, fileName));

    // tolerance
    if (StringUtils.isNotBlank(error)) {
      specText.append(", error ")
          .append(error);
    }
    if (offset > 0) {
      specText.append(", analyze-offset ");
      specText.append(offset);
    }
    if (!toIgnore.isEmpty()) {
      List<Selector> objects = toIgnore;
      specText.append(", ignore-objects ");
      if (objects.size() == 1) {
        specText.append(objects.get(0));
      }
      else {
        specText.append("[");
        Collection<String> elementNames = new HashSet<String>();

        for (Selector object : objects) {
          elementNames.add(object.elementName());
        }

        specText.append(StringUtils.join(elementNames, ", "));
        specText.append("]");
      }
    }

    return specText.toString();
  }

  private static String getImageOrDummySamplePath(String folder, String fileName) {
    String fullFilePath;

    // folder
    if (StringUtils.isNotBlank(folder)) {
      fullFilePath = FilenameUtils.concat(folder, fileName);
    }
    else {
      // no folder means fileName is all the path info we have
      fullFilePath = fileName;
    }

    createDummyIfSampleDoesNotExist(fullFilePath);

    return fullFilePath;
  }

  static String getImagePathFrom(Spec spec) {
    Matcher matcher = REGEX_PATTERN_IMAGE_FILENAME.matcher(spec.toText());
    if (matcher.matches() && matcher.groupCount() >= 1) {
      return matcher.group(1);
    }
    return "";
  }

  private static File getOriginalFilteredImage(ValidationResult result) {
    ValidationError error = result.getError();
    if (error == null) {
      LOG.debug("could not find error in validation result.");
      return null;
    }

    ImageComparison imageComparison = error.getImageComparison();
    if (imageComparison == null) {
      LOG.debug("could not find image comparison in validation error.");
      return null;
    }

    File actualImage = imageComparison.getOriginalFilteredImage();
    if (actualImage == null) {
      LOG.debug("could not find sampled image in image comparison.");
    }

    return actualImage;
  }

  private static File getSampleSourceFile(Spec spec, ValidationResult result) {
    String imagePath = getImagePathFrom(spec);
    if (StringUtils.isBlank(imagePath)) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("could not extract image name from: " + spec.toText());
      }
      return null;
    }
    File imageFile = getOriginalFilteredImage(result);
    if (imageFile != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("sample source file: " + imageFile.getPath());
      }
      return imageFile;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("sample source path: " + imagePath);
    }
    return new File(imagePath);
  }

  private static File getSampleTargetFile(Spec spec) {
    String targetPath = getTargetPathFrom(spec);
    File imageFile = new File(targetPath);
    FileHandlingUtil.ensureParent(imageFile);
    return imageFile;
  }

  private static String getTargetPathFrom(Spec spec) {
    File rootDirectory = new File(getExpectedImagesDirectory());
    String imagePathFromSpec = getImagePathFrom(spec);
    String relativeImagePath = constructRelativePath(rootDirectory, new File(imagePathFromSpec));
    return getActualImagesDirectory() + File.separator + relativeImagePath;
  }

  private static boolean isExpectedImageSampleMissing(String fullFilePath) {
    return !new File(fullFilePath).isFile();
  }

  private static File writeDummySample(File targetFile) {
    try {
      if (LOG.isTraceEnabled()) {
        LOG.trace("begin writing dummy image '" + targetFile);
      }
      FileHandlingUtil.ensureParent(targetFile);
      if (ImageIO.write(DUMMY_IMAGE, DUMMY_IMAGE_FORMAT, targetFile)) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("done writing dummy image '" + targetFile);
        }
      }
      else if (LOG.isInfoEnabled()) {
        LOG.info("could not write dummy image '" + targetFile);
      }
      return targetFile;
    }
    catch (IOException ex) {
      throw new GaleniumException("could not write dummy image.", ex);
    }
  }

  static String getImageComparisonSpecText(IcsDefinition def) {
    return IcUtil.getImageComparisonSpecText(
        def.getFoldername(),
        def.getFilename(),
        def.getAllowedError(),
        def.getAllowedOffset(),
        def.getObjectsToIgnore());
  }

  static Spec getSpecForText(String specText) {
    try {
      return new SpecReader().read(specText);
    }
    catch (IllegalArgumentException | SyntaxException ex) {
      String msg = "when parsing spec text: '" + specText + "'";
      LOG.error(msg);
      throw new GaleniumException(msg, ex);
    }
  }

  static String getZeroToleranceImageComparisonSpecText(IcsDefinition def) {
    return getImageComparisonSpecText(
        def.getFoldername(),
        def.getFilename(),
        "",
        0,
        def.getObjectsToIgnore());
  }

  static boolean isImageComparisonSpec(Spec spec) {
    return StringUtils.contains(spec.toText(), "image file ");
  }

  static void saveSample(String objectName, Spec spec, ValidationResult result) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("checking for image file: " + spec.toText() + " (with regex: " + REGEX_PATTERN_IMAGE_FILENAME.pattern() + ")");
    }
    File source = getSampleSourceFile(spec, result);
    if (source == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("did not find source file: " + objectName);
      }
      return;
    }

    File target = getSampleTargetFile(spec);
    if (LOG.isTraceEnabled()) {
      LOG.trace("begin copying image '" + source + "' -> '" + target + "'");
    }
    try {
      FileUtils.copyFile(source, target);
    }
    catch (GaleniumException | IOException ex) {
      String msg = "could not write image: " + target;
      LOG.error(msg, ex);
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace("done copying image '" + source + "' -> '" + target + "'");
    }
  }

}
