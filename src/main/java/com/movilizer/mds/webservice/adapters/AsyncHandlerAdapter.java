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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;


public class AsyncHandlerAdapter<T> implements AsyncHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(AsyncHandlerAdapter.class);

    private CompletableFuture<T> future;

    public AsyncHandlerAdapter(CompletableFuture<T> future) {
        this.future = future;
    }

    @Override
    public void handleResponse(Response<T> response) {
        if (response.isCancelled()) {
            if (logger.isErrorEnabled()) {
                logger.error(Messages.WEB_RESPONSE_CANCELED);
            }
            Exception exception = new MovilizerWebServiceException(
                    Messages.REQUEST_CANCELLED_ERROR);
            future.completeExceptionally(exception);
            return;
        }
        if (response.isDone()) {
            try {
                if (logger.isTraceEnabled()) {
                    logger.trace(Messages.HANDLING_WEB_RESPONSE);
                }
                T resInstance = response.get();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format(Messages.SUCCESSFUL_WEB_RESPONSE,
                            resInstance.getClass().toString()));
                }
                future.complete(resInstance);
            } catch (ExecutionException | InterruptedException e) {
                future.completeExceptionally(e);
            }
        } else {
            if (logger.isErrorEnabled()) {
                logger.error(Messages.WEB_RESPONSE_NOT_DONE);
            }
        }
    }
}
