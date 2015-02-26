package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.exceptions.MovilizerXMLException;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.models.UploadResponse;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.Future;

public interface MovilizerDistributionService {

    // Webservice interaction
    public MovilizerRequest prepareUploadRequest(Long systemId, String password, MovilizerRequest request);
    public MovilizerResponse getReplyFromCloud(MovilizerRequest request) throws MovilizerWebServiceException;
    public Future<MovilizerResponse> getReplyFromCloudAsync(MovilizerRequest request) throws MovilizerWebServiceException;
    public void getReplyFromCloudAsync(MovilizerRequest request, FutureCallback<MovilizerResponse> asyncHandler) throws MovilizerWebServiceException;
    // Document upload
    void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;
    Future<UploadResponse> uploadDocumentAsync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;
    void uploadDocumentAsync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException;
    void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;
    Future<UploadResponse> uploadDocumentAsync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException;
    void uploadDocumentAsync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException;
    // XML utils
    public MovilizerRequest getRequestFromFile(Path filePath) throws MovilizerXMLException;
    public String printRequest(MovilizerRequest request) throws MovilizerXMLException;
    public String printResponse(MovilizerResponse response) throws MovilizerXMLException;
    public void saveRequestToFile(MovilizerRequest request, Path filePath) throws MovilizerXMLException;
}
