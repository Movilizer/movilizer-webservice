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
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.Messages;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class is used to set the configuration of interest for the Movilizer webservice and then
 * return an instance configured. Do not instance it by yourself, use the
 * {@code Movilizer.buildConf()} static factory method.
 * <p>
 * Intended use would be as the following:
 * <pre>
 * {@code
 * MovilizerDistributionService mds = Movilizer.buildConf()
 *                                          .setEndpoint(EndPoint.PROD)
 *                                          .setOutputEncoding(Charset.defaultCharset())
 *                                          .getService();
 * }
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
    private URI cloudBaseAddress;
    private String mdsRelativePath;
    private Integer defaultConnectionTimeoutInMillis = DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS;
    private Integer defaultReceiveTimeoutInMillis = DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS;
    private String agentId = DefaultValues.AGENT_ID;
    private String agentVersion = DefaultValues.AGENT_VERSION;
    private boolean threadSafe = false;

    /**
     * <b>DO NOT USE</b>. Use instead Movilizer.buildConf() or Movilizer.getService()
     *
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
            logger.trace(Messages.BUILDING_CONFIG);
        }
        MovilizerWebServiceV15 movilizerCloud = new MovilizerWebServiceV15Service()
                .getMovilizerWebServiceV15Soap11();
        MovilizerWebService webService = new MovilizerWebService(movilizerCloud,
                defaultConnectionTimeoutInMillis, defaultReceiveTimeoutInMillis, agentId,
                agentVersion);
        webService.setEndpoint(endpoint.getMdsUrl());
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.USING_ENCODING, outputEncoding.displayName()));
        }
        MovilizerXMLParserService parserService;
        if (this.threadSafe) {
            parserService = new MovilizerXMLThreadSafeParserServiceImpl(outputEncoding);
        } else {
            parserService = new MovilizerXMLParserServiceImpl(outputEncoding);
        }

        UploadFileService uploadFileService = new UploadFileService(endpoint.getUploadUrl(),
                new MovilizerUploadForm(), defaultConnectionTimeoutInMillis);

        MafManagementService mafService = new MafManagementService(endpoint.getMafUrl(),
                defaultConnectionTimeoutInMillis);

        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.USING_CLOUD_CONFIG, endpoint.toString()));
        }

        FolderLoaderService loaderService = new FolderLoaderService(parserService);

        return new MovilizerDistributionServiceImpl(
                webService,
                parserService,
                uploadFileService,
                loaderService,
                mafService
        );
    }

    protected URI getCloudBaseAddress() {
        return cloudBaseAddress;
    }

    protected String getMdsRelativePath() {
        return mdsRelativePath;
    }

    protected EndPoint getEndpoint() {
        return endpoint;
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
            logger.trace(String.format(Messages.SET_ENDPOINT, endpoint.toString()));
        }
        return this;
    }

    /**
     * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
     *
     * @param cloudBaseAddress the URI of the Movilizer cloud. I.e.: https//demo.movilizer.com
     * @return this to be able to chain calls in a fluid API way.
     * @see EndPoint
     * @since 12.11.1.0
     */
    public MovilizerConfBuilder setEndpoint(URI cloudBaseAddress) {
        this.cloudBaseAddress = cloudBaseAddress;
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.SET_PRIVATE_ENDPOINT, cloudBaseAddress.toString()));
        }
        return setEndpoint(new EndPoint(cloudBaseAddress.toString()));
    }

    /**
     * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
     *
     * @param cloudBaseAddress the URL of the Movilizer cloud. I.e.: https//demo.movilizer.com
     * @return this to be able to chain calls in a fluid API way.
     * @throws MalformedURLException when any url given is not valid
     * @see EndPoint
     * @since 12.11.1.0
     */
    public MovilizerConfBuilder setEndpoint(String cloudBaseAddress)
            throws MalformedURLException {
        String mdsBase = cloudBaseAddress;
        if (!cloudBaseAddress.endsWith("/")) {
            mdsBase = cloudBaseAddress + "/";
        }
        setEndpoint(URI.create(mdsBase));
        return this;
    }

    /**
     * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
     *
     * @param cloudBaseAddress the URI of the Movilizer cloud. I.e.: https//demo.movilizer.com
     * @param mdsRelativePath  the relative URL of the Movilizer Distribution Service. I.e.: mds/
     * @return this to be able to chain calls in a fluid API way.
     * @see EndPoint
     * @since 15.11.2.4
     */
    public MovilizerConfBuilder setEndpoint(URI cloudBaseAddress, String mdsRelativePath) {
        this.cloudBaseAddress = cloudBaseAddress;
        this.mdsRelativePath = mdsRelativePath;
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(
                    Messages.SET_PRIVATE_ENDPOINT_WITH_CUSTOM_MDS,
                    cloudBaseAddress.toString(),
                    mdsRelativePath));
        }
        return setEndpoint(new EndPoint(cloudBaseAddress.toString(), mdsRelativePath));
    }

    /**
     * URLs to be used in the <tt>MovilizerDistributionService</tt> instance.
     *
     * @param cloudBaseAddress the URL of the Movilizer cloud. I.e.: https//demo.movilizer.com
     * @param mdsRelativePath  the relative URL of the Movilizer Distribution Service. I.e.: mds/
     * @return this to be able to chain calls in a fluid API way.
     * @throws MalformedURLException when any url given is not valid
     * @see EndPoint
     * @since 15.11.2.4
     */
    public MovilizerConfBuilder setEndpoint(String cloudBaseAddress, String mdsRelativePath)
            throws MalformedURLException {
        String mdsBase = cloudBaseAddress;
        if (!cloudBaseAddress.endsWith("/")) {
            mdsBase = cloudBaseAddress + "/";
        }
        setEndpoint(URI.create(mdsBase), mdsRelativePath);
        return this;
    }

    protected Integer getDefaultConnectionTimeoutInMillis() {
        return defaultConnectionTimeoutInMillis;
    }

    /**
     * Connection timeout to be used in the <tt>MovilizerDistributionService</tt> instance.
     *
     * @param defaultConnectionTimeoutInMillis timeout to be used by default.
     * @return this to be able to chain calls in a fluid API way.
     * @since 12.11.1.2
     */
    public MovilizerConfBuilder setDefaultConnectionTimeout(
            Integer defaultConnectionTimeoutInMillis) {
        this.defaultConnectionTimeoutInMillis = defaultConnectionTimeoutInMillis;
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.SET_CONNECTION_TIMEOUT,
                    defaultConnectionTimeoutInMillis));
        }
        return this;
    }

    protected Integer getDefaultReceiveTimeoutInMillis() {
        return defaultReceiveTimeoutInMillis;
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
            logger.trace(String.format(Messages.SET_RECEIVE_TIMEOUT,
                    defaultReceiveTimeoutInMillis));
        }
        return this;
    }

    protected String getAgentId() {
        return agentId;
    }

    protected String getAgentVersion() {
        return agentVersion;
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
            logger.trace(String.format(Messages.SET_USER_AGENT,
                    DefaultValues.userAgentFormatString(agentId, agentVersion)));
        }
        return this;
    }

    protected Charset getOutputEncoding() {
        return outputEncoding;
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
            logger.trace(String.format(Messages.SET_ENCODING, outputEncoding.displayName()));
        }
        this.outputEncoding = outputEncoding;
        return this;
    }

    protected boolean isThreadSafe() {
        return threadSafe;
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
            logger.trace(String.format(Messages.SET_THREAD_SAFE, isThreadSafe));
        }
        return this;
    }
}
