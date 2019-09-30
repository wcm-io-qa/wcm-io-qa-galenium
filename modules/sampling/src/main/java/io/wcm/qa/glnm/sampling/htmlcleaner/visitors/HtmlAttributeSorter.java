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

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

/**
 * Sorts all attributes.
 */
public class HtmlAttributeSorter implements TagNodeVisitor {

  @Override
  public boolean visit(TagNode parentNode, HtmlNode htmlNode) {

    if (!(htmlNode instanceof TagNode)) {
      // not a tag node, so no attributes
      return true;
    }

    TagNode node = (TagNode)htmlNode;

    Map<String, String> attributes = node.getAttributes();
    Set<String> keySet = new TreeSet<String>();
    keySet.addAll(attributes.keySet());
    for (String key : keySet) {
      node.removeAttribute(key);
      node.addAttribute(key, attributes.get(key));
    }

    return true;
  }

}
