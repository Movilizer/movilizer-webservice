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

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.exceptions.MovilizerXMLException;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.models.UploadResponse;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Public API to access the Movilizer Cloud Services and utils related to it. Features:
 * <ul>
 * <li>Compiled WSDL to Java classes and (a)sync webservice calls</li>
 * <li>HTTP POST method to upload documents</li>
 * <li>Convenient method for upload only requests</li>
 * <li>File access and persistence to load and save Movilizer requests</li>
 * <li>toString methods for Movilizer requests</li>
 * </ul>
 *
 * @author Jesús de Mula Cano
 * @since 12.11.1.0
 */
public interface MovilizerDistributionService {

    // ------------------------------------------------------------------------------------------ Webservice interaction

    /**
     * Sets systemd Id and password for the request and erases the replies from next response.
     *
     * @param systemId the system id to be set in the request.
     * @param password the password to be set in the request.
     * @param request  request to be modified for upload.
     * @return the same request entered in the method parameters but with the credentials and number of replies changed.
     * @since 12.11.1.0
     */
    MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request);

    /**
     * Performs a synchronous request the Movilizer cloud.
     *
     * @param request request to be used in the call.
     * @return the response coming from the cloud with the corresponding acknowledgements, errors, replies and
     * datacontainers.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @since 12.11.1.0
     */
    MovilizerResponse getReplyFromCloudSync(MovilizerRequest request) throws MovilizerWebServiceException;

    /**
     * Performs an asynchronous request the Movilizer cloud.
     *
     * @param request      request to be used in the call.
     * @param asyncHandler future instructions in form a FutureCallback that will contain the cloud response with all
     *                     its information.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see FutureCallback
     * @since 12.11.1.0
     */
    void getReplyFromCloud(MovilizerRequest request, FutureCallback<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException;

    // ------------------------------------------------------------------------------------------------- Document upload

    /**
     * Performs a synchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename            the name of the file for the document.
     * @param systemId            the system id where the document is going to be uploaded to.
     * @param password            the password of the system id where the document is going to be uploaded to.
     * @param documentPool        the document pool of the document.
     * @param documentKey         the document key for the document.
     * @param language            the language of the document.
     * @param ackKey              the acknowledge key for the document.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see UploadResponse
     * @since 12.11.1.0
     */
    UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;

    /**
     * Performs an asynchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename            the name of the file for the document.
     * @param systemId            the system id where the document is going to be uploaded to.
     * @param password            the password of the system id where the document is going to be uploaded to.
     * @param documentPool        the document pool of the document.
     * @param documentKey         the document key for the document.
     * @param language            the language of the document.
     * @param ackKey              the acknowledge key for the document.
     * @param asyncHandler        future instructions in form a FutureCallback that will contain the cloud response with
     *                            all its information.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see FutureCallback
     * @see UploadResponse
     * @since 12.11.1.0
     */
    void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException;

    /**
     * Performs a synchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId         the system id where the document is going to be uploaded to.
     * @param password         the password of the system id where the document is going to be uploaded to.
     * @param documentPool     the document pool of the document.
     * @param documentKey      the document key for the document.
     * @param language         the language of the document.
     * @param ackKey           the acknowledge key for the document.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     * @see UploadResponse
     * @since 12.11.1.0
     */
    UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;

    /**
     * Performs an asynchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId         the system id where the document is going to be uploaded to.
     * @param password         the password of the system id where the document is going to be uploaded to.
     * @param documentPool     the document pool of the document.
     * @param documentKey      the document key for the document.
     * @param language         the language of the document.
     * @param ackKey           the acknowledge key for the document.
     * @param asyncHandler     future instructions in form a FutureCallback that will contain the cloud response with
     *                         all its information.
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     * @see FutureCallback
     * @see UploadResponse
     * @since 12.11.1.0
     */
    void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException;

    // -------------------------------------------------------------------------------------------------- File XML utils

    /**
     * Reads and parses a Movilizer Request file (.mxml) from the file system.
     *
     * @param filePath path to the request file.
     * @return the java instance parsed from the file.
     * @throws MovilizerXMLException when there's parsing or file access problems.
     * @since 12.11.1.0
     */
    MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException;

    /**
     * Persists a request java instance to a file.
     *
     * @param request  the request to be persisted.
     * @param filePath the path to the file.
     * @throws MovilizerXMLException when there's parsing or file access problems.
     * @since 12.11.1.0
     */
    void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException;

    /**
     * Generates a string from a request java instance.
     *
     * @param request the request to be transformed into string.
     * @return the string representation of the request.
     * @throws MovilizerXMLException if there's parsing problems.
     * @since 12.11.1.0
     */
    String requestToString(MovilizerRequest request) throws MovilizerXMLException;

    /**
     * Generates a string from a response java instance.
     *
     * @param response the response to be transformed into string.
     * @return the string representation of the response.
     * @throws MovilizerXMLException if there's parsing problems.
     * @since 12.11.1.0
     */
    String requestToString(MovilizerResponse response) throws MovilizerXMLException;
}
