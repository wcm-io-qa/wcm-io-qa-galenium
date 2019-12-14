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
package io.wcm.qa.glnm.maven.specs.handlebars;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;

public class CodeWriter {

  private String className;
  private String packageName;
  private File rootFolder;
  private String sourceCode;

  public String getClassName() {
    return className;
  }


  public String getPackageName() {
    return packageName;
  }


  public File getRootFolder() {
    return rootFolder;
  }


  public String getSourceCode() {
    return sourceCode;
  }


  public void setClassName(String className) {
    this.className = className;
  }


  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }


  public void setRootFolder(File rootFolder) {
    this.rootFolder = rootFolder;
  }


  public void setSourceCode(String sourceCode) {
    this.sourceCode = sourceCode;
  }


  public void write() {
    String packageAsPath = HandleBarsUtil.getPackageAsPath(getPackageName());
    String pathToJavaFile = packageAsPath + "/" + getClassName() + ".java";
    File outputFile = new File(getRootFolder(), pathToJavaFile);
    try {
      FileUtils.write(outputFile, getSourceCode(), StandardCharsets.UTF_8);
    }
    catch (IOException ex) {
      throw new GaleniumException("When writing source to .java file", ex);
    }
  }
}
