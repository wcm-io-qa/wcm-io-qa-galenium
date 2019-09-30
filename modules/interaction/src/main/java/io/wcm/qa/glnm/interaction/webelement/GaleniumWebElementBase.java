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
package io.wcm.qa.glnm.interaction.webelement;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

import io.wcm.qa.glnm.exceptions.GaleniumException;


/**
 * Base class for implementing delegating wrappers for Selenium's web elements.
 */
public abstract class GaleniumWebElementBase
    implements WebElement, WrapsDriver, Locatable, TakesScreenshot {

  private WebElement delegatee;

  protected GaleniumWebElementBase(WebElement element) {
    setDelegatee(element);
  }

  @Override
  public void clear() {
    getDelegatee().clear();
  }

  @Override
  public void click() {
    getDelegatee().click();
  }

  @Override
  public WebElement findElement(By arg0) {
    return getDelegatee().findElement(arg0);
  }

  @Override
  public List<WebElement> findElements(By arg0) {
    return getDelegatee().findElements(arg0);
  }

  @Override
  public String getAttribute(String arg0) {
    return getDelegatee().getAttribute(arg0);
  }

  @Override
  public Coordinates getCoordinates() {
    if (!isLocatable()) {
      throw new GaleniumException("Does not implement Locatable: " + getDelegatee());
    }
    return ((Locatable)getDelegatee()).getCoordinates();
  }

  @Override
  public String getCssValue(String arg0) {
    return getDelegatee().getCssValue(arg0);
  }

  @Override
  public Point getLocation() {
    return getDelegatee().getLocation();
  }

  @Override
  public Rectangle getRect() {
    return getDelegatee().getRect();
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
    return getDelegatee().getScreenshotAs(arg0);
  }

  @Override
  public Dimension getSize() {
    return getDelegatee().getSize();
  }

  @Override
  public String getTagName() {
    return getDelegatee().getTagName();
  }

  @Override
  public String getText() {
    return getDelegatee().getText();
  }

  @Override
  public WebDriver getWrappedDriver() {
    if (!isWrapsDriver()) {
      throw new GaleniumException("Does not implement WrapsDriver: " + getDelegatee());
    }
    return ((WrapsDriver)getDelegatee()).getWrappedDriver();
  }

  @Override
  public boolean isDisplayed() {
    return getDelegatee().isDisplayed();
  }

  @Override
  public boolean isEnabled() {
    return getDelegatee().isEnabled();
  }

  @Override
  public boolean isSelected() {
    return getDelegatee().isSelected();
  }

  @Override
  public void sendKeys(CharSequence... arg0) {
    getDelegatee().sendKeys(arg0);
  }

  @Override
  public void submit() {
    getDelegatee().submit();
  }

  private boolean isLocatable() {
    return getDelegatee() instanceof Locatable;
  }

  private boolean isWrapsDriver() {
    return getDelegatee() instanceof WrapsDriver;
  }

  protected WebElement getDelegatee() {
    return delegatee;
  }

  protected void setDelegatee(WebElement delegatee) {
    this.delegatee = delegatee;
  }

}
