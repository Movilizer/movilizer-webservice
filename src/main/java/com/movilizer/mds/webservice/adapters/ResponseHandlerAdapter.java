package com.movilizer.mds.webservice.adapters;

import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.models.FutureCallback;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

public abstract class ResponseHandlerAdapter<T> implements ResponseHandler<T> {

    private FutureCallback<T> futureCallback;

    public ResponseHandlerAdapter(FutureCallback<T> futureCallback) {
        this.futureCallback = futureCallback;
    }

    public abstract T convertHttpResponse(HttpResponse httpResponse);

    @Override
    public T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        if (!wasSucceful(httpResponse)) {
            futureCallback.failed(new MovilizerWebServiceException(
                    String.format("Failed upload request with status '%d' with reason '%s'",
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase())));
            return null;
        }
        return convertHttpResponse(httpResponse);
    }

    private boolean wasSucceful(HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return HttpStatus.SC_OK <= status && status < HttpStatus.SC_MULTIPLE_CHOICES;
    }
}
