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
import java.util.ArrayList;
import java.util.Collection;
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
import org.codehaus.plexus.util.DirectoryScanner;

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

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  /**
   * Name of Freemarker template to use for generating top level classes.
   */
  @Parameter(defaultValue = "interactive-selector.ftlh", property = "interactiveSelectorTemplate")
  private String interactiveSelectorTemplate;

  /**
   * Root directory for generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/java", property = "outputDir", required = true)
  private File outputDirectory;

  /**
   * Package name to generate {@link Selector} code into.
   */
  @Parameter(defaultValue = "io.wcm.qa.galenium.selectors", property = "packagePrefixSelectors", required = true)
  private String packagePrefixSelectors;

  /**
   * Package name to generate {@link Selector} code into.
   */
  @Parameter(defaultValue = "io.wcm.qa.galenium.specs", property = "packagePrefixSpecs", required = true)
  private String packagePrefixSpecs;

  /**
   * Name of Freemarker template to use for generating top level classes.
   */
  @Parameter(defaultValue = "selector.ftlh", property = "selectorTemplate")
  private String selectorTemplate;

  /**
   * A set of file patterns to include in the zip.
   */
  @Parameter(property = "includes")
  private String[] includes;

  /**
   * A set of file patterns to exclude from the zip.
   * @parameter alias="excludes"
   */
  @Parameter(property = "excludes")
  private String[] excludes;

  private Collection<SpecPojo> specs = new ArrayList<>();

  /**
   * Name of Freemarker template to use for recursively generating static inner classes.
   */
  @Parameter(defaultValue = "spec.ftlh", property = "specTemplate")
  private String specTemplate;

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

    // handle spec files and collect objects
    parseSpecs();

    // prepare Freemarker data model and process template
    generateCode();
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
    return checkDirectory(ParsingUtil.getSpecRootDirectory()) && checkDirectory(templateDirectory);
  }

  private void generateSpecCode() {
    // same template for all specs
    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, specTemplate);

    for (SpecPojo specPojo : specs) {

      getLog().info("generating data model for '" + specPojo.getSpecFile().getPath() + "'");
      Map<String, Object> dataModelForSpec = FreemarkerUtil.getDataModelForSpec(specPojo, packagePrefixSpecs);

      getLog().debug("processing template");
      FreemarkerUtil.process(template, dataModelForSpec, getOutputFile(specPojo));

    }
  }

  private String getInteractiveSelectorClassName() {
    return FormatUtil.getClassName(new File(interactiveSelectorTemplate));
  }

  private String getInteractiveSelectorPackageName() {
    return packagePrefixSelectors;
  }

  private File getOutputFile(NestedSelector selector, SpecPojo spec) {
    String outputPackage = FormatUtil.getSelectorsPackageName(packagePrefixSelectors, spec);
    String className = FormatUtil.getClassName(selector);
    return FreemarkerUtil.getOutputFile(outputDirectory, outputPackage, className);
  }

  private File getOutputFile(SpecPojo spec) {
    String outputPackage = packagePrefixSpecs;
    String className = FormatUtil.getClassName(spec);
    return FreemarkerUtil.getOutputFile(outputDirectory, outputPackage, className);
  }

  private Collection<File> getSpecFiles() {
    DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setIncludes(includes);
    directoryScanner.setExcludes(excludes);
    String galenSpecPath = GaleniumConfiguration.getGalenSpecPath();
    directoryScanner.setBasedir(galenSpecPath);
    Collection<File> specFiles = new ArrayList<>();
    String[] includedFiles = directoryScanner.getIncludedFiles();
    for (String relativeFilePath : includedFiles) {
      specFiles.add(new File(galenSpecPath, relativeFilePath));
    }
    getLog().debug("found " + specFiles.size() + " spec files.");
    return specFiles;
  }

  private void storeSpec(SpecPojo specPojo) {
    specs.add(specPojo);
  }

  protected void generateCode() {
    generateInteractiveSelectorCode();
    generateSelectorCode();
    generateSpecCode();
  }

  protected void generateInteractiveSelectorCode() {
    Template template = FreemarkerUtil.getTemplate(templateDirectory, interactiveSelectorTemplate);
    String className = getInteractiveSelectorClassName();
    String packageName = getInteractiveSelectorPackageName();
    Map<String, Object> model = FreemarkerUtil.getDataModelForInteractiveSelector(packageName, className);
    File outputFile = FreemarkerUtil.getOutputFile(outputDirectory, packageName, className);
    FreemarkerUtil.process(template, model, outputFile);
  }

  protected void generateSelectorCode() {
    // same template for all selectors
    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, selectorTemplate);

    for (SpecPojo specPojo : specs) {

      getLog().info("generating data models for '" + specPojo.getSpecFile().getPath() + "'");
      for (NestedSelector selector : specPojo.getRootSelectors()) {

        getLog().info("generating data model for '" + selector.elementName() + "'");
        Map<String, Object> dataModelForSelector = FreemarkerUtil.getDataModelForSelector(
            selector,
            specPojo,
            getInteractiveSelectorPackageName(),
            getInteractiveSelectorClassName());

        getLog().debug("processing template");
        FreemarkerUtil.process(template, dataModelForSelector, getOutputFile(selector, specPojo));
      }
    }
  }

  protected boolean initPlugin() {

    // transfer system properties
    ConfigurationUtil.addToSystemProperties(systemPropertyVariables);
    System.setProperty("packageRootName", packagePrefixSelectors);
    System.setProperty("galenium.specPath", ParsingUtil.getSpecRootDirectory().getPath());

    // check input parameters
    if (!checkInputParams()) {
      return false;
    }

    return true;
  }

  protected void parseSpecs() {
    getLog().info("collecting spec files");
    Collection<File> specFiles = getSpecFiles();

    getLog().info("processing spec files");
    for (File specFile : specFiles) {
      storeSpec(new SpecPojo(specFile));
    }
  }

}
