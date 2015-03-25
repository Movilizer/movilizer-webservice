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
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.models.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Path;

class MovilizerDistributionServiceImpl implements MovilizerDistributionService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerDistributionService.class);

    private MovilizerWebService webService;
    private MovilizerXMLParserService parserService;
    private UploadFileService uploadFileService;

    public MovilizerDistributionServiceImpl(MovilizerWebService webService, MovilizerXMLParserService parserService, UploadFileService uploadFileService) {
        this.webService = webService;
        this.parserService = parserService;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request) {
        logger.info(String.format(MESSAGES.PREPARE_UPLOAD_REQUEST, systemId));
        return webService.prepareUploadRequest(systemId, password, request);
    }

    @Override
    public MovilizerResponse getReplyFromCloudSync(MovilizerRequest request) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
        return webService.getReplyFromCloudSync(request);
    }

    @Override
    public void getReplyFromCloud(MovilizerRequest request, FutureCallback<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
        webService.getReplyFromCloud(request, asyncHandler);
    }

    @Override
    public UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        return uploadFileService.uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        uploadFileService.uploadDocument(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, asyncHandler);
    }

    @Override
    public UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        return uploadFileService.uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        uploadFileService.uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, asyncHandler);
    }

    @Override
    public MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException {
        logger.info(String.format(MESSAGES.READING_REQUEST_FROM_FILE, filePath.toAbsolutePath().toString()));
        return parserService.getRequestFromFile(filePath);
    }

    @Override
    public String requestToString(MovilizerRequest request) throws MovilizerXMLException {
        return parserService.printRequest(request);
    }

    @Override
    public String requestToString(MovilizerResponse response) throws MovilizerXMLException {
        return parserService.printResponse(response);
    }

    @Override
    public void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException {
        logger.info(String.format(MESSAGES.SAVING_REQUEST_TO_FILE, filePath.toAbsolutePath().toString()));
        saveRequestToFile(request, filePath);
    }
}
