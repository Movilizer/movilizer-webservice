package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.adapters.ResponseHandlerAdapter;
import com.movilizer.mds.webservice.defaults.DefaultValues;
import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.EN;
import com.movilizer.mds.webservice.models.FutureCallback;
import com.movilizer.mds.webservice.models.MovilizerUploadForm;
import com.movilizer.mds.webservice.models.UploadResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.Future;


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

    protected void uploadDocument(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        upload(movilizerUpload.getForm(documentInputStream, filename, systemId, password, documentPool, documentKey,language, getSuffixFromFilename(filename), ackKey));
    }

    protected Future<UploadResponse> uploadDocumentAsync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        return uploadAsync(movilizerUpload.getForm(documentInputStream, filename, systemId, password, documentPool, documentKey, language, getSuffixFromFilename(filename), ackKey));
    }

    protected void uploadDocumentAsync(InputStream documentInputStream, String filename, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        uploadAsync(movilizerUpload.getForm(documentInputStream, filename, systemId, password, documentPool, documentKey, language, getSuffixFromFilename(filename), ackKey), asyncHandler);
    }

    protected void uploadDocument(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        upload(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password, documentPool, documentKey, language, getSuffixFromFilename(documentFilePath.getFileName().toString()), ackKey));
    }

    protected Future<UploadResponse> uploadDocumentAsync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey) throws MovilizerWebServiceException {
        String suffix = getSuffixFromFilename(documentFilePath.getFileName().toString());
        return uploadAsync(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password, documentPool, documentKey, language, suffix, ackKey));
    }

    protected void uploadDocumentAsync(Path documentFilePath, long systemId, String password, String documentPool, String documentKey, String language, String ackKey, FutureCallback<UploadResponse> asyncHandler) throws MovilizerWebServiceException {
        String suffix = getSuffixFromFilename(documentFilePath.getFileName().toString());
        uploadAsync(movilizerUpload.getForm(documentFilePath.toFile(), systemId, password, documentPool, documentKey, language, suffix, ackKey), asyncHandler);
    }

    private String getSuffixFromFilename(String filename) {
        if(!filename.contains("."))
            throw new MovilizerWebServiceException(String.format(EN.MISSING_FILE_EXTENSION, filename));
        String[] filenameSplit = filename.split("\\.");
        return filenameSplit[filenameSplit.length - 1];
    }

    private void upload(HttpEntity entity) {
        try {
            HttpResponse response = Request.Post(documentUploadAddress.toURI())
                    .addHeader("User-Agent", DefaultValues.USER_AGENT)
                    .body(entity)
                    .execute().returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if (!(HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)) {
                throw new MovilizerWebServiceException(
                        String.format(EN.FAILE_FILE_UPLOAD,
                                statusCode,
                                response.getStatusLine().getReasonPhrase()));
            }
        } catch (IOException | URISyntaxException e) {
            throw new MovilizerWebServiceException(e);
        }
    }

    private Future<UploadResponse> uploadAsync(HttpEntity entity) {
        try {
            return Async.newInstance().execute(Request.Post(documentUploadAddress.toURI())
                    .addHeader("User-Agent", DefaultValues.USER_AGENT)
                    .body(entity),new ResponseHandler<UploadResponse>() {
                @Override
                public UploadResponse handleResponse(HttpResponse httpResponse) {
                    UploadResponse uploadResponse = new UploadResponse(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase());
                    if (!uploadResponse.wasSucceful()) {
                        throw new MovilizerWebServiceException(
                                String.format(EN.FAILE_FILE_UPLOAD,
                                        uploadResponse.getStatusCode(),
                                        uploadResponse.getMessage()));
                    }
                    return uploadResponse;
                }
            });
        } catch (URISyntaxException e) {
            throw new MovilizerWebServiceException(e);
        }
    }

    private void uploadAsync(HttpEntity entity,  FutureCallback<UploadResponse> asyncHandler) {
        try {
            Async.newInstance().execute(Request.Post(documentUploadAddress.toURI())
                    .addHeader("User-Agent", DefaultValues.USER_AGENT)
                    .body(entity), new ResponseHandlerAdapter<UploadResponse>(asyncHandler) {
                        @Override
                        public UploadResponse convertHttpResponse(HttpResponse httpResponse) {
                            return new UploadResponse(
                                    httpResponse.getStatusLine().getStatusCode(),
                                    httpResponse.getStatusLine().getReasonPhrase());
                        }
                    });
        } catch (URISyntaxException e) {
            asyncHandler.failed(new MovilizerWebServiceException(e));
        }
    }
}
