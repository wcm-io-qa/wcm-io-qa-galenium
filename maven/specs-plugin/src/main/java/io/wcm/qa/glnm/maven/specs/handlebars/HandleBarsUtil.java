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
import org.apache.commons.text.StringEscapeUtils;

import com.galenframework.specs.page.Locator;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.maven.specs.pojo.SelectorPojo;
import io.wcm.qa.glnm.maven.specs.pojo.SpecPojo;
import io.wcm.qa.glnm.maven.specs.util.FormatUtil;
import io.wcm.qa.glnm.selectors.base.Selector;

final class HandleBarsUtil {


  private HandleBarsUtil() {
    // do not instantiate
  }

  public static File getOutputFile(File rootDirectory, String packageName, String className) {
    File outputFile = new File(rootDirectory, getPackageAsPath(packageName) + "/"
        + className
        + ".java");
    return outputFile;
  }

  static String generateSourceCode(String templateName, Context context, Package resourcePackage) {
    Template template = compileTemplate(templateName, resourcePackage);

    return generateSourceCode(template, context);
  }

  static String generateSourceCode(Template template, Context context) {
    try {
      return template.apply(context);
    }
    catch (IOException ex) {
      throw new GaleniumException("When generating code", ex);
    }
  }

  static Template compileTemplate(String templateName, Package resourcePackage) {
    ClassPathTemplateLoader templateLoader = new ClassPathTemplateLoader(getPackageAsPath(resourcePackage), ".hbs");

    TemplateSource templateSource;
    try {
      templateSource = templateLoader.sourceAt(templateName);
    }
    catch (IOException ex) {
      throw new GaleniumException("When locating template", ex);
    }
    Handlebars handlebars = new Handlebars().with(EscapingStrategy.NOOP);
    handlebars.registerHelper("escapeJava", new Helper<String>() {

      @Override
      public Object apply(String arg0, Options arg1) throws IOException {
        return StringEscapeUtils.escapeJava(arg0);
      }
    });
    handlebars.registerHelper("elementName", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        return arg0.elementName();
      }

    });
    handlebars.registerHelper("cssSelector", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        return StringEscapeUtils.escapeJava(arg0.asString());
      }

    });
    handlebars.registerHelper("locatorIndex", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        return arg0.asRelative().asLocator().getIndex();
      }

    });
    handlebars.registerHelper("locatorValue", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        String locatorValue = arg0.asRelative().asLocator().getLocatorValue();
        return StringEscapeUtils.escapeJava(locatorValue);
      }

    });
    handlebars.registerHelper("locatorType", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        Selector asRelative = arg0.asRelative();
        Locator asLocator = asRelative.asLocator();
        return asLocator.getLocatorType();
      }

    });
    handlebars.registerHelper("className", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        return FormatUtil.getClassName(arg0);
      }

    });
    handlebars.registerHelper("specClassName", new Helper<SpecPojo>() {

      @Override
      public Object apply(SpecPojo arg0, Options arg1) throws IOException {
        return FormatUtil.getClassName(arg0);
      }

    });
    handlebars.registerHelper("stringClassName", new Helper<String>() {

      @Override
      public Object apply(String arg0, Options arg1) throws IOException {
        return FormatUtil.getClassName(arg0);
      }

    });
    handlebars.registerHelper("constantName", new Helper<SelectorPojo>() {

      @Override
      public Object apply(SelectorPojo arg0, Options arg1) throws IOException {
        return FormatUtil.getConstantName(arg0);
      }

    });
    handlebars.infiniteLoops(true);
    Template template;
    try {
      template = handlebars.compile(templateSource);
    }
    catch (IOException ex) {
      throw new GaleniumException("When compiling template", ex);
    }
    return template;
  }

  static void generateAndWriteSourceCode(
      String templateName,
      Context context,
      Package resourcePackage,
      File rootFolder,
      String packageName,
      String className) {
    try {
      String generatedCode = generateSourceCode(templateName, context, resourcePackage);
      File outputFile = getOutputFile(rootFolder, packageName, className);
      FileUtils.write(outputFile, generatedCode, StandardCharsets.UTF_8);
    }
    catch (IOException ex) {
      throw new GaleniumException("When handling bars.", ex);
    }
  }

  static String getPackageAsPath(Package packageName) {
    return getPackageAsPath(packageName.getName());
  }

  static String getPackageAsPath(String name) {
    return "/" + name.replace('.', '/');
  }


}
