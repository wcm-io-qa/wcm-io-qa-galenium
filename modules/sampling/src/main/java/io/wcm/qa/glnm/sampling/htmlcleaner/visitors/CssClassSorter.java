/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.sampling.htmlcleaner.visitors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

/**
 * Sorts strings in class attributes.
 */
public final class CssClassSorter implements TagNodeVisitor {

  private static final String CSS_CLASS = "class";

  @Override
  public boolean visit(TagNode parentNode, HtmlNode htmlNode) {
    if (!(htmlNode instanceof TagNode)) {
      // not a tag node, so no attributes
      return true;
    }
    TagNode node = (TagNode)htmlNode;
    if (!node.hasAttribute(CSS_CLASS)) {
      // no class attribute to sort
      return true;
    }
    String cssClasses = node.getAttributeByName(CSS_CLASS);
    if (StringUtils.isBlank(cssClasses)) {
      // no classes in attribute
      return true;
    }
    String[] splitClasses = StringUtils.split(cssClasses);
    if (ArrayUtils.isSorted(splitClasses)) {
      // classes already sorted
      return true;
    }

    // split
    List<String> cssClassesList = Arrays.asList(splitClasses);
    // sort
    Collections.sort(cssClassesList);
    // replace
    node.removeAttribute(CSS_CLASS);
    node.addAttribute(CSS_CLASS, StringUtils.join(cssClassesList, ' '));
    return true;
  }
}