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

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.models.UploadResponse;
import com.movilizer.mds.webservice.models.maf.MafSource;
import com.movilizer.mds.webservice.models.maf.communications.MafResponse;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
 * @since 12.11.1.0
 */
public interface MovilizerDistributionService {

    // ---------------------------------------------------------------------- Webservice interaction

    /**
     * Set systemd Id and password for the request and erases the replies from next response.
     *
     * @param systemId the system id to be set in the request.
     * @param password the password to be set in the request.
     * @param request  request to be modified for upload.
     * @return the same request entered in the method parameters but with the credentials and number
     *         of replies changed.
     * @since 12.11.1.0
     */
    MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request);

    /**
     * Set systemd Id and password for the request and set the number of replies replies from next
     * response.
     *
     * @param systemId the system id to be set in the request.
     * @param password the password to be set in the request.
     * @param numResponses the number of responses to receive in the response.
     * @param request request to be modified for upload.
     * @return the same request entered in the method parameters but with the credentials and number
     *         of replies changed.
     * @since 12.11.1.0
     */
    MovilizerRequest prepareDownloadRequest(Long systemId, String password, Integer numResponses,
                                            MovilizerRequest request);

    /**
     * Perform a synchronous request the Movilizer cloud.
     *
     * @param request request to be used in the call.
     * @return the response coming from the cloud with the corresponding acknowledgements, errors,
     *         replies and datacontainers.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @since 12.11.1.0
     */
    MovilizerResponse getReplyFromCloudSync(MovilizerRequest request);

    /**
     * Perform a synchronous request the Movilizer cloud.
     *
     * @param request request to be used in the call.
     * @param connectionTimeoutInMillis connection timeout to be used in the call.
     * @param receiveTimeoutInMillis receive timeout to be used in the call.
     * @return the response coming from the cloud with the corresponding acknowledgements, errors,
     *         replies and datacontainers.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @since 12.11.1.0
     */
    MovilizerResponse getReplyFromCloudSync(MovilizerRequest request,
                                            Integer connectionTimeoutInMillis,
                                            Integer receiveTimeoutInMillis);

    /**
     * Perform an asynchronous request the Movilizer cloud.
     *
     * @param request request to be used in the call.
     * @return A completable future with the response.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see CompletableFuture
     * @since 15.11.2.2
     */
    CompletableFuture<MovilizerResponse> getReplyFromCloud(MovilizerRequest request);

    /**
     * Perform an asynchronous request the Movilizer cloud.
     *
     * @param request request to be used in the call.
     * @param connectionTimeoutInMillis connection timeout to be used in the call.
     * @param receiveTimeoutInMillis receive timeout to be used in the call.
     * @return A completable future with the response.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see CompletableFuture
     * @since 12.11.2.2
     */
    CompletableFuture<MovilizerResponse> getReplyFromCloud(MovilizerRequest request,
                                                           Integer connectionTimeoutInMillis,
                                                           Integer receiveTimeoutInMillis);

    /**
     * Indicate if the response java instance has errors.
     *
     * @param response the response to be analyzed.
     * @return true in case response has errors else false.
     * @since 12.11.1.3
     */
    Boolean responseHasErrors(MovilizerResponse response);

    /**
     * Generate a string with errors.
     *
     * @param response the response that contains the errors.
     * @return string represantion of the errors to use in messages.
     * @since 12.11.1.3
     */
    String responseErrorsToString(MovilizerResponse response);

    /**
     * Send all files that has .mxml extension in a folder and subfolders collecting all cloud
     * responses.
     *
     * @param folder to walk for the request files.
     * @return a list with all the cloud responses.
     * @since 15.11.1.5
     */
    List<MovilizerResponse> batchUploadFolderSync(Path folder);

    // ----------------------------------------------------------------------------- Document upload

    /**
     * Perform a synchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename the name of the file for the document.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see UploadResponse
     * @since 12.11.1.0
     */
    UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename,
                                      long systemId, String password, String documentPool,
                                      String documentKey, String language, String ackKey);

    /**
     * Perform a synchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     * @see UploadResponse
     * @since 12.11.1.0
     */
    UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password,
                                      String documentPool, String documentKey, String language,
                                      String ackKey);

    /**
     * Perform a synchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename the name of the file for the document.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @param connectionTimeoutInMillis timeout of the request.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see UploadResponse
     * @since 12.11.1.2
     */
    UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename,
                                      long systemId, String password, String documentPool,
                                      String documentKey, String language, String ackKey,
                                      Integer connectionTimeoutInMillis);

    /**
     * Perform a synchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @param connectionTimeoutInMillis timeout of the request.
     * @return the upload response with the results of the upload
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     * @see UploadResponse
     * @since 12.11.1.2
     */
    UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password,
                                      String documentPool, String documentKey, String language,
                                      String ackKey, Integer connectionTimeoutInMillis);

    /**
     * Perform an asynchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename the name of the file for the document.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @return a future with the upload response.
     * @throws MovilizerWebServiceException when there's connection problems.
     * @see CompletableFuture
     * @see UploadResponse
     * @since 15.11.2.2
     */
    CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream,
                                                     String filename, long systemId,
                                                     String password, String documentPool,
                                                     String documentKey, String language,
                                                     String ackKey);

    /**
     * Perform an asynchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @return a future with the upload response.
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     * @see CompletableFuture
     * @see UploadResponse
     * @since 15.11.2.2
     */
    CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath, long systemId,
                                                     String password, String documentPool,
                                                     String documentKey, String language,
                                                     String ackKey);

    /**
     * Perform an asynchronous blob upload to the Movilizer Cloud given the input stream.
     *
     * @param documentInputStream the input stream containing the blob/document to upload.
     * @param filename the name of the file for the document.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @param connectionTimeoutInMillis timeout of the request.
     * @return a future with the upload response.
     * @throws MovilizerWebServiceException when there's connection problems.
     *
     * @see CompletableFuture
     * @see UploadResponse
     * @since 15.11.2.2
     */
    CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream,
                                                     String filename, long systemId,
                                                     String password, String documentPool,
                                                     String documentKey, String language,
                                                     String ackKey,
                                                     Integer connectionTimeoutInMillis);

    /**
     * Perform an asynchronous blob upload to the Movilizer Cloud given the path to a file.
     *
     * @param documentFilePath the path to the document file to upload.
     * @param systemId the system id where the document is going to be uploaded to.
     * @param password the password of the system id where the document is going to be uploaded to.
     * @param documentPool the document pool of the document.
     * @param documentKey the document key for the document.
     * @param language the language of the document.
     * @param ackKey the acknowledge key for the document.
     * @param connectionTimeoutInMillis timeout of the request.
     * @return a future with the upload response.
     * @throws MovilizerWebServiceException when there's connection or file access problems.
     *
     * @see CompletableFuture
     * @see UploadResponse
     * @since 15.11.2.2
     */
    CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath, long systemId,
                                                     String password, String documentPool,
                                                     String documentKey, String language,
                                                     String ackKey,
                                                     Integer connectionTimeoutInMillis);

    // ----------------------------------------------------------------------------------- XML utils

    /**
     * Read a Movilizer element from the webservice name space (MovilizerMovelet, MovilizerReply,
     * etc...) and creates a java object instance of the class indicated.
     *
     * @param elementString string value of the serialization of the object.
     * @param movilizerElementClass class of the Movilizer object to create from the string.
     * @param <T> Movilizer object class
     * @return the java instance parsed from the string.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException when there are parsing
     *         problems.
     * @since 12.11.1.3
     */
    <T> T getElementFromString(final String elementString, final Class<T> movilizerElementClass);

    /**
     * Print a Movilizer element from the webservice name space (MovilizerMovelet, MovilizerReply,
     * etc...) to string.
     *
     * @param movilizerElement      java instance of the Movilizer element.
     * @param movilizerElementClass class of the Movilizer element to use.
     * @param <T>                   Movilizer object class
     * @return XML string representation for the Movilizer element given.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException when there are parsing
     *         problems.
     * @since 12.11.1.3
     */
    <T> String printMovilizerElementToString(final T movilizerElement,
                                             final Class<T> movilizerElementClass);

    // ------------------------------------------------------------------------------ File XML utils

    /**
     * Read and parses a Movilizer Request file (.mxml) from the file system.
     *
     * @param filePath path to the request file.
     * @return the java instance parsed from the file.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException when there's parsing or
     *         file access problems.
     * @since 12.11.1.0
     */
    MovilizerRequest getRequestFromFile(Path filePath);

    /**
     * Read and parses a Movilizer Request from the given String.
     *
     * @param requestString non null string with a valid xml request.
     * @return the java instance parsed from the file.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException when there's parsing or
     *         string problems.
     * @since 12.11.1.1
     */
    MovilizerRequest getRequestFromString(String requestString);

    /**
     * Persist a request java instance to a file.
     *
     * @param request  the request to be persisted.
     * @param filePath the path to the file.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException when there's parsing or
     *         file access problems.
     * @since 12.11.1.0
     */
    void saveRequestToFile(MovilizerRequest request, Path filePath);

    /**
     * Generate a string from a request java instance.
     *
     * @param request the request to be transformed into string.
     * @return the string representation of the request.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException if there's parsing
     *         problems.
     * @since 12.11.1.0
     */
    String requestToString(MovilizerRequest request);

    /**
     * Generate a string from a response java instance.
     *
     * @param response the response to be transformed into string.
     * @return the string representation of the response.
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerXMLException if there's parsing
     *         problems.
     * @since 12.11.1.0
     */
    String responseToString(MovilizerResponse response);

    // ------------------------------------------------------------------------------ MAF Management

    /**
     * Read a file into an MAF source file for latter source uploading.
     *
     * @param sourceFile file to read
     * @return MafSource with the source and the metadata
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerMAFManagementException when having
     *         troubles reading the file.
     * @see com.movilizer.mds.webservice.models.maf.MafEventScript
     * @see com.movilizer.mds.webservice.models.maf.MafLibraryScript
     * @see com.movilizer.mds.webservice.models.maf.MafGenericScript
     * @since 15.11.2.1
     */
    MafSource readSource(File sourceFile);

    /**
     * Deploy a MAF source to the cloud.
     *
     * @param systemId where to deploy the file
     * @param password for the system id given
     * @param token    for MAF access
     * @param source   file to upload
     * @return MafResponse with the result of the operation
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerMAFManagementException when having
     *         troubles uploading the file.
     * @see com.movilizer.mds.webservice.models.maf.communications.MafEventResponse
     * @see com.movilizer.mds.webservice.models.maf.communications.MafLibraryResponse
     * @see com.movilizer.mds.webservice.models.maf.communications.MafGenericResponse
     * @since 15.11.2.1
     */
    MafResponse deploySourceSync(long systemId, String password, String token, MafSource source);

    /**
     * Deploy a MAF source to the cloud.
     *
     * @param systemId   where to deploy the file
     * @param password   for the system id given
     * @param token      for MAF access
     * @param sourceFile file to upload
     * @return MafResponse with the result of the operation
     * @throws com.movilizer.mds.webservice.exceptions.MovilizerMAFManagementException when having
     *         troubles uploading the file.
     * @see com.movilizer.mds.webservice.models.maf.communications.MafEventResponse
     * @see com.movilizer.mds.webservice.models.maf.communications.MafLibraryResponse
     * @see com.movilizer.mds.webservice.models.maf.communications.MafGenericResponse
     * @since 15.11.2.1
     */
    MafResponse deploySourceSync(long systemId, String password, String token, File sourceFile);
}
