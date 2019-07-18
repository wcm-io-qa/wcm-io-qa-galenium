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
package io.wcm.qa.glnm.maven.freemarker.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.maven.freemarker.util.ReflectionUtil;

/**
 * Pojo to generate interaction methods.
 */
public class InteractionMethodPojo {

  private static final String CODE_PART_PUBLIC = "public ";
  private static final String CODE_PART_VOID = "void";
  private static final String CODE_PART_RETURN = "return ";
  private Method method;

  /**
   * @param method method to generate delegate for
   */
  public InteractionMethodPojo(Method method) {
    setMethod(method);
  }

  /**
   * @return method body for generated class
   */
  public String getBody() {
    StringBuilder body = new StringBuilder();
    if (!StringUtils.equals(CODE_PART_VOID, method.getReturnType().getCanonicalName())) {
      body.append(CODE_PART_RETURN);
    }
    body.append(getSimpleClassName())
        .append(".")
        .append(getMethodName())
        .append("(")
        .append(getParametersForCall())
        .append(");");
    return body.toString();
  }

  private String getMethodName() {
    return getMethod().getName();
  }

  private String getSimpleClassName() {
    return getMethod().getDeclaringClass().getSimpleName();
  }

  /**
   * @return method head for generated class
   */
  public String getHead() {
    StringBuilder head = new StringBuilder();
    head.append(CODE_PART_PUBLIC);
    head.append(getHeadForInterface());
    return head.toString();
  }

  /**
   * @return method head for generated interface
   */
  public String getHeadForInterface() {
    StringBuilder head = new StringBuilder();
    head.append(getMethodReturnType());
    head.append(" ");
    head.append(getMethodName());
    head.append("(");
    head.append(getParametersForDeclaration());
    head.append(")");
    return head.toString();
  }

  private String getMethodReturnType() {
    return method.getGenericReturnType().getTypeName();
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
