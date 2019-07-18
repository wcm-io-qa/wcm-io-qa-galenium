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

import com.galenframework.page.PageElement;
import com.galenframework.page.Rect;

final class MockPageElement extends PageElement {

  @Override
  public String getCssProperty(String cssPropertyName) {
    return "DUMMY_CSS_PROPERTY";
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getLeft() {
    return 0;
  }

  @Override
  public String getText() {
    return null;
  }

  @Override
  public int getTop() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public boolean isPresent() {
    return true;
  }

  @Override
  public boolean isVisible() {
    return true;
  }

  @Override
  protected Rect calculateArea() {
    return new Rect(getTop(), getLeft(), getWidth(), getHeight());
  }
}