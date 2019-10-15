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
package io.wcm.qa.glnm.maven.mock;

import java.awt.image.BufferedImage;
import java.io.File;

import com.galenframework.page.Page;
import com.galenframework.page.PageElement;
import com.galenframework.specs.page.Locator;

/**
 * Mock {@link com.galenframework.page.Page} to facilitate parsing specs without need for real browser interaction.
 *
 * @since 1.0.0
 */
public class MockPage implements Page {

  /** {@inheritDoc} */
  @Override
  public Page createFrameContext(PageElement mainObject) {
    return new MockPage();
  }

  /** {@inheritDoc} */
  @Override
  public Page createObjectContextPage(Locator mainObjectLocator) {
    return new MockPage();
  }

  /** {@inheritDoc} */
  @Override
  public PageElement getObject(Locator objectLocator) {
    return new MockPageElement();
  }

  /** {@inheritDoc} */
  @Override
  public PageElement getObject(String objectName, Locator objectLocator) {
    return new MockPageElement();
  }

  /** {@inheritDoc} */
  @Override
  public int getObjectCount(Locator locator) {
    return 2;
  }

  /** {@inheritDoc} */
  @Override
  public File getScreenshotFile() {
    return new File("DUMMY_IMAGE_FILE_PATH");
  }

  /** {@inheritDoc} */
  @Override
  public BufferedImage getScreenshotImage() {
    return new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
  }

  /** {@inheritDoc} */
  @Override
  public PageElement getSpecialObject(String objectName) {
    return new MockPageElement();
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return "DUMMY_PAGE_TITLE";
  }

  /** {@inheritDoc} */
  @Override
  public void setScreenshot(File screenshotFile) {
    // do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void switchToFrame(PageElement mainObject) {
    // do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void switchToParentFrame() {
    // do nothing
  }

}
