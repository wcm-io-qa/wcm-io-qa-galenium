package io.wcm.qa.galenium.maven;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

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

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GalenHelperUtil;

/**
 * Goal which finds Galen specs, extracts objects and generates Java code from it.
 */
@Mojo(name = "specs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GalenSpecsMojo extends AbstractMojo {

  private static final GalenSpecFileFilter GALEN_SPEC_FILE_FILTER = new GalenSpecFileFilter();

  /**
   * Location of generated output.
   */
  @Parameter(defaultValue = "${project.basedir}/src/test/resources/galen/specs", property = "inputDir", required = true)
  private File inputDirectory;

  /**
   * Location of generated output.
   */
  @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
  private File outputDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("Hello Galen Specs!");

    if (!inputDirectory.isDirectory()) {
      getLog().error("directory not found: " + inputDirectory.getPath());
      return;
    }
    getLog().info("checking directory: " + inputDirectory.getPath());

    File[] specFiles = inputDirectory.listFiles(GALEN_SPEC_FILE_FILTER);
    for (File specFile : specFiles) {
      getLog().info("checking file: " + specFile.getPath());
      String specPath = specFile.getPath();
      PageSpec galenSpec = GalenHelperUtil.readSpec(specPath);
      Collection<Selector> selectors = GalenHelperUtil.getObjects(galenSpec);
      for (Selector selector : selectors) {
        getLog().info("found: " + selector.elementName() + " - > '" + selector.asString() + "'");
      }
    }
  }

  private static final class GalenSpecFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(".gspec");
    }
  }
}
