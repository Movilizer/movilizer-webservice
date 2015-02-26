package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerWebServiceV12;
import com.movilitas.movilizer.v12.MovilizerWebServiceV12Service;
import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;

import java.net.URL;
import java.nio.charset.Charset;

public class MovilizerConfBuilder {
    private Charset outputEncoding = DefaultValues.OUTPUT_ENCODING;
    private EndPoint endpoint = EndPoint.DEMO;
    private URL endpointAddress;
    private URL documentUploadAddress;

    public MovilizerConfBuilder() {
    }

    public MovilizerDistributionService getService() {
        MovilizerWebServiceV12 movilizerCloud = new MovilizerWebServiceV12Service().getMovilizerWebServiceV12Soap11();
        MovilizerWebService webService = new MovilizerWebService(movilizerCloud);
        webService.setEndpoint(endpoint.getMdsUrl());
        MovilizerXMLParserService parserService = new MovilizerXMLParserService(outputEncoding);
        UploadFileService uploadFileService = new UploadFileService(endpoint.getUploadUrl(), new MovilizerUploadForm());

        if (endpointAddress != null && documentUploadAddress != null) {
            webService.setEndpoint(endpointAddress);
            uploadFileService.setDocumentUploadAddress(documentUploadAddress);
        }

        return new MovilizerDistributionServiceImpl(
                webService,
                parserService,
                uploadFileService
        );
    }

    public MovilizerConfBuilder setEndpoint(EndPoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public MovilizerConfBuilder setEndpoint(URL endpointAddress, URL documentUploadAddress) {
        this.endpointAddress = endpointAddress;
        this.documentUploadAddress = documentUploadAddress;
        return this;
    }

    public MovilizerConfBuilder setOutputEncoding(Charset outputEncoding) {
        this.outputEncoding = outputEncoding;
        return this;
    }
}
