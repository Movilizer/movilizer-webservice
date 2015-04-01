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

import com.movilitas.movilizer.v12.*;
import com.movilizer.mds.webservice.adapters.AsyncHandlerAdapter;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.messages.MovilizerCloudMessages;
import com.movilizer.mds.webservice.models.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.http.HTTPException;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;

class MovilizerWebService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private MovilizerWebServiceV12 movilizerCloud;

    protected MovilizerWebService(MovilizerWebServiceV12 movilizerCloud) {
        this.movilizerCloud = movilizerCloud;
    }

    protected void setEndpoint(URL webServiceAddress) {
        BindingProvider provider = (BindingProvider) movilizerCloud;
        provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceAddress.toString());
    }

    protected MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(MESSAGES.PREPARE_UPLOAD_REQUEST, systemId));
        }
        // Load system credentials
        request.setSystemId(systemId);
        request.setSystemPassword(password);
        // Set default values for single use mode
        request.setNumResponses(0);
        request.setUseAutoAcknowledge(false);
        request.setResponseSize(0);
        return request;
    }

    protected MovilizerRequest prepareDownloadRequest(Long systemId, String password, Integer numResponses, MovilizerRequest request) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(MESSAGES.PREPARE_DOWNLOAD_REQUEST, systemId, numResponses));
        }
        // Load system credentials
        request.setSystemId(systemId);
        request.setSystemPassword(password);
        request.setNumResponses(numResponses);
        request.setUseAutoAcknowledge(true);
        return request;
    }

    protected MovilizerResponse getReplyFromCloudSync(MovilizerRequest request) {
        MovilizerResponse response;
        try {
            response = movilizerCloud.movilizer(request);
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format(MESSAGES.RESPONSE_RECEIVED, response.getSystemId()));
        }
        return response;
    }

    protected void getReplyFromCloud(MovilizerRequest request, FutureCallback<MovilizerResponse> asyncHandler) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(MESSAGES.PERFORMING_REQUEST, request.getSystemId()));
            }
            movilizerCloud.movilizerAsync(request, new AsyncHandlerAdapter<>(asyncHandler));
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        }
    }

    protected boolean responseHasErrors(MovilizerResponse response) {
        if (!response.getDocumentError().isEmpty()) return true;
        if (!response.getMasterdataError().isEmpty()) return true;
        if (!response.getMoveletError().isEmpty()) return true;
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage message : response.getStatusMessage()) {
                if (MovilizerCloudMessages.fromType(message.getType()).isError()) { //System id onFailure to authenticate
                    return true;
                }
            }
        }
        return false;
    }

    protected String prettyPrintErrors(MovilizerResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGES.RESPONSE_HAS_ERRORS).append("\n");
        if (!response.getDocumentError().isEmpty()) {
            sb.append(MESSAGES.DOCUMENT_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getDocumentError().size()))
                    .append(")\n");
            for (MovilizerDocumentError error : response.getDocumentError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getMasterdataError().isEmpty()) {
            sb.append(MESSAGES.MASTERDATA_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getMasterdataError().size()))
                    .append(")\n");
            for (MovilizerMasterdataError error : response.getMasterdataError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getMoveletError().isEmpty()) {
            sb.append(MESSAGES.MOVELET_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getMoveletError().size()))
                    .append(")\n");
            for (MovilizerMoveletError error : response.getMoveletError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage cloudMessage : response.getStatusMessage()) {
                if (MovilizerCloudMessages.isError(cloudMessage.getType())) {
                    sb.append(MESSAGES.MESSAGES_ERROR)
                            .append("\n")
                            .append("  - ")
                            .append(cloudMessage.getMessage())
                            .append(MESSAGES.SYSTEM_ID_IN_MESSAGE)
                            .append("'")
                            .append(response.getSystemId())
                            .append("'");
                }
            }
        }
        return sb.toString();
    }
}
