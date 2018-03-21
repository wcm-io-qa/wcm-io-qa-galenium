package io.wcm.qa.galenium.maven.freemarker;

import static java.util.Collections.emptyList;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.openqa.selenium.Dimension;

import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import io.wcm.qa.galenium.maven.freemarker.datamodel.SpecPojo;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.BrowserType;
import io.wcm.qa.galenium.util.GalenHelperUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  private static final Configuration CONFIGURATION = generateConfiguration();
  private static final String FILE_EXTENSION_JAVA = ".java";
  private static final GalenSpecFileFilter GALEN_SPEC_FILE_FILTER = new GalenSpecFileFilter();
  private static final TestDevice TEST_DEVICE = new TestDevice("galen-specs", BrowserType.CHROME, new Dimension(1000, 1000), emptyList(), null);

  @Parameter(defaultValue = "Selectors", property = "className", required = true)
  private String className;

  /**
   * Location of input Galen specs.
   */
  @Parameter(defaultValue = "${project.basedir}/src/test/resources/galen/specs", property = "inputDir", required = true)
  private File inputDirectory;

  /**
   * Location of generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources", property = "outputDir", required = true)
  private File outputDirectory;

  @Parameter(defaultValue = "io.wcm.qa.galenium.selectors", property = "packageName", required = true)
  private String packageName;

  private Map<File, Collection<Selector>> specSelectorMapping = new HashMap<>();

  @Parameter(property = "systemPropertyVariables")
  private Map<String, String> systemPropertyVariables;

  @Parameter(defaultValue = "${project.basedir}/src/test/resources/freemarker", property = "templateDir", required = true)
  private File templateDirectory;

  @Parameter(defaultValue = "selectors_from_galen.ftlh", property = "templateName")
  private String templateName;

  @Override
  public void execute() throws MojoExecutionException {

    // check directory
    if (!inputDirectory.isDirectory()) {
      getLog().error("directory not found: " + inputDirectory.getPath());
      return;
    }
    getLog().info("checking directory: " + inputDirectory.getPath());

    // transfer system properties
    initSysProps();

    try {
      // handle spec files
      parseSpecFiles();

      // prepare Freemarker data model
      prepareFreemarker();
    }
    finally {
      WebDriverManager.closeDriver();
    }
  }

  private File getOutputFile() {
    File outputDirectoryPackage = new File(outputDirectory, packageName.replaceAll("\\.", "/"));
    outputDirectoryPackage.mkdirs();
    File outputFile = new File(outputDirectoryPackage, className + FILE_EXTENSION_JAVA);
    return outputFile;
  }

  private Collection<Selector> getSelectorsFromSpec(PageSpec galenSpec) {
    Collection<Selector> selectors = GalenHelperUtil.getObjects(galenSpec);
    if (getLog().isDebugEnabled()) {
      for (Selector selector : selectors) {
        getLog().debug("found: " + selector.elementName() + " - > '" + selector.asString() + "'");
      }
    }
    return selectors;
  }

  private Collection<Selector> getSelectorsFromSpecFile(File specFile) {
    getLog().info("checking file: " + specFile.getPath());
    String specPath = specFile.getPath();
    PageSpec galenSpec = readSpec(specPath);
    Collection<Selector> selectors = getSelectorsFromSpec(galenSpec);
    getLog().debug("found " + selectors.size() + " selectors in '" + specPath + "'");
    return selectors;
  }

  private File[] getSpecFiles() {
    File[] specFiles = inputDirectory.listFiles(GALEN_SPEC_FILE_FILTER);
    getLog().debug("found " + specFiles.length + " spec files.");
    return specFiles;
  }

  private Set<Entry<File, Collection<Selector>>> getSpecFileToSelectorMapping() {
    return specSelectorMapping.entrySet();
  }

  private List<SpecPojo> getSpecsForDataModel() {
    List<SpecPojo> specs = new ArrayList<>();
    for (Entry<File, Collection<Selector>> entry : getSpecFileToSelectorMapping()) {
      File spec = entry.getKey();
      Collection<Selector> selectors = entry.getValue();
      getLog().debug("adding " + selectors.size() + " selectors from spec '" + spec.getPath() + "'");
      specs.add(new SpecPojo(spec, selectors));
    }
    return specs;
  }

  private Template getTemplate() throws IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException {
    CONFIGURATION.setDirectoryForTemplateLoading(templateDirectory);
    Template template = CONFIGURATION.getTemplate(templateName);
    return template;
  }

  private void initSysProps() {
    Set<Entry<String, String>> entrySet = systemPropertyVariables.entrySet();
    for (Entry<String, String> entry : entrySet) {
      if (entry != null) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (key == null || value == null) {
          getLog().info("skipping entry: " + key + "->" + value);
        }
        else {
          getLog().info("adding entry: " + key + "->" + value);
          System.setProperty(key, value);
        }
      }
      else {
        getLog().warn("entry for system property was null.");
      }
    }
  }

  private void parseSpecFiles() {
    for (File specFile : getSpecFiles()) {
      Collection<Selector> selectors = getSelectorsFromSpecFile(specFile);
      getLog().debug("found " + selectors.size());
      storeSelectors(specFile, selectors);
    }
  }

  private Map<String, Object> prepareDataModel() {
    getLog().debug("generating data model");
    Map<String, Object> root = new HashMap<>();
    root.put("specs", getSpecsForDataModel());
    root.put("packageName", packageName);
    root.put("className", className);
    return root;
  }

  private void prepareFreemarker() {
    try {
      File outputFile = getOutputFile();
      Template template = getTemplate();
      getLog().debug("generating '" + outputFile.getPath() + "'");
      template.process(prepareDataModel(), new FileWriter(outputFile));
      getLog().info("generated '" + outputFile.getPath() + "'");
    }
    catch (IOException | TemplateException ex) {
      throw new RuntimeException("template exception", ex);
    }
  }

  private PageSpec readSpec(String specPath) {
    return GalenHelperUtil.readSpec(TEST_DEVICE, specPath, new SectionFilter(emptyList(), emptyList()));
  }

  private void storeSelectors(File specFile, Collection<Selector> selectors) {
    specSelectorMapping.put(specFile, selectors);
  }

  private static Configuration generateConfiguration() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(getExceptionHandler());
    return cfg;
  }

  private static TemplateExceptionHandler getExceptionHandler() {
    if (GaleniumConfiguration.isSparseReporting()) {
      return TemplateExceptionHandler.RETHROW_HANDLER;
    }
    else {
      return TemplateExceptionHandler.DEBUG_HANDLER;
    }
  }

  private static final class GalenSpecFileFilter implements FilenameFilter {

    private static final String FILE_EXTENSION_GSPEC = ".gspec";

    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(FILE_EXTENSION_GSPEC);
    }
  }
}
