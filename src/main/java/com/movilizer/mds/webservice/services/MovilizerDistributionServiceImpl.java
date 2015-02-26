package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.exceptions.MovilizerXMLException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.Future;

class MovilizerDistributionServiceImpl implements MovilizerDistributionService {

    MovilizerWebService webService;
    MovilizerXMLParserService parserService;
    UploadFileService uploadFileService;

    public MovilizerDistributionServiceImpl(MovilizerWebService webService, MovilizerXMLParserService parserService, UploadFileService uploadFileService) {
        this.webService = webService;
        this.parserService = parserService;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request) {
        return webService.prepareUploadRequest(systemId, password, request);
    }

    @Override
    public MovilizerResponse getReplyFromCloud(MovilizerRequest request) throws MovilizerWebServiceException {
        return webService.getReplyFromCloud(request);
    }

    @Override
    public Response<MovilizerResponse> getReplyFromCloudAsync(MovilizerRequest request) throws MovilizerWebServiceException {
        return webService.getReplyFromCloudAsync(request);
    }

    @Override
    public Future<?> getReplyFromCloudAsync(MovilizerRequest request, AsyncHandler<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException {
        return webService.getReplyFromCloudAsync(request, asyncHandler);
    }

    @Override
    public void uploadDocument(InputStream documentInputStream, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException {
        uploadFileService.uploadDocument(documentInputStream, systemId, password, documentPool, documentKey, language, suffix, ackKey);
    }

    @Override
    public void uploadDocument(Path documentFilePath, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException {
        uploadDocument(documentFilePath, systemId, password, documentPool, documentKey, language, suffix, ackKey);
    }

    @Override
    public MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException {
        return parserService.getRequestFromFile(filePath);
    }

    @Override
    public String printRequest(MovilizerRequest request) throws MovilizerXMLException {
        return parserService.printRequest(request);
    }

    @Override
    public String printResponse(MovilizerResponse response) throws MovilizerXMLException {
        return parserService.printResponse(response);
    }

    @Override
    public void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException {
        saveRequestToFile(request, filePath);
    }
}
