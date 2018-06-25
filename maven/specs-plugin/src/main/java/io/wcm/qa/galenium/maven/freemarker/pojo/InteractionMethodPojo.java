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
package io.wcm.qa.galenium.maven.freemarker.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.maven.freemarker.util.ReflectionUtil;

public class InteractionMethodPojo {

  private Method method;

  public InteractionMethodPojo(Method method) {
    setMethod(method);
  }

  public String getBody() {
    StringBuilder body = new StringBuilder();
    if (!StringUtils.equals("void", method.getReturnType().getCanonicalName())) {
      body.append("return ");
    }
    body.append("Element.");
    body.append(method.getName());
    body.append("(");
    body.append(getParametersForCall());
    body.append(");");
    return body.toString();
  }

  public String getHead() {
    StringBuilder head = new StringBuilder();
    head.append("public ");
    head.append(method.getGenericReturnType().getTypeName());
    head.append(" ");
    head.append(method.getName());
    head.append("(");
    head.append(getParametersForDeclaration());
    head.append(")");
    return head.toString();
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method2) {
    this.method = method2;
  }

  private String getParametersForCall() {
    ArrayList<String> paramStrings = new ArrayList<String>();
    for (Parameter parameter : method.getParameters()) {
      if (ReflectionUtil.isSelector(parameter.getType())) {
        paramStrings.add("this");
      }
      else {
        paramStrings.add(parameter.getName());
      }
    }
    return StringUtils.join(paramStrings, ", ");
  }

  private String getParametersForDeclaration() {
    ArrayList<String> paramStrings = new ArrayList<String>();
    for (Parameter parameter : method.getParameters()) {
      if (ReflectionUtil.isSelector(parameter.getType())) {
        continue;
      }
      paramStrings.add(getParameterString(parameter));
    }
    return StringUtils.join(paramStrings, ", ");
  }

  private String getParameterString(Parameter parameter) {
    StringBuilder paramString = new StringBuilder();
    paramString.append(parameter.getType().getCanonicalName());
    paramString.append(" ");
    paramString.append(parameter.getName());
    return paramString.toString();
  }
}
