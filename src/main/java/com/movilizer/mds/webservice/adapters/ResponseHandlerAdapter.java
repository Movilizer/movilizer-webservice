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

package com.movilizer.mds.webservice.adapters;

import com.movilizer.mds.webservice.exceptions.MovilizerWebServiceException;
import com.movilizer.mds.webservice.messages.MESSAGES;
import com.movilizer.mds.webservice.models.FutureCallback;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ResponseHandlerAdapter<T> implements ResponseHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandlerAdapter.class);

    private FutureCallback<T> futureCallback;

    public ResponseHandlerAdapter(FutureCallback<T> futureCallback) {
        this.futureCallback = futureCallback;
    }

    public abstract T convertHttpResponse(HttpResponse httpResponse);

    @Override
    public T handleResponse(HttpResponse httpResponse) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace(MESSAGES.HANDLING_HTTP_RESPONSE);
        }
        if (!wasSuccessful(httpResponse)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format(MESSAGES.FAILED_REQUEST_ERROR,
                        httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().getReasonPhrase()));
            }
            Exception e = new MovilizerWebServiceException(
                    String.format(MESSAGES.FAILED_REQUEST_ERROR,
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase()));
            futureCallback.onFailure(e);
            futureCallback.onComplete(null, e);
            return null;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(MESSAGES.SUCCESSFUL_HTTP_RESPONSE, httpResponse.getStatusLine().getStatusCode()));
            }
        }
        return convertHttpResponse(httpResponse);
    }

    private boolean wasSuccessful(HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return HttpStatus.SC_OK <= status && status < HttpStatus.SC_MULTIPLE_CHOICES;
    }
}
