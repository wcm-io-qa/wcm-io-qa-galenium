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
package io.wcm.qa.glnm.device;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Device profile bean.
 *
 * @since 1.0.0
 */
public class DeviceProfile {

  private String browser;
  private String emulator;
  private String name;
  private int screenHeight;
  private int screenWidth;

  /**
   * <p>Getter for the field <code>browser</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public String getBrowser() {
    return browser;
  }

  /**
   * <p>getBrowserType.</p>
   *
   * @return a  {@link io.wcm.qa.glnm.device.BrowserType} object.
   * @since 3.0.0
   */
  public BrowserType getBrowserType() {
    return BrowserType.valueOf(getBrowser());
  }

  /**
   * <p>Getter for the field <code>emulator</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public String getEmulator() {
    return emulator;
  }

  /**
   * <p>getHeight.</p>
   *
   * @return a int.
   * @since 3.0.0
   */
  public int getHeight() {
    return screenHeight;
  }

  /**
   * <p>Getter for the field <code>name</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * <p>getWidth.</p>
   *
   * @return a int.
   * @since 3.0.0
   */
  public int getWidth() {
    return screenWidth;
  }

  /**
   * <p>Setter for the field <code>browser</code>.</p>
   *
   * @param browser a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public void setBrowser(String browser) {
    this.browser = browser;
  }

  /**
   * <p>Setter for the field <code>emulator</code>.</p>
   *
   * @param emulator a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public void setEmulator(String emulator) {
    this.emulator = emulator;
  }

  /**
   * <p>setHeight.</p>
   *
   * @param height a int.
   * @since 3.0.0
   */
  public void setHeight(int height) {
    this.screenHeight = height;
  }

  /**
   * <p>Setter for the field <code>name</code>.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p>setWidth.</p>
   *
   * @param width a int.
   * @since 3.0.0
   */
  public void setWidth(int width) {
    this.screenWidth = width;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
