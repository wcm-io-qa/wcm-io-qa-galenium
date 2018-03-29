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
package io.wcm.qa.galenium.maven.freemarker.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.maven.freemarker.pojo.SpecPojo;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

public final class FreemarkerUtil {

  private static final Configuration CONFIGURATION = generateConfiguration();

  private FreemarkerUtil() {
    // do not instantiate
  }

  public static Configuration generateConfiguration() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(getExceptionHandler());
    return cfg;
  }

  public static Map<String, Object> getDataModelForSpecs(List<SpecPojo> specs, String packageName, String className) {
    Map<String, Object> root = new HashMap<>();
    root.put("specs", specs);
    root.put("packageName", packageName);
    root.put("className", className);
    return root;
  }

  public static TemplateExceptionHandler getExceptionHandler() {
    if (GaleniumConfiguration.isSparseReporting()) {
      return TemplateExceptionHandler.RETHROW_HANDLER;
    }
    else {
      return TemplateExceptionHandler.DEBUG_HANDLER;
    }
  }

  public static File getOutputFile(File outputDir, String outputPackage, String outputClassName) {
    File outputDirectoryPackage = new File(outputDir, outputPackage.replaceAll("\\.", "/"));
    outputDirectoryPackage.mkdirs();
    File outputFile = new File(outputDirectoryPackage, outputClassName + ".java");
    return outputFile;
  }

  public static List<SpecPojo> getSpecsForDataModel(Set<Entry<File, Collection<Selector>>> specFileToSelectorMapping) {
    List<SpecPojo> specs = new ArrayList<>();
    for (Entry<File, Collection<Selector>> entry : specFileToSelectorMapping) {
      File spec = entry.getKey();
      Collection<Selector> selectors = entry.getValue();
      GaleniumReportUtil.getLogger().debug("adding " + selectors.size() + " selectors from spec '" + spec.getPath() + "'");
      specs.add(new SpecPojo(spec, selectors));
    }
    return specs;
  }

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

  public static void process(Template template, Map<String, Object> dataModel, File outputFile) {
    FileWriter out = null;
    try {
      out = new FileWriter(outputFile);
      GaleniumReportUtil.getLogger().debug("generating '" + outputFile.getPath() + "'");
      template.process(dataModel, out);
      GaleniumReportUtil.getLogger().info("generated '" + outputFile.getPath() + "'");
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


}
