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
import io.wcm.qa.galenium.configuration.ConfigurationUtil;
import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.maven.freemarker.pojo.SpecPojo;
import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.maven.freemarker.util.FreemarkerUtil;
import io.wcm.qa.galenium.selectors.base.NestedSelector;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  /**
   * Root directory for generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/target/specs", property = "inputDir", required = true)
  private File inputDirectory;

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
   * A set of file patterns to exclude from selector generation.
   */
  @Parameter(property = "selectorExcludes")
  private String[] selectorExcludes;

  /**
   * A set of file patterns to include in selector generation.
   */
  @Parameter(property = "selectorIncludes")
  private String[] selectorIncludes;

  /**
   * Name of Freemarker template to use for generating top level classes.
   */
  @Parameter(defaultValue = "selector.ftlh", property = "selectorTemplate")
  private String selectorTemplate;

  /**
   * A set of file patterns to exclude from spec generation.
   */
  @Parameter(property = "specExcludes")
  private String[] specExcludes;

  /**
   * A set of file patterns to include in spec generation.
   */
  @Parameter(property = "specIncludes")
  private String[] specIncludes;

  private Collection<SpecPojo> specsForSelectors = new ArrayList<>();
  private Collection<SpecPojo> specsForSpecs = new ArrayList<>();

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
    return checkDirectory(inputDirectory) && checkDirectory(templateDirectory);
  }

  private void generateCode() {
    generateInteractiveSelectorCode();
    generateSelectorCode();
    generateSpecCode();
  }

  private void generateInteractiveSelectorCode() {
    Template template = FreemarkerUtil.getTemplate(templateDirectory, interactiveSelectorTemplate);
    String className = getInteractiveSelectorClassName();
    String packageName = getInteractiveSelectorPackageName();
    Map<String, Object> model = FreemarkerUtil.getDataModelForInteractiveSelector(packageName, className);
    File outputFile = FreemarkerUtil.getOutputFile(outputDirectory, packageName, className);
    FreemarkerUtil.process(template, model, outputFile);
  }

  private void generateSpecCode() {
    // same template for all specs
    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, specTemplate);

    for (SpecPojo specPojo : specsForSpecs) {

      getLog().info("generating data model for '" + specPojo.getSpecFile().getPath() + "'");
      Map<String, Object> dataModelForSpec = FreemarkerUtil.getDataModelForSpec(specPojo, packagePrefixSpecs);

      getLog().debug("processing template");
      FreemarkerUtil.process(template, dataModelForSpec, getOutputFile(specPojo));

    }
  }

  private String[] getIncludedFiles(File inputFolder, String[] includes, String[] excludes) {
    DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setIncludes(includes);
    directoryScanner.setExcludes(excludes);
    directoryScanner.setBasedir(inputFolder);
    directoryScanner.scan();
    String[] includedFiles = directoryScanner.getIncludedFiles();
    return includedFiles;
  }

  private String[] getIncludedFilesForSelectors() {
    return getIncludedFiles(inputDirectory, selectorIncludes, selectorExcludes);
  }

  private String[] getIncludedFilesForSpecs() {
    return getIncludedFiles(inputDirectory, specIncludes, specExcludes);
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

  private Collection<File> getSpecFiles(String[] includedFiles) {
    Collection<File> specFiles = new ArrayList<>();
    for (String relativeFilePath : includedFiles) {
      specFiles.add(new File(inputDirectory, relativeFilePath));
    }
    getLog().debug("found " + specFiles.size() + " spec files.");
    return specFiles;
  }

  private Collection<File> getSpecFilesForSelectors() {
    return getSpecFiles(getIncludedFilesForSelectors());
  }

  private Collection<File> getSpecFilesForSpecs() {
    return getSpecFiles(getIncludedFilesForSpecs());
  }

  private void storeSpecForSelector(SpecPojo specPojo) {
    specsForSelectors.add(specPojo);
  }

  private void storeSpecForSpec(SpecPojo specPojo) {
    specsForSpecs.add(specPojo);
  }

  protected void generateSelectorCode() {
    // same template for all selectors
    getLog().info("fetching template");
    Template template = FreemarkerUtil.getTemplate(templateDirectory, selectorTemplate);

    for (SpecPojo specPojo : specsForSelectors) {

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
    System.setProperty("galenium.specPath", inputDirectory.getPath());

    // check input parameters
    if (!checkInputParams()) {
      return false;
    }

    return true;
  }

  protected void parseSpecs() {
    getLog().info("processing spec files for selectors");
    for (File specFile : getSpecFilesForSelectors()) {
      storeSpecForSelector(new SpecPojo(specFile));
    }
    getLog().info("processing spec files for specs");
    for (File specFile : getSpecFilesForSpecs()) {
      storeSpecForSpec(new SpecPojo(specFile));
    }
  }

}
