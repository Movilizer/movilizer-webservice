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
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.models.FutureCallback;
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


class UploadFileService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);

    private URL documentUploadAddress;
    private MovilizerUploadForm movilizerUpload;

    protected UploadFileService(URL documentUploadAddress, MovilizerUploadForm movilizerUpload) {
        this.documentUploadAddress = documentUploadAddress;
        this.movilizerUpload = movilizerUpload;
    }

    public void setDocumentUploadAddress(URL documentUploadAddress) {
        this.documentUploadAddress = documentUploadAddress;
    }

    protected UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        logger.debug(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        UploadResponse result = uploadSync(movilizerUpload.getForm(documentInputStream, filename, systemId, password, documentPool, documentKey, language, getSuffixFromFilename(filename), ackKey));
        logger.info(MESSAGES.UPLOAD_COMPLETE);
        return result;
    }

    protected void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        logger.debug(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        upload(movilizerUpload.getForm(documentInputStream, filename, systemId, password, documentPool, documentKey, language, getSuffixFromFilename(filename), ackKey), asyncHandler);
    }

    protected UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        logger.debug(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        UploadResponse result = uploadSync(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password, documentPool, documentKey, language, getSuffixFromFilename(documentFilePath.getFileName().toString()), ackKey));
        logger.info(MESSAGES.UPLOAD_COMPLETE);
        return result;
    }

    protected void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        String suffix = getSuffixFromFilename(documentFilePath.getFileName().toString());
        logger.debug(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
        upload(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password, documentPool, documentKey, language, suffix, ackKey), asyncHandler);
    }

    private String getSuffixFromFilename(String filename) {
        if(!filename.contains("."))
            throw new MovilizerWebServiceException(String.format(MESSAGES.MISSING_FILE_EXTENSION, filename));
        String[] filenameSplit = filename.split("\\.");
        return filenameSplit[filenameSplit.length - 1];
    }

    private UploadResponse uploadSync(HttpEntity entity) {
        try {
            HttpResponse response = Request.Post(documentUploadAddress.toURI())
                    .addHeader("User-Agent", DefaultValues.USER_AGENT)
                    .body(entity)
                    .execute().returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if (!(HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)) {
                throw new MovilizerWebServiceException(
                        String.format(MESSAGES.FAILED_FILE_UPLOAD,
                                statusCode,
                                response.getStatusLine().getReasonPhrase()));
            }
            return new UploadResponse(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        } catch (IOException | URISyntaxException e) {
            logger.error(String.format(MESSAGES.UPLOAD_ERROR, e.getMessage()));
            throw new MovilizerWebServiceException(e);
        }
    }

    private void upload(HttpEntity entity, FutureCallback<UploadResponse> asyncHandler) {
        try {
            Async.newInstance().execute(Request.Post(documentUploadAddress.toURI())
                    .addHeader("User-Agent", DefaultValues.USER_AGENT)
                    .body(entity), new ResponseHandlerAdapter<UploadResponse>(asyncHandler) {
                        @Override
                        public UploadResponse convertHttpResponse(HttpResponse httpResponse) {
                            logger.info(MESSAGES.UPLOAD_COMPLETE);
                            return new UploadResponse(
                                    httpResponse.getStatusLine().getStatusCode(),
                                    httpResponse.getStatusLine().getReasonPhrase());
                        }
                    });
        } catch (URISyntaxException e) {
            logger.error(String.format(MESSAGES.UPLOAD_ERROR, e.getMessage()));
            asyncHandler.onFailure(new MovilizerWebServiceException(e));
        }
    }
}
