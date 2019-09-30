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
package io.wcm.qa.glnm.aem;

import java.net.MalformedURLException;
import java.net.URL;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Encapsulates logic to directly talk to components.
 */
public class AemComponentUrlBuilder {


  private static final String NO_CONTENT_PATH_CONFIGURED = "CONTENT_PATH_FROM_AEM__COMPONENT_URL_BUILDER";
  private static final String NO_COMPONENT_NAME_CONFIGURED = "COMPONENT_NAME_FROM_AEM__COMPONENT_URL_BUILDER";

  private String protocol = "http";
  private String host = "localhost";
  private int port = 4502;
  private String componentName = NO_COMPONENT_NAME_CONFIGURED;
  private String contentPath = NO_CONTENT_PATH_CONFIGURED;
  private boolean authorInstance = true;
  private String extension = "html";


  AemComponentUrlBuilder() {
  }

  /**
   * @return configured URL
   */
  public URL build() {
    try {
      return new URL(getProtocol(), getHost(), getPort(), getFile());
    }
    catch (MalformedURLException ex) {
      StringBuilder stringBuilder = new StringBuilder()
          .append("could not construct URL: [protocol: '")
          .append(getProtocol())
          .append("host: '")
          .append(getHost())
          .append("', port: ")
          .append(getPort())
          .append("file: '")
          .append(getFile())
          .append("']");
      throw new GaleniumException(stringBuilder.toString());
    }
  }

  /**
   * @param isAuthor whether SUT is an author instance
   * @return this
   */
  public AemComponentUrlBuilder setAuthorInstance(boolean isAuthor) {
    this.authorInstance = isAuthor;
    return this;
  }

  /**
   * @param name of component to address
   * @return this
   */
  public AemComponentUrlBuilder setComponentName(String name) {
    this.componentName = name;
    return this;
  }

  /**
   * @param path to page
   * @return this
   */
  public AemComponentUrlBuilder setContentPath(String path) {
    this.contentPath = path;
    return this;
  }

  /**
   * Default 'html'.
   * @param renderFormat defines rendering format for component
   * @return this
   */
  public AemComponentUrlBuilder setExtension(String renderFormat) {
    this.extension = renderFormat;
    return this;
  }

  /**
   * Default 'localhost'.
   * @param hostName of AEM instance
   * @return this
   */
  public AemComponentUrlBuilder setHost(String hostName) {
    this.host = hostName;
    return this;
  }

  /**
   * Default 4502.
   * @param aemPort of AEM instance, set to -1 to omit port
   * @return this
   */
  public AemComponentUrlBuilder setPort(int aemPort) {
    this.port = aemPort;
    return this;
  }

  /**
   * Default 'http'.

   * @param networkProtocol to use for fetching
   * @return this
   */
  public AemComponentUrlBuilder setProtocol(String networkProtocol) {
    this.protocol = networkProtocol;
    return this;
  }

  private String getFile() {
    StringBuilder builder = new StringBuilder()
        .append(getContentPath())
        .append("/jcr:content/")
        .append(getComponentName())
        .append(".")
        .append(getExtension());
    if (isAuthorInstance()) {
      builder.append("?wcmmode=disabled");
    }
    return builder.toString();
  }

  protected String getComponentName() {
    return componentName;
  }

  protected String getContentPath() {
    return contentPath;
  }

  protected String getExtension() {
    return extension;
  }

  protected String getHost() {
    return host;
  }

  protected int getPort() {
    return port;
  }

  protected String getProtocol() {
    return protocol;
  }

  protected boolean isAuthorInstance() {
    return authorInstance;
  }

}
