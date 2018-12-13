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
import com.movilizer.mds.webservice.messages.Messages;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public abstract class ResponseHandlerAdapter<T> implements ResponseHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandlerAdapter.class);

    private CompletableFuture<T> future;

    public ResponseHandlerAdapter(CompletableFuture<T> future) {
        this.future = future;
    }

    public abstract T convertHttpResponse(HttpResponse httpResponse);

    @Override
    public T handleResponse(HttpResponse httpResponse) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace(Messages.HANDLING_HTTP_RESPONSE);
        }
        if (!wasSuccessful(httpResponse)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format(Messages.FAILED_REQUEST_ERROR,
                        httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().getReasonPhrase()));
            }
            Exception exception = new MovilizerWebServiceException(
                    String.format(Messages.FAILED_REQUEST_ERROR,
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase()));
            future.completeExceptionally(exception);
            return null;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(Messages.SUCCESSFUL_HTTP_RESPONSE,
                        httpResponse.getStatusLine().getStatusCode()));
            }
        }
        T out = convertHttpResponse(httpResponse);
        future.complete(out);
        return out;
    }

    private boolean wasSuccessful(HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return HttpStatus.SC_OK <= status && status < HttpStatus.SC_MULTIPLE_CHOICES;
    }
}
