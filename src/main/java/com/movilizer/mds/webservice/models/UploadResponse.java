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

package com.movilizer.mds.webservice.models;

/**
 * This class encapsulates the results of the HTTP POST done to the Movilizer upload document
 * service.
 *
 * @author Jesús de Mula Cano
 * @since 12.11.1.0
 */
public class UploadResponse {
    private static final int SC_OK = 200;
    private static final int SC_MULTIPLE_CHOICES = 300;

    private int statusCode;
    private String message;

    public UploadResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * If the status code indicates a successful request then returns true, anything else returns
     * false.
     *
     * @return true if the request was successful.
     */
    public boolean wasSuccessful() {
        return SC_OK <= statusCode && statusCode < SC_MULTIPLE_CHOICES;
    }

    /**
     * The HTTP status code of the response.
     *
     * @return the HTTP status code of the response.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * The response message of the HTTP POST.
     *
     * @return the response message of the HTTP POST.
     */
    public String getMessage() {
        return message;
    }
}
