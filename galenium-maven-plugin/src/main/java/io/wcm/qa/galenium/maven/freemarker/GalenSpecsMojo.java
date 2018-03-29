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
import java.util.List;
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
import io.wcm.qa.galenium.maven.freemarker.util.FreemarkerUtil;
import io.wcm.qa.galenium.maven.freemarker.util.ParsingUtil;
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
   * Class name for generated code. When generating multiple classes in one run, this is used as a prefix for the class
   * name.
   */
  @Parameter(defaultValue = "Selectors", property = "className", required = true)
  private String className;

  /**
   * Whether to generate a dedicated class per spec file.
   */
  @Parameter(defaultValue = "true", property = "classPerSpec", required = true)
  private boolean classPerSpec;

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
  @Parameter(defaultValue = "io.wcm.qa.galenium.selectors", property = "packageName", required = true)
  private String packageName;

  private Map<File, Collection<Selector>> specSelectorMapping = new HashMap<>();

  /**
   * To be transferred to system properties when running plugin. Mostly used to manipulate
   * {@link GaleniumConfiguration}.
   */
  @Parameter(property = "systemPropertyVariables")
  private Map<String, String> systemPropertyVariables;

  /**
   * Directory containing the Freemarker template.
   */
  @Parameter(defaultValue = "${project.basedir}/src/test/resources/freemarker", property = "templateDir", required = true)
  private File templateDirectory;

  /**
   * Name of Freemarker template to use in code generation.
   */
  @Parameter(defaultValue = "selectors_from_galen.ftlh", property = "templateName")
  private String templateName;

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

  private File getOutputFile() {
    return FreemarkerUtil.getOutputFile(outputDirectory, packageName, className);
  }

  private Collection<Selector> getSelectorsFromSpecFile(File specFile) {
    getLog().info("checking file: " + specFile.getPath());
    Collection<Selector> selectors = ParsingUtil.getSelectorsFromSpec(specFile);
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
    getLog().info("generating data model");
    List<SpecPojo> specsForDataModel = FreemarkerUtil.getSpecsForDataModel(specSelectorMapping.entrySet());
    Map<String, Object> dataModelForSpecs = FreemarkerUtil.getDataModelForSpecs(specsForDataModel, packageName, className);

    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, templateName);

    getLog().info("processing template");
    FreemarkerUtil.process(template, dataModelForSpecs, getOutputFile());
  }

  protected boolean initPlugin() {

    // transfer system properties
    ConfigurationUtil.addToSystemProperties(systemPropertyVariables);

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
      Collection<Selector> selectorsFromSpecFile = getSelectorsFromSpecFile(specFile);
      storeSelectors(specFile, selectorsFromSpecFile);
    }
  }

  protected void storeSelectors(File specFile, Collection<Selector> selectors) {
    getLog().debug("storing " + selectors.size() + " selector(s)");
    specSelectorMapping.put(specFile, selectors);
  }

}
