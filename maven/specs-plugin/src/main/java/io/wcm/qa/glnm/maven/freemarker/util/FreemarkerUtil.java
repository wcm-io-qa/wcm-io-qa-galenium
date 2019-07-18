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
package io.wcm.qa.glnm.maven.freemarker.util;

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.interaction.FormElement;
import io.wcm.qa.glnm.maven.freemarker.methods.ClassNameFromSelectorMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.ClassNameFromSpecMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.ClassNameFromStringMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.ConstantNameMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.EscapeJavaDocMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.EscapeJavaMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.FullSelectorClassNameFromSelectorMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.FullWebElementClassNameFromSelectorMethod;
import io.wcm.qa.glnm.maven.freemarker.methods.PackageNameMethod;
import io.wcm.qa.glnm.maven.freemarker.pojo.InteractionPojo;
import io.wcm.qa.glnm.maven.freemarker.pojo.SpecPojo;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Utility methods to build data models for use in freemarker code generation.
 */
public final class FreemarkerUtil {

  private static final Configuration CONFIGURATION = generateConfiguration();

  private FreemarkerUtil() {
    // do not instantiate
  }

  /**
   * @param packageRoot package for interactive selector interface
   * @param interfaceName name of interactive selector interface to implement
   * @param className of interactive selector class
   * @return data model for generating interactive selector interface
   */
  public static Map<String, Object> getDataModelForInteractiveSelector(String packageRoot, String interfaceName, String className) {
    Map<String, Object> model = getCommonDataModel();
    model.put("elementInteraction", new InteractionPojo(Element.class));
    model.put("formElementInteraction", new InteractionPojo(FormElement.class));
    model.put("interactiveSelectorPackage", packageRoot);
    model.put("interactiveSelectorBaseClassName", className);
    model.put("interactiveSelectorInterfaceClassName", interfaceName);

    return model;
  }

  /**
   * @param selector selector to build data model for
   * @param spec selector is taken from
   * @param packageName package of interactive selector interface
   * @param interactiveClassName name of class
   * @param interactiveInterfaceName name of interactive selector interface
   * @return data model for use in generating selector class
   */
  public static Map<String, Object> getDataModelForSelector(
      NestedSelector selector,
      SpecPojo spec,
      String packageName,
      String interactiveClassName,
      String interactiveInterfaceName) {
    Map<String, Object> model = getDataModelForInteractiveSelector(packageName, interactiveInterfaceName, interactiveClassName);
    model.put("className", new ClassNameFromSelectorMethod());
    model.put("spec", spec);
    model.put("this", selector);

    return model;
  }

  /**
   * @param spec to generate Java class for
   * @param packagePrefixSpecs root package
   * @return data model for generating spec class
   */
  public static Map<String, Object> getDataModelForSpec(SpecPojo spec, String packagePrefixSpecs) {
    Map<String, Object> model = getCommonDataModel();
    model.put("className", new ClassNameFromSpecMethod());
    model.put("classNameFromString", new ClassNameFromStringMethod());
    model.put("specRootPackageName", packagePrefixSpecs);
    model.put("spec", spec);
    return model;
  }

  /**
   * @param selector selector to build data model for
   * @param spec selector is taken from
   * @param packageName package of interactive selector interface
   * @param interactiveClassName name of interactive selector implementation
   * @param interactiveInterfaceName name of interactive selector interface
   * @return data model for use in generating selector class
   */
  public static Map<String, Object> getDataModelForWebElement(
      NestedSelector selector,
      SpecPojo spec,
      String packageName,
      String interactiveClassName,
      String interactiveInterfaceName) {
    Map<String, Object> model = getDataModelForSelector(selector, spec, packageName, interactiveClassName, interactiveInterfaceName);
    model.put("gweClassName", new FullWebElementClassNameFromSelectorMethod());
    model.put("selectorClassName", new FullSelectorClassNameFromSelectorMethod());
    return model;
  }

  /**
   * @param outputDir directory
   * @param outputPackage package
   * @param outputClassName class name
   * @return file to write generated code to
   */
  public static File getOutputFile(File outputDir, String outputPackage, String outputClassName) {
    File outputDirectoryPackage = new File(outputDir, outputPackage.replaceAll("\\.", "/"));
    outputDirectoryPackage.mkdirs();
    File outputFile = new File(outputDirectoryPackage, outputClassName + ".java");
    return outputFile;
  }

  /**
   * @param directory template root folder
   * @param name name of template
   * @return freemarker template
   */
  public static Template getTemplate(File directory, String name) {
    try {
      CONFIGURATION.setDirectoryForTemplateLoading(directory);
      Template template = CONFIGURATION.getTemplate(name);
      return template;
    }
    catch (IOException ex) {
      throw new GaleniumException("exception when getting template.", ex);
    }
  }

  /**
   * Actually process template to generate code.
   * @param template to process
   * @param dataModel to use
   * @param outputFile to write to
   */
  public static void process(Template template, Map<String, Object> dataModel, File outputFile) {
    FileWriter out = null;
    try {
      out = new FileWriter(outputFile);
      getLogger().debug("generating '" + outputFile.getPath() + "'");
      template.process(dataModel, out);
      getLogger().info("generated '" + outputFile.getPath() + "'");
    }
    catch (IOException | TemplateException ex) {
      throw new GaleniumException("template exception", ex);
    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (IOException ex) {
          throw new GaleniumException("could not close writer when processing Freemarker template.", ex);
        }
      }
    }
  }

  private static Configuration generateConfiguration() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(getExceptionHandler());
    return cfg;
  }

  private static Map<String, Object> getCommonDataModel() {
    Map<String, Object> model = new HashMap<>();
    model.put("escapeXml", new EscapeJavaDocMethod());
    model.put("escapeJava", new EscapeJavaMethod());
    model.put("constantName", new ConstantNameMethod());
    model.put("packageName", new PackageNameMethod());
    return model;
  }

  private static TemplateExceptionHandler getExceptionHandler() {
    if (GaleniumConfiguration.isSparseReporting()) {
      return TemplateExceptionHandler.RETHROW_HANDLER;
    }
    else {
      return TemplateExceptionHandler.DEBUG_HANDLER;
    }
  }

}
