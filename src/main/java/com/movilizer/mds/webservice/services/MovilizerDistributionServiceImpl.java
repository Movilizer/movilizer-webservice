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

import com.movilitas.movilizer.v11.MovilizerRequest;
import com.movilitas.movilizer.v11.MovilizerResponse;
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
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PREPARE_UPLOAD_REQUEST, systemId));
    }
    return webService.prepareUploadRequest(systemId, password, request);
  }

  @Override
  public MovilizerRequest prepareDownloadRequest(Long systemId, String password, Integer numResponses, MovilizerRequest request) {
    return webService.prepareDownloadRequest(systemId, password, numResponses, request);
  }

  @Override
  public MovilizerResponse getReplyFromCloudSync(MovilizerRequest request) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
    }
    return webService.getReplyFromCloudSync(request);
  }

  @Override
  public void getReplyFromCloud(MovilizerRequest request, FutureCallback<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
    }
    webService.getReplyFromCloud(request, asyncHandler);
  }

  @Override
  public MovilizerResponse getReplyFromCloudSync(MovilizerRequest request, Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
    }
    return webService.getReplyFromCloudSync(request, connectionTimeoutInMillis, receiveTimeoutInMillis);
  }

  @Override
  public void getReplyFromCloud(MovilizerRequest request, Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis, FutureCallback<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
    }
    webService.getReplyFromCloud(request, connectionTimeoutInMillis, receiveTimeoutInMillis, asyncHandler);
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
  public UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    return uploadFileService.uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey);
  }

  @Override
  public void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    uploadFileService.uploadDocument(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, asyncHandler);
  }

  @Override
  public UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    return uploadFileService.uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey);
  }

  @Override
  public void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    uploadFileService.uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, asyncHandler);
  }

  @Override
  public UploadResponse uploadDocumentSync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    return uploadFileService.uploadDocumentSync(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
  }

  @Override
  public void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    uploadFileService.uploadDocument(documentInputStream, filename, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis, asyncHandler);
  }

  @Override
  public UploadResponse uploadDocumentSync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    return uploadFileService.uploadDocumentSync(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis);
  }

  @Override
  public void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, Integer connectionTimeoutInMillis, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.PERFORMING_UPLOAD, systemId));
    }
    uploadFileService.uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, ackKey, connectionTimeoutInMillis, asyncHandler);
  }

  @Override
  public <T> T getElementFromString(String elementString, Class<T> movilizerElementClass) throws MovilizerXMLException {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(MESSAGES.PARSING_XML, movilizerElementClass.getName()));
    }
    return parserService.getMovilizerElementFromString(elementString, movilizerElementClass);
  }

  @Override
  public <T> String printMovilizerElementToString(T movilizerElement, Class<T> movilizerElementClass) {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(MESSAGES.PRINTING_XML, movilizerElementClass.getName()));
    }
    return parserService.printMovilizerElementToString(movilizerElement, movilizerElementClass);
  }

  @Override
  public MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.READING_REQUEST_FROM_FILE, filePath.toAbsolutePath().toString()));
    }
    return parserService.getRequestFromFile(filePath);
  }

  @Override
  public MovilizerRequest getRequestFromString(String requestString) throws MovilizerXMLException {
    if (logger.isInfoEnabled()) {
      logger.info(MESSAGES.READING_REQUEST_FROM_STRING);
    }
    return parserService.getRequestFromString(requestString);
  }

  @Override
  public String requestToString(MovilizerRequest request) throws MovilizerXMLException {
    return parserService.printRequest(request);
  }

  @Override
  public String responseToString(MovilizerResponse response) throws MovilizerXMLException {
    return parserService.printResponse(response);
  }

  @Override
  public void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(MESSAGES.SAVING_REQUEST_TO_FILE, filePath.toAbsolutePath().toString()));
    }
    parserService.saveRequestToFile(request, filePath);
  }
}
