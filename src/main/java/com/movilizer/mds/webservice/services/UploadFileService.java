package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Path;


class UploadFileService {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerWebService.class);

    private String html5UploadAddress;

    protected UploadFileService(String html5UploadAddress) {
        this.html5UploadAddress = html5UploadAddress;
    }

    protected void uploadDocument(InputStream documentInputStream, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException {

    }

    protected void uploadDocument(Path documentFilePath, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException {

    }
}
