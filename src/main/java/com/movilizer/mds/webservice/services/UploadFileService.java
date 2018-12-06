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

import com.movilizer.mds.webservice.adapters.ResponseHandlerAdapter;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.Messages;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import com.movilizer.mds.webservice.models.UploadResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;


class UploadFileService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private static final String USER_AGENT_HEADER_KEY = "User-Agent";
    private static final int POSSIBLE_BAD_CREDENTIALS = 400;

    private URL documentUploadAddress;
    private MovilizerUploadForm movilizerUpload;
    private Integer defaultConnectionTimeoutInMillis;

    protected UploadFileService(URL documentUploadAddress, MovilizerUploadForm movilizerUpload,
                                Integer defaultConnectionTimeoutInMillis) {
        this.documentUploadAddress = documentUploadAddress;
        this.movilizerUpload = movilizerUpload;
        this.defaultConnectionTimeoutInMillis = defaultConnectionTimeoutInMillis;
    }

    public void setDocumentUploadAddress(URL documentUploadAddress) {
        this.documentUploadAddress = documentUploadAddress;
    }

    protected UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename,
                                                long systemId, String password, String documentPool,
                                                String documentKey, String language,
                                                String ackKey) {
        return uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool,
                documentKey, language, ackKey, defaultConnectionTimeoutInMillis);
    }

    protected UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename,
                                                long systemId, String password, String documentPool,
                                                String documentKey, String language, String ackKey,
                                                Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        UploadResponse result = uploadSync(movilizerUpload.getForm(documentInputStream, filename,
                systemId, password, documentPool, documentKey, language,
                getSuffixFromFilename(filename), ackKey), connectionTimeoutInMillis);
        if (logger.isInfoEnabled()) {
            logger.info(Messages.UPLOAD_COMPLETE);
        }
        return result;
    }

    protected UploadResponse uploadDocumentSync(Path documentFilePath, long systemId,
                                                String password, String documentPool,
                                                String documentKey, String language,
                                                String ackKey) {
        return uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey,
                language, ackKey, defaultConnectionTimeoutInMillis);
    }

    protected UploadResponse uploadDocumentSync(Path documentFilePath, long systemId,
                                                String password, String documentPool,
                                                String documentKey, String language, String ackKey,
                                                Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        UploadResponse result = uploadSync(movilizerUpload.getForm(documentFilePath.toFile(),
                systemId, password, documentPool, documentKey, language,
                getSuffixFromFilename(documentFilePath.getFileName()), ackKey),
                connectionTimeoutInMillis);
        if (logger.isInfoEnabled()) {
            logger.info(Messages.UPLOAD_COMPLETE);
        }
        return result;
    }

    protected CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream,
                                                               String filename, long systemId,
                                                               String password, String documentPool,
                                                               String documentKey, String language,
                                                               String ackKey) {
        return uploadDocument(documentInputStream, filename, systemId, password, documentPool,
                documentKey, language, ackKey, defaultConnectionTimeoutInMillis);
    }

    protected CompletableFuture<UploadResponse> uploadDocument(InputStream documentInputStream,
                                                               String filename, long systemId,
                                                               String password, String documentPool,
                                                               String documentKey, String language,
                                                               String ackKey,
                                                               Integer connectionTimeoutInMillis) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return upload(movilizerUpload.getForm(documentInputStream, filename, systemId, password,
                documentPool, documentKey, language, getSuffixFromFilename(filename), ackKey),
                connectionTimeoutInMillis);
    }

    protected CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath,
                                                               long systemId, String password,
                                                               String documentPool,
                                                               String documentKey,
                                                               String language, String ackKey) {
        return uploadDocument(documentFilePath, systemId, password, documentPool, documentKey,
                language, ackKey, defaultConnectionTimeoutInMillis);
    }

    protected CompletableFuture<UploadResponse> uploadDocument(Path documentFilePath,
                                                               long systemId, String password,
                                                               String documentPool,
                                                               String documentKey, String language,
                                                               String ackKey,
                                                               Integer connectionTimeoutInMillis) {
        String suffix = getSuffixFromFilename(documentFilePath.getFileName());
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(Messages.PERFORMING_UPLOAD, systemId));
        }
        return upload(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password,
                documentPool, documentKey, language, suffix, ackKey), connectionTimeoutInMillis);
    }

    private String getSuffixFromFilename(Path filename) {
        if (filename == null) {
            throw new MovilizerWebServiceException(String.format(Messages.MISSING_FILE_EXTENSION,
                    "null filename"));
        }
        return getSuffixFromFilename(filename.toString());
    }

    private String getSuffixFromFilename(String filename) {
        if (!filename.contains(".")) {
            throw new MovilizerWebServiceException(String.format(Messages.MISSING_FILE_EXTENSION,
                    filename));
        }
        String[] filenameSplit = filename.split("\\.");
        return filenameSplit[filenameSplit.length - 1];
    }

    private UploadResponse uploadSync(HttpEntity entity, Integer connectionTimeoutInMillis) {
        try {
            HttpResponse response = Request.Post(documentUploadAddress.toURI())
                    .addHeader(USER_AGENT_HEADER_KEY, DefaultValues.USER_AGENT)
                    .connectTimeout(connectionTimeoutInMillis)
                    .body(entity)
                    .execute().returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if (!(HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)) {
                String errorMessage = response.getStatusLine().getReasonPhrase();
                if (statusCode == POSSIBLE_BAD_CREDENTIALS) {
                    errorMessage = errorMessage + Messages.FAILED_FILE_UPLOAD_CREDENTIALS;
                }
                throw new MovilizerWebServiceException(String.format(Messages.FAILED_FILE_UPLOAD,
                        statusCode, errorMessage));
            }
            return new UploadResponse(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        } catch (IOException | URISyntaxException e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format(Messages.UPLOAD_ERROR, e.getMessage()));
            }
            throw new MovilizerWebServiceException(e);
        }
    }

    private CompletableFuture<UploadResponse> upload(HttpEntity entity,
                                                     Integer connectionTimeoutInMillis) {
        CompletableFuture<UploadResponse> future = new CompletableFuture<>();
        try {

            Async.newInstance().execute(Request.Post(documentUploadAddress.toURI())
                    .addHeader(USER_AGENT_HEADER_KEY, DefaultValues.USER_AGENT)
                    .connectTimeout(connectionTimeoutInMillis)
                    .body(entity), new ResponseHandlerAdapter<UploadResponse>(future) {
                        @Override
                        public UploadResponse convertHttpResponse(HttpResponse httpResponse) {
                            logger.info(Messages.UPLOAD_COMPLETE);
                            int statusCode = httpResponse.getStatusLine().getStatusCode();
                            String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
                            if (statusCode == POSSIBLE_BAD_CREDENTIALS) {
                                errorMessage = errorMessage +
                                        Messages.FAILED_FILE_UPLOAD_CREDENTIALS;
                            }
                            return new UploadResponse(statusCode, errorMessage);
                        }
                    });
        } catch (URISyntaxException e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format(Messages.UPLOAD_ERROR, e.getMessage()));
            }
            future.completeExceptionally(new MovilizerWebServiceException(e));
        }
        return future;
    }
}
