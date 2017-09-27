/*
 * Copyright 2015 Movilizer GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerWebServiceV15;
import com.movilitas.movilizer.v15.MovilizerWebServiceV15Service;
import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * This class is used to set the configuration of interest for the Movilizer webservice and then return an instance
 * configured. Do not instance it by yourself, use the {@code Movilizer.buildConf()} static factory method.
 * <p/>
 * Intended use would be as the following:
 * <pre>
 * {@code
 * MovilizerDistributionService mds = Movilizer.buildConf()
 *                                          .setEndpoint(EndPoint.PROD)
 *                                          .setOutputEncoding(Charset.defaultCharset())
 *                                          .getService();
 * </pre>
 *
 * @author Jesús de Mula Cano
 * @see MovilizerDistributionService
 * @since 12.11.1.0
 */
public class MovilizerConfBuilder {
  private static final Logger logger = LoggerFactory.getLogger(MovilizerConfBuilder.class);
  private Charset outputEncoding = DefaultValues.OUTPUT_ENCODING;
  private EndPoint endpoint = DefaultValues.MOVILIZER_ENDPOINT;
  private URL endpointAddress;
  private URL documentUploadAddress;
  private Integer defaultConnectionTimeoutInMillis = DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS;
  private Integer defaultReceiveTimeoutInMillis = DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS;
  private String agentId = DefaultValues.AGENT_ID;
  private String agentVersion = DefaultValues.AGENT_VERSION;
  private boolean threadSafe = false;

  /**
   * <b>DO NOT USE</b>. Use instead Movilizer.buildConf() or Movilizer.getService()
   * @see com.movilizer.mds.webservice.Movilizer
   */
  public MovilizerConfBuilder() {
    // All defaults already set in the fields declaration of the class.
  }

