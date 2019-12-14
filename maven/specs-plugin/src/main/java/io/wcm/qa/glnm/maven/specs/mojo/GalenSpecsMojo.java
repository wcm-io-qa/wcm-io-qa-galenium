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
package io.wcm.qa.glnm.maven.specs.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.ConfigurationUtil;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.maven.specs.handlebars.CodeGenerator;
import io.wcm.qa.glnm.maven.specs.handlebars.CodeWriter;
import io.wcm.qa.glnm.maven.specs.handlebars.InteractiveSelectorBaseGenerator;
import io.wcm.qa.glnm.maven.specs.handlebars.InteractiveSelectorGenerator;
import io.wcm.qa.glnm.maven.specs.handlebars.SelectorCodeGenerator;
import io.wcm.qa.glnm.maven.specs.handlebars.SpecCodeGenerator;
import io.wcm.qa.glnm.maven.specs.pojo.SelectorPojo;
import io.wcm.qa.glnm.maven.specs.pojo.SpecPojo;
import io.wcm.qa.glnm.maven.specs.util.FormatUtil;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 * @since 1.0.0
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  private static final Logger LOG = LoggerFactory.getLogger(GalenSpecsMojo.class);

  /**
   * Root directory for generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/target/specs", property = "inputDir", required = true)
  private File inputDirectory;

  /**
   * Root directory for generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/java", property = "outputDir", required = true)
  private File outputDirectory;

  /**
   * Package name to generate {@link Selector} code into.
   */
  @Parameter(defaultValue = "io.wcm.qa.glnm.selectors", property = "packageRoot", required = true)
  private String packageRoot;

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

  /**
   * To be transferred to system properties when running plugin. Mostly used to manipulate
   * {@link GaleniumConfiguration}.
   */
  @Parameter(property = "systemPropertyVariables")
  private Map<String, String> systemPropertyVariables;

  /** {@inheritDoc} */
  @Override
  public void execute() throws MojoExecutionException {

    if (!initPlugin()) {
      // if initialization does not work, plugin does not work
      throw new GaleniumException("Plugin initialization failed.");
    }

    // prepare Freemarker data model and process template
    generateCode();
  }

  private void generateCode() {
    generateInteractiveSelectorInterfaceCode();
    generateInteractiveSelectorBaseCode();
    generateSelectorCode();
    //    generateWebElementCode();
    generateSpecCode();
  }

  private void generateInteractiveSelectorBaseCode() {
    CodeGenerator generator = new InteractiveSelectorBaseGenerator();
    generator.setPackageName(packageRoot);
    writeSourceCode(
        generator,
        "InteractiveSelectorBase");
  }

  private void generateInteractiveSelectorInterfaceCode() {
    InteractiveSelectorGenerator generator = new InteractiveSelectorGenerator();
    generator.setPackageName(packageRoot);
    writeSourceCode(
        generator,
        "InteractiveSelector");
  }

  private void generateSelectorCode() {
    for (File specFile : getSpecFiles()) {
      LOG.debug("writing selector classes for : " + specFile);
      SpecPojo spec = new SpecPojo(specFile);
      for (SelectorPojo selector : spec.getRootSelectors()) {
        LOG.debug("writing selector class for : " + selector.elementName());
        generateSelectorCode(selector);
      }
    }
  }

  private void generateSelectorCode(SelectorPojo selector) {
    SelectorCodeGenerator generator = new SelectorCodeGenerator(selector);
    generator.setPackageName(packageRoot);
    String selectorsPackageName = FormatUtil.getSelectorsPackageName(packageRoot, selector.getSpec());
    writeSourceCode(generator, selectorsPackageName, FormatUtil.getClassName(selector));
  }

  private void generateSpecCode() {
    for (File file : getSpecFiles()) {
      SpecPojo spec = new SpecPojo(file);
      String packageName = packageRoot + ".specs";
      String className = FormatUtil.getClassName(spec);
      SpecCodeGenerator generator = new SpecCodeGenerator(spec);
      generator.setPackageName(packageName);
      writeSourceCode(generator, packageName, className);
    }
  }

  private void generateWebElementCode() {
    throw new GaleniumException("not implemented: private void generateWebElementCode()");
  }

  private CodeWriter getCodeWriter(String generatedCode, String packageName, String className) {
    CodeWriter codeWriter = new CodeWriter();
    codeWriter.setClassName(className);
    codeWriter.setPackageName(packageName);
    codeWriter.setRootFolder(outputDirectory);
    codeWriter.setSourceCode(generatedCode);
    return codeWriter;
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

  private String[] getIncludedFiles() {
    return getIncludedFiles(inputDirectory, specIncludes, specExcludes);
  }

  private Collection<File> getSpecFiles(String... includedFiles) {
    Collection<File> specFiles = new ArrayList<>();
    for (String relativeFilePath : includedFiles) {
      specFiles.add(new File(inputDirectory, relativeFilePath));
    }
    getLog().debug("found " + specFiles.size() + " spec files.");
    return specFiles;
  }

  private Collection<File> getSpecFiles() {
    return getSpecFiles(getIncludedFiles());
  }

  private boolean initPlugin() {

    // transfer system properties
    ConfigurationUtil.addToSystemProperties(systemPropertyVariables);
    System.setProperty("packageRootName", packageRoot);
    System.setProperty("galenium.specPath", inputDirectory.getPath());

    return true;
  }

  private void writeSourceCode(CodeGenerator generator, String className) {
    writeSourceCode(generator, packageRoot, className);
  }

  private void writeSourceCode(CodeGenerator generator, String packageName, String className) {
    getCodeWriter(generator.generate(), packageName, className).write();
  }

}
