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

import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;

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
    DEMO("https://demo.movilizer.com"),
    PROD("https://movilizer.com");

    public static final String MDS_RELATIVE_PATH = "MovilizerDistributionService/";
    public static final String WEBSERVICE_RELATIVE_PATH = "WebService";
    public static final String DOCUMENT_RELATIVE_PATH = "document";
    public static final String MAF_RELATIVE_PATH =  "maf";

    private final URL mdsUrl;
    private final URL uploadUrl;
    private final URL mafUrl;

    EndPoint(final String cloudBaseUrl) {
        try {
            String mdsBase = cloudBaseUrl;
            if (!cloudBaseUrl.endsWith("/")) {
                mdsBase = cloudBaseUrl + "/";
            }
            mdsUrl = URI.create(mdsBase + MDS_RELATIVE_PATH + WEBSERVICE_RELATIVE_PATH).toURL();
            uploadUrl = URI.create(mdsBase + MDS_RELATIVE_PATH + DOCUMENT_RELATIVE_PATH).toURL();
            mafUrl = URI.create(mdsBase + MDS_RELATIVE_PATH + MAF_RELATIVE_PATH).toURL();
        } catch (MalformedURLException e) {
            throw new MovilizerWebServiceException(e);
        }

    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "mdsUrl='" + mdsUrl + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", mafUrl='" + mafUrl + '\'' +
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

    /**
     * The Movilizer URL to MAF api.
     *
     * @return the URL of to the MAF api.
     * @since 15.11.2.1
     */
    public URL getMafUrl() {
        return mafUrl;
    }
}