  /**
   * Get a <tt>MovilizerDistributionService</tt> instance with the configuration of the builder.
   *
   * @return the <tt>MovilizerDistributionService</tt> instance configured.
   * @see MovilizerDistributionService
   * @since 12.11.1.0
   */
  public MovilizerDistributionService getService() {
    if (logger.isTraceEnabled()) {
      logger.trace(MESSAGES.BUILDING_CONFIG);
    }
    MovilizerWebServiceV15 movilizerCloud = new MovilizerWebServiceV15Service().getMovilizerWebServiceV15Soap11();
    MovilizerWebService webService = new MovilizerWebService(movilizerCloud, defaultConnectionTimeoutInMillis, defaultReceiveTimeoutInMillis, agentId, agentVersion);
    webService.setEndpoint(endpoint.getMdsUrl());
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.USING_ENCODING, outputEncoding.displayName()));
    }
    MovilizerXMLParserService parserService;
    if(this.threadSafe) {
      parserService = new MovilizerXMLThreadSafeParserServiceImpl(outputEncoding);
    } else {
      parserService = new MovilizerXMLParserServiceImpl(outputEncoding);
    }

    UploadFileService uploadFileService = new UploadFileService(endpoint.getUploadUrl(), new MovilizerUploadForm(), defaultConnectionTimeoutInMillis);

    if (endpointAddress != null && documentUploadAddress != null) {
      if (logger.isTraceEnabled()) {
        logger.trace(String.format(MESSAGES.USING_PRIVATE_CONFIG, endpointAddress.toString(), documentUploadAddress.toString()));
      }
      webService.setEndpoint(endpointAddress);
      uploadFileService.setDocumentUploadAddress(documentUploadAddress);
    } else {
      if (logger.isTraceEnabled()) {
        logger.trace(String.format(MESSAGES.USING_PUBLIC_CONFIG, endpoint.name()));
      }
    }

    FolderLoaderService loaderService = new FolderLoaderService(parserService);

    return new MovilizerDistributionServiceImpl(
        webService,
        parserService,
        uploadFileService,
        loaderService
    );
  }

  /**
   * Endpoint to be used in the <tt>MovilizerDistributionService</tt> instance.
   *
   * @param endpoint Endpoint to be used in the <tt>MovilizerDistributionService</tt> instance.
   * @return this to be able to chain calls in a fluid API way.
   * @see EndPoint
   * @since 12.11.1.0
   */
  public MovilizerConfBuilder setEndpoint(EndPoint endpoint) {
    this.endpoint = endpoint;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_ENDPOINT, endpoint.name()));
    }
    return this;
  }

  /**
   * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
   *
   * @param endpointAddress       the URL of the web service endpoint.
   * @param documentUploadAddress the URL of to upload documents to the cloud.
   * @return this to be able to chain calls in a fluid API way.
   * @see EndPoint
   * @since 12.11.1.0
   */
  public MovilizerConfBuilder setEndpoint(URL endpointAddress, URL documentUploadAddress) {
    this.endpointAddress = endpointAddress;
    this.documentUploadAddress = documentUploadAddress;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_PRIVATE_ENDPOINT, endpointAddress.toString(), documentUploadAddress.toString()));
    }
    return this;
  }

  /**
   * Connection timeout to be used in the <tt>MovilizerDistributionService</tt> instance.
   *
   * @param defaultConnectionTimeoutInMillis timeout to be used by default.
   * @return this to be able to chain calls in a fluid API way.
   * @since 12.11.1.2
   */
  public MovilizerConfBuilder setDefaultConnectionTimeout(Integer defaultConnectionTimeoutInMillis) {
    this.defaultConnectionTimeoutInMillis = defaultConnectionTimeoutInMillis;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_CONNECTION_TIMEOUT, defaultConnectionTimeoutInMillis));
    }
    return this;
  }

  /**
   * Receive timeout to be used in the <tt>MovilizerDistributionService</tt> instance.
   *
   * @param defaultReceiveTimeoutInMillis timeout to be used by default.
   * @return this to be able to chain calls in a fluid API way.
   * @since 12.11.1.2
   */
  public MovilizerConfBuilder setDefaultReceiveTimeout(Integer defaultReceiveTimeoutInMillis) {
    this.defaultReceiveTimeoutInMillis = defaultReceiveTimeoutInMillis;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_RECEIVE_TIMEOUT, defaultReceiveTimeoutInMillis));
    }
    return this;
  }

  /**
   * User agent string to be used in the <tt>MovilizerDistributionService</tt> calls.
   *
   * @param agentId      id to show as user agent.
   * @param agentVersion version of the user agent.
   * @return this to be able to chain calls in a fluid API way.
   * @since 12.11.1.2
   */
  public MovilizerConfBuilder setUserAgent(String agentId, String agentVersion) {
    this.agentId = agentId;
    this.agentVersion = agentVersion;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_USER_AGENT, DefaultValues.USER_AGENT_FORMAT_STRING(agentId, agentVersion)));
    }
    return this;
  }

  /**
   * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
   *
   * @param endpointAddress       the URL of the web service endpoint.
   * @param documentUploadAddress the URL of to upload documents to the cloud.
   * @return this to be able to chain calls in a fluid API way.
   * @throws MalformedURLException
   * @see EndPoint
   * @since 12.11.1.0
   */
  public MovilizerConfBuilder setEndpoint(String endpointAddress, String documentUploadAddress) throws MalformedURLException {
    setEndpoint(URI.create(endpointAddress).toURL(), URI.create(documentUploadAddress).toURL());
    return this;
  }

  /**
   * The charset encoding to be used in the files that contains the requests.
   *
   * @param outputEncoding the charset to be used.
   * @return this to be able to chain calls in a fluid API way.
   * @since 12.11.1.0
   */
  public MovilizerConfBuilder setOutputEncoding(Charset outputEncoding) {
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_ENCODING, outputEncoding.displayName()));
    }
    this.outputEncoding = outputEncoding;
    return this;
  }

  /**
   * Set the XML serialization to be thread-safe.
   *
   * @param isThreadSafe boolean to set the thread safety on or off.
   * @return this to be able to chain calls in a fluid API way.
   * @see MovilizerXMLParserService
   * @see MovilizerXMLThreadSafeParserServiceImpl
   * @since 12.11.1.4
   */
  public MovilizerConfBuilder setThreadSafe(boolean isThreadSafe) {
    this.threadSafe = isThreadSafe;
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(MESSAGES.SET_THREAD_SAFE, isThreadSafe));
    }
    return this;
  }
}
