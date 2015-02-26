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

public interface MovilizerDistributionService {

    // Webservice interaction
    public MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request);
    public MovilizerResponse getReplyFromCloud(MovilizerRequest request) throws MovilizerWebServiceException;
    public Response<MovilizerResponse> getReplyFromCloudAsync(MovilizerRequest request) throws MovilizerWebServiceException;
    public Future<?> getReplyFromCloudAsync(MovilizerRequest request, AsyncHandler<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException;
    // Document upload
    void uploadDocument(InputStream documentInputStream, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException;
    void uploadDocument(Path documentFilePath, String systemId, String password, String documentPool, String documentKey, String language, String suffix, String ackKey) throws MovilizerWebServiceException;
    // XML utils
    public MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException;
    public String printRequest(MovilizerRequest request) throws MovilizerXMLException;
    public String printResponse(MovilizerResponse response) throws MovilizerXMLException;
    public void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException;
}
