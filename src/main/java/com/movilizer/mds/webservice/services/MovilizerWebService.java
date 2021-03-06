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

import com.movilitas.movilizer.v16.*;
import com.movilizer.mds.webservice.adapters.AsyncHandlerAdapter;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.Messages;
import com.movilizer.mds.webservice.messages.MovilizerCloudMessages;
import com.movilizer.mds.webservice.models.PasswordHashTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.http.HTTPException;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;


class MovilizerWebService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private static final String CONNECTION_TIMEOUT_KEY = "javax.xml.ws.client.connectionTimeout";
    private static final String RECEIVE_TIMEOUT_KEY = "javax.xml.ws.client.receiveTimeout";
    private static final String THREAD_LOCAL_CONTEXT_KEY = "thread.local.request.context";
    private MovilizerWebServiceV16 movilizerCloud;
    private Integer defaultConnectionTimeoutInMillis;
    private Integer defaultReceiveTimeoutInMillis;
    private String userAgent;

    protected MovilizerWebService(MovilizerWebServiceV16 movilizerCloud,
                                  Integer defaultConnectionTimeoutInMillis,
                                  Integer defaultReceiveTimeoutInMillis, String agentId,
                                  String agentVersion) {
        this.movilizerCloud = movilizerCloud;
        this.defaultConnectionTimeoutInMillis = defaultConnectionTimeoutInMillis;
        this.defaultReceiveTimeoutInMillis = defaultReceiveTimeoutInMillis;
        this.userAgent = DefaultValues.userAgentFormatString(agentId, agentVersion);
        setUserAgentPropietaryApacheCXF();
        setTimeout(defaultConnectionTimeoutInMillis, defaultReceiveTimeoutInMillis);
        setWSClientToKeepSeparateContextPerThread();
    }

    /**
     * See Apache CXF explanation for adding headers:
     * http://cxf.apache.org/faq.html#FAQ-HowcanIaddsoapheaderstotherequest/response?
     */
    protected void setUserAgentPropietaryApacheCXF() {
//        try {
//            List<Header> headers = new ArrayList<>();
//            Header userAgentHeader = new Header(new QName("uri:com.movilizer.mds.webservice",
//                    "user-agent"), userAgent, new JAXBDataBinding(String.class));
//            headers.add(userAgentHeader);
//            ((BindingProvider) movilizerCloud).getRequestContext().put(Header.HEADER_LIST, headers);
//        } catch (JAXBException e) {
//            if (logger.isErrorEnabled()) {
//                logger.error(String.format(Messages.MARSHALLING_ERROR, userAgent), e);
//            }
//        }
    }

    protected void setTimeout(Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis) {
//        Client client = ClientProxy.getClient(movilizerCloud);
//        HTTPConduit http = (HTTPConduit) client.getConduit();
//
//        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
//        httpClientPolicy.setAllowChunking(false);
//        httpClientPolicy.setConnectionTimeout(connectionTimeoutInMillis);
//        httpClientPolicy.setReceiveTimeout(receiveTimeoutInMillis);
//
//        http.setClient(httpClientPolicy);

        //Set timeout until a connection is established
        ((BindingProvider) movilizerCloud).getRequestContext().put(CONNECTION_TIMEOUT_KEY,
                String.valueOf(connectionTimeoutInMillis));

        //Set timeout until the response is received
        ((BindingProvider) movilizerCloud).getRequestContext().put(RECEIVE_TIMEOUT_KEY,
                String.valueOf(receiveTimeoutInMillis));

    }

    /**
     * See Apache CXF explanation of thread safety:
     * http://cxf.apache.org/faq.html#FAQ-AreJAX-WSclientproxiesthreadsafe?
     */
    private void setWSClientToKeepSeparateContextPerThread() {
        ((BindingProvider) movilizerCloud).getRequestContext()
                .put(THREAD_LOCAL_CONTEXT_KEY, "true");
    }

    protected void setEndpoint(URL webServiceAddress) {
        ((BindingProvider) movilizerCloud).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceAddress.toString());
    }

    protected MovilizerRequest prepareUploadRequest(Long systemId, String password,
                                                    MovilizerRequest request) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.PREPARE_UPLOAD_REQUEST, systemId));
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

    protected MovilizerRequest prepareDownloadRequest(Long systemId, String password,
                                                      Integer numResponses,
                                                      MovilizerRequest request) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(Messages.PREPARE_DOWNLOAD_REQUEST, systemId, numResponses));
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
            logger.info(String.format(Messages.RESPONSE_RECEIVED, response.getSystemId()));
        }
        return response;
    }

    protected MovilizerResponse getReplyFromCloudSync(MovilizerRequest request,
                                                      Integer connectionTimeoutInMillis,
                                                      Integer receiveTimeoutInMillis) {
        MovilizerResponse response;
        try {
            setTimeout(connectionTimeoutInMillis, receiveTimeoutInMillis);
            response = movilizerCloud.movilizer(request);
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        } finally {
            setTimeout(defaultConnectionTimeoutInMillis, defaultReceiveTimeoutInMillis);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format(Messages.RESPONSE_RECEIVED, response.getSystemId()));
        }
        return response;
    }

    protected CompletableFuture<MovilizerResponse> getReplyFromCloud(MovilizerRequest request) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
            }
            CompletableFuture<MovilizerResponse> response = new CompletableFuture<>();
            movilizerCloud.movilizerAsync(request, new AsyncHandlerAdapter<>(response));
            return response;
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        }
    }

    protected CompletableFuture<MovilizerResponse> getReplyFromCloud(
            MovilizerRequest request, Integer connectionTimeoutInMillis,
            Integer receiveTimeoutInMillis) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(Messages.PERFORMING_REQUEST, request.getSystemId()));
            }
            CompletableFuture<MovilizerResponse> response = new CompletableFuture<>();
            setTimeout(connectionTimeoutInMillis, receiveTimeoutInMillis);
            movilizerCloud.movilizerAsync(request, new AsyncHandlerAdapter<>(response));
            return response;
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        } finally {
            setTimeout(defaultConnectionTimeoutInMillis, defaultReceiveTimeoutInMillis);
        }
    }

    protected boolean responseHasErrors(MovilizerResponse response) {
        if (!response.getDocumentError().isEmpty()) {
            return true;
        }
        if (!response.getMasterdataError().isEmpty()) {
            return true;
        }
        if (!response.getMoveletError().isEmpty()) {
            return true;
        }
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage message : response.getStatusMessage()) {
                if (MovilizerCloudMessages.fromType(message.getType()).isError()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String prettyPrintErrors(MovilizerResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.RESPONSE_HAS_ERRORS).append("\n");
        if (!response.getDocumentError().isEmpty()) {
            sb.append(Messages.DOCUMENT_ERRORS)
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
            sb.append(Messages.MASTERDATA_ERRORS)
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
            sb.append(Messages.MOVELET_ERRORS)
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
                    sb.append(Messages.MESSAGES_ERROR)
                            .append("\n")
                            .append("  - ")
                            .append(cloudMessage.getMessage())
                            .append(Messages.SYSTEM_ID_IN_MESSAGE)
                            .append("'")
                            .append(response.getSystemId())
                            .append("'");
                }
            }
        }
        return sb.toString();
    }

    protected void setParticipantPassword(Long systemId, String systemPassword,
                                          String deviceAddress, String newPassword,
                                          PasswordHashTypes type) {
        MovilizerResponse response;
        try {
            MovilizerParticipantConfiguration config = new MovilizerParticipantConfiguration();
            config.setDeviceAddress(deviceAddress);
            config.setPasswordHashType(type.getValue());
            config.setPasswordHashValue(digestPassword(newPassword, type));

            MovilizerRequest request = prepareUploadRequest(systemId, systemPassword,
                    new MovilizerRequest());
            request.getParticipantConfiguration().add(config);
            response = movilizerCloud.movilizer(request);
        } catch (SOAPFaultException | HTTPException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new MovilizerWebServiceException(e);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format(Messages.PASSWORD_SUCCESSFULY_CHANGED, deviceAddress,
                    response.getSystemId()));
        }
    }

    private String digestPassword(String password, PasswordHashTypes type) {
        //see: http://www.jasypt.org/api/jasypt/1.9.2/org/jasypt/digest/StandardStringDigester.html
        String hashedPassword;
        switch (type) {
            case PLAIN_TEXT:
                hashedPassword = password;
                break;
            case MD5:
                hashedPassword = "";
                break;
            case SHA_256:
                hashedPassword = "";
                break;
            case SHA_512:
            default:
                hashedPassword = "";
                break;
        }
        return hashedPassword;
    }
}
