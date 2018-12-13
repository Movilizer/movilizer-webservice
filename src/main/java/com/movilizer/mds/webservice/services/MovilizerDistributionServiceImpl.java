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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.movilizer.mds.webservice.exceptions.MovilizerIOException;
import com.movilizer.mds.webservice.models.maf.MafSource;
import com.movilizer.mds.webservice.models.maf.communications.MafResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.webservice.messages.Messages;
import com.movilizer.mds.webservice.models.UploadResponse;

class MovilizerDistributionServiceImpl implements MovilizerDistributionService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerDistributionService.class);

    private MovilizerWebService webService;
    private MovilizerXMLParserService parserService;
    private UploadFileService uploadFileService;
    private FolderLoaderService loaderService;
    private MafManagementService mafService;

    public MovilizerDistributionServiceImpl(MovilizerWebService webService, MovilizerXMLParserService parserService,
                                            UploadFileService uploadFileService, FolderLoaderService loaderService,
                                            MafManagementService mafService) {
        this.webService = webService;
        this.parserService = parserService;
        this.uploadFileService = uploadFileService;
        this.loaderService = loaderService;
        this.mafService = mafService;
    }

    @Override
    public MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PREPARE_UPLOAD_REQUEST, systemId));
        }
        return webService.prepareUploadRequest(systemId, password, request);
    }

    @Override
    public MovilizerRequest prepareDownloadRequest(Long systemId, String password, Integer numResponses, MovilizerRequest request) {
        return webService.prepareDownloadRequest(systemId, password, numResponses, request);
    }

    @Override
    public MovilizerResponse getReplyFromCloudSync(MovilizerRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
        }
        return webService.getReplyFromCloudSync(request);
    }

    @Override
    public MovilizerResponse getReplyFromCloudSync(MovilizerRequest request, Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
        }
        return webService.getReplyFromCloudSync(request, connectionTimeoutInMillis, receiveTimeoutInMillis);
    }

    @Override
    public CompletableFuture<MovilizerResponse> getReplyFromCloud(MovilizerRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
        }
        return webService.getReplyFromCloud(request);
    }

    @Override
    public CompletableFuture<MovilizerResponse> getReplyFromCloud(MovilizerRequest request, Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
        }
        return webService.getReplyFromCloud(request, connectionTimeoutInMillis, receiveTimeoutInMillis);
    }

    @Override
    public Boolean responseHasErrors(MovilizerResponse response) {
        return webService.responseHasErrors(response);
    }

    @Override
    public String responseErrorsToString(MovilizerResponse response) {
        return webService.prettyPrintErrors(response);
    }

    @Override
    public List<MovilizerResponse> batchUploadFolderSync(Path folder) {
        try {
            List<MovilizerRequest> requests = loaderService.loadRequestsFromFolder(folder);
            List<MovilizerResponse> responses = new ArrayList<>(requests.size());
            for (MovilizerRequest request : requests) {
                MovilizerResponse response = getReplyFromCloudSync(request);
                responses.add(response);
            }
            return responses;
        } catch (IOException e) {
            throw new MovilizerIOException(e);
        }
    }

    @Override
    public UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
    }

    @Override
    public CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocument(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
    }

    @Override
    public CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey);
    }

    @Override
    public CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocument(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
    }

    @Override
    public CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return uploadFileService.uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
    }

    @Override
    public <T> T getElementFromString(String elementString, Class<T> movilizerElementClass) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PARSING_XML, movilizerElementClass.getName()));
        }
        return parserService.getMovilizerElementFromString(elementString, movilizerElementClass);
    }

    @Override
    public <T> String printMovilizerElementToString(T movilizerElement, Class<T> movilizerElementClass) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PRINTING_XML, movilizerElementClass.getName()));
        }
        return parserService.printMovilizerElementToString(movilizerElement, movilizerElementClass);
    }

    @Override
    public MovilizerRequest getRequestFromFile(Path filePath) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.READING_REQUEST_FROM_FILE, filePath.toAbsolutePath().toString()));
        }
        return parserService.getRequestFromFile(filePath);
    }

    @Override
    public MovilizerRequest getRequestFromString(String requestString) {
        if (logger.isDebugEnabled()) {
            logger.debug(Messages.READING_REQUEST_FROM_STRING);
        }
        return parserService.getRequestFromString(requestString);
    }

    @Override
    public String requestToString(MovilizerRequest request) {
        return parserService.printRequest(request);
    }

    @Override
    public String responseToString(MovilizerResponse response) {
        return parserService.printResponse(response);
    }

    @Override
    public void saveRequestToFile(MovilizerRequest request, Path filePath) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.SAVING_REQUEST_TO_FILE, filePath.toAbsolutePath().toString()));
        }
        parserService.saveRequestToFile(request, filePath);
    }

    @Override
    public MafSource readSource(File sourceFile) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.READ_MAF_SCRIPT_FILE, sourceFile.getPath()));
        }
        return this.mafService.readSource(sourceFile);
    }

    @Override
    public MafResponse deploySourceSync(long systemId, String password, String token, MafSource source) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PROCESSING_MAF_SOURCE, source.getDescription()));
        }
        MafResponse response = this.mafService.deploySourceSync(systemId, password, token, source);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.FINISHED_UPLOADING_MAF_SOURCE, source.getDescription(), systemId));
        }
        return response;
    }

    @Override
    public MafResponse deploySourceSync(long systemId, String password, String token, File sourceFile) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PROCESSING_MAF_SOURCE_FILE, sourceFile.getPath()));
        }
        MafResponse response = this.mafService.deploySourceSync(systemId, password, token, sourceFile);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.FINISHED_UPLOADING_MAF_SOURCE_FILE, sourceFile.getPath(), systemId));
        }
        return response;
    }
}
