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

package com.movilizer.mds.webservice;

import com.movilizer.mds.webservice.services.MovilizerConfBuilder;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;

/**
 * Main class for the Movilizer webservice usage. It provides access to configuration builder for the webservice and a
 * shortcut for the webservice with default values.
 * <p/>
 * Usage with defaults DEMO cloud values and UTF-8 for requests files:
 * <pre>
 * {@code
 * MovilizerDistributionService mds = Movilizer.getService();
 * </pre>
 * <p/>
 * Usage giving your own values for private clouds:
 * <pre>
 * {@code
 * MovilizerDistributionService mds = Movilizer.buildConf()
 *                                          .setEndpoint("https://movilizer.mycloud.com/WebService/",
 *                                                       "https://movilizer.mycloud.com/mds/document")
 *                                          .setOutputEncoding(Charset.defaultCharset())
 *                                          .getService();
 * </pre>
 *
 * @author Jesús de Mula Cano
 * @see MovilizerDistributionService
 * @see MovilizerConfBuilder
 * @since 12.11.1.0
 */
public class Movilizer {
    public static final String SCHEMA = "META-INF/movilizer/wsdl/MovilizerTypesV12.xsd";
    public static final String WSDL = "META-INF/movilizer/wsdl/MovilizerV12Wsdl11.wsdl";
    public static final String ONLINE_WSDL = "META-INF/movilizer/wsdl/MovilizerOnlineV12Wsdl11.wsdl";
    public static final String ONLINE_INTERFACE = "com.movilitas.movilizer.v12.MovilizerOnlineWebServiceV12";
    public static final String ONLINE_OPERATION = "MovilizerOnlineCallback";

    /**
     * This class in not meant to be instantiated.
     */
    private Movilizer() {
    }

    /**
     * Provides the webservice instance with the default configuration.
     *
     * @return a MovilizerDistributionService instance loaded with defaults.
     * @see MovilizerDistributionService
     * @since 12.11.1.0
     */
    public static MovilizerDistributionService getService() {
        return new MovilizerConfBuilder().getService();
    }

    /**
     * Provides a configuration builder to specify which parameters are to be modified from the defaults.
     *
     * @return the builder that can be used to get the webservice instance.
     * @see MovilizerConfBuilder
     * @since 12.11.1.0
     */
    public static MovilizerConfBuilder buildConf() {
        return new MovilizerConfBuilder();
    }
}
