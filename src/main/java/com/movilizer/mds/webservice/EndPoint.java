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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Movilizer official endpoints to the public cloud.
 *
 * @author Jesús de Mula Cano
 * @since 12.11.1.0
 */
public enum EndPoint {
    DEMO("https://demo.movilizer.com/MovilizerDistributionService/WebService/", "https://demo.movilizer.com/mds/document"),
    PROD("https://movilizer.com/MovilizerDistributionService/WebService/", "https://movilizer.com/mds/document");

    private final URL mdsUrl;
    private final URL uploadUrl;

    EndPoint(final String mdsUrl, final String uploadUrl) throws MalformedURLException {
        this.mdsUrl = URI.create(mdsUrl).toURL();
        this.uploadUrl = URI.create(uploadUrl).toURL();
    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "mdsUrl='" + mdsUrl + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }

    /**
     * The URL for the Movilizer Web Service (MDS).
     *
     * @return the URL of the web service endpoint.
     * @since 12.11.1.0
     */
    public URL getMdsUrl() {
        return mdsUrl;
    }

    /**
     * The Movilizer URL to upload documents.
     *
     * @return the URL of to upload documents to the cloud.
     * @since 12.11.1.0
     */
    public URL getUploadUrl() {
        return uploadUrl;
    }
}
