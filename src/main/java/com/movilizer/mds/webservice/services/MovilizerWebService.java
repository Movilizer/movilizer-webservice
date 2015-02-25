package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.*;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.EN;
import com.movilizer.mds.webservice.messages.MovilizerCloudMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.http.HTTPException;
import javax.xml.ws.soap.SOAPFaultException;

class MovilizerWebService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);
    private MovilizerWebServiceV12 movilizerCloud;

    protected MovilizerWebService(String webServiceAddress) {
        movilizerCloud = new MovilizerWebServiceV12Service().getMovilizerWebServiceV12Soap11();
        setEndpoint(this.movilizerCloud, webServiceAddress);
    }

    protected MovilizerWebService(MovilizerWebServiceV12 movilizerCloud, String webServiceAddress) {
        this.movilizerCloud = movilizerCloud;
        setEndpoint(this.movilizerCloud, webServiceAddress);
    }

    protected static void setEndpoint(MovilizerWebServiceV12 movilizerCloud, String webServiceAddress) {
        BindingProvider provider = (BindingProvider) movilizerCloud;
        provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceAddress);
    }

    protected MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request) {
        // Load system credentials
        request.setSystemId(systemId);
        request.setSystemPassword(password);
        // Set default values for single use mode
        request.setNumResponses(0);
        request.setUseAutoAcknowledge(false);
        request.setResponseSize(0);
        return request;
    }

    protected MovilizerResponse getReplyFromCloud(MovilizerRequest request) {
        MovilizerResponse response;
        try {
            response = movilizerCloud.movilizer(request);
        } catch (SOAPFaultException | HTTPException e) {
            logger.error(e.getMessage());
            throw new MovilizerWebServiceException(e);
        }
        logger.debug(String.format(EN.RESPONSE_RECEIVED, response.getSystemId()));
        return response;
    }

    protected boolean responseHasErrors(MovilizerResponse response) {
        if (!response.getDocumentError().isEmpty()) return true;
        if (!response.getMasterdataError().isEmpty()) return true;
        if (!response.getMoveletError().isEmpty()) return true;
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage message : response.getStatusMessage()) {
                if (MovilizerCloudMessages.fromType(message.getType()).isError()) { //System id failed to authenticate
                    return true;
                }
            }
        }
        return false;
    }

    protected String prettyPrintErrors(MovilizerResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(EN.RESPONSE_HAS_ERRORS).append("\n");
        if (!response.getDocumentError().isEmpty()) {
            sb.append(EN.DOCUMENT_ERRORS)
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
            sb.append(EN.MASTERDATA_ERRORS)
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
            sb.append(EN.MOVELET_ERRORS)
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
                    sb.append(EN.MESSAGES_ERROR)
                            .append("\n")
                            .append("  - ")
                            .append(cloudMessage.getMessage())
                            .append(EN.SYSTEMID_IN_MESSAGE)
                            .append("'")
                            .append(response.getSystemId())
                            .append("'");
                }
            }
        }
        return sb.toString();
    }
}
