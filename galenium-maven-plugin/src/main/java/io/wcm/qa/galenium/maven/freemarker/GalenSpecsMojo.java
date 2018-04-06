/* #%L
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
package io.wcm.qa.galenium.maven.freemarker;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
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
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import freemarker.template.Template;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.maven.freemarker.pojo.SpecPojo;
import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.maven.freemarker.util.FreemarkerUtil;
import io.wcm.qa.galenium.maven.freemarker.util.ParsingUtil;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.ConfigurationUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  /**
   * Name of Freemarker template to use for recursively generating static inner classes.
   */
  @Parameter(defaultValue = "class.ftlh", property = "classTemplate")
  private String classTemplate;

  /**
   * Name of Freemarker template to use for generating constants in a class.
   */
  @Parameter(defaultValue = "constants.ftlh", property = "constantsTemplate")
  private String constantsTemplate;

  /**
   * Location of input directory containing Galen specs.
   */
  @Parameter(defaultValue = "${project.basedir}/src/test/resources/galen/specs", property = "inputDir", required = true)
  private File inputDirectory;

  /**
   * Root directory for generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources", property = "outputDir", required = true)
  private File outputDirectory;

  /**
   * Package name to generate code into.
   */
  @Parameter(defaultValue = "io.wcm.qa.galenium.selectors", property = "packageRootName", required = true)
  private String packageRootName;

  /**
   * Name of Freemarker template to use for generating top level classes.
   */
  @Parameter(defaultValue = "root.ftlh", property = "rootTemplate")
  private String rootTemplate;

  private Map<File, Collection<NestedSelector>> specSelectorMapping = new HashMap<>();

  /**
   * To be transferred to system properties when running plugin. Mostly used to manipulate
   * {@link GaleniumConfiguration}.
   */
  @Parameter(property = "systemPropertyVariables")
  private Map<String, String> systemPropertyVariables;

  /**
   * Directory containing the Freemarker templates.
   */
  @Parameter(defaultValue = "${project.basedir}/src/main/resources/freemarker", property = "templateDir", required = true)
  private File templateDirectory;

  @Override
  public void execute() throws MojoExecutionException {

    if (!initPlugin()) {
      // if initialization does not work, plugin does not work
      throw new GaleniumException("Plugin initialization failed.");
    }

    try {

      // handle spec files and collect objects
      parseSpecs();

      // prepare Freemarker data model and process template
      generateCode();

    }
    finally {
      WebDriverManager.closeDriver();
    }
  }

  private boolean checkDirectory(File directory) {
    getLog().info("checking directory: " + directory.getPath());
    if (!directory.isDirectory()) {
      getLog().error("directory not found: " + directory.getPath());
      return false;
    }

    return true;
  }

  private boolean checkInputParams() {
    return checkDirectory(inputDirectory) && checkDirectory(templateDirectory);
  }

  private File getOutputFile(NestedSelector selector, SpecPojo spec) {
    String outputPackage = FormatUtil.getPackageName(packageRootName, spec);
    String className = FormatUtil.getClassName(selector);
    return FreemarkerUtil.getOutputFile(outputDirectory, outputPackage, className);
  }

  private Collection<NestedSelector> getSelectorsFromSpecFile(File specFile) {
    getLog().info("checking file: " + specFile.getPath());
    Collection<NestedSelector> selectors = ParsingUtil.getSelectorsFromSpec(specFile);
    if (getLog().isDebugEnabled()) {
      for (Selector selector : selectors) {
        getLog().debug("found: " + selector.elementName() + " - > '" + selector.asString() + "'");
      }
    }
    getLog().info("found " + selectors.size() + " selectors in '" + specFile.getPath() + "'");
    return selectors;
  }

  private File[] getSpecFiles() {
    File fromDir = inputDirectory;
    File[] specFiles = ParsingUtil.getSpecFiles(fromDir);
    getLog().debug("found " + specFiles.length + " spec files.");
    return specFiles;
  }

  protected void generateCode() {

    // same template for all specs
    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, rootTemplate);

    for (SpecPojo specPojo : FreemarkerUtil.getSpecsForDataModel(specSelectorMapping.entrySet())) {

      getLog().info("generating data models for '" + specPojo.getSpecFile().getPath() + "'");
      for (NestedSelector selector : specPojo.getRootSelectors()) {

        getLog().info("generating data model for '" + selector.elementName() + "'");
        Map<String, Object> dataModelForSelector = FreemarkerUtil.getDataModelForSelector(selector, specPojo);

        getLog().debug("processing template");
        FreemarkerUtil.process(template, dataModelForSelector, getOutputFile(selector, specPojo));
      }
    }
  }

  protected boolean initPlugin() {

    // transfer system properties
    ConfigurationUtil.addToSystemProperties(systemPropertyVariables);
    System.setProperty("packageRootName", packageRootName);

    // check input parameters
    if (!checkInputParams()) {
      return false;
    }

    return true;
  }

  protected void parseSpecs() {
    getLog().info("collecting spec files");
    File[] specFiles = getSpecFiles();

    getLog().info("processing spec files");
    for (File specFile : specFiles) {
      Collection<NestedSelector> selectorsFromSpecFile = getSelectorsFromSpecFile(specFile);
      storeSelectors(specFile, selectorsFromSpecFile);
    }
  }

  protected void storeSelectors(File specFile, Collection<NestedSelector> selectors) {
    getLog().debug("storing " + selectors.size() + " selector(s)");
    specSelectorMapping.put(specFile, selectors);
  }

}
