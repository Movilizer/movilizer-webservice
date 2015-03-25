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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;

public class AsyncHandlerAdapter<T> implements AsyncHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(AsyncHandlerAdapter.class);

    private FutureCallback<T> futureCallback;

    public AsyncHandlerAdapter(FutureCallback<T> futureCallback) {
        this.futureCallback = futureCallback;
    }

    @Override
    public void handleResponse(Response<T> response) {
        if (response.isCancelled()) {
            logger.error(MESSAGES.WEB_RESPONSE_CANCELED);
            Exception e = new MovilizerWebServiceException(MESSAGES.REQUEST_CANCELLED_ERROR);
            futureCallback.onFailure(e);
            futureCallback.onComplete(null, e);
            return;
        }
        if (response.isDone()) {
            try {
                logger.trace(MESSAGES.HANDLING_WEB_RESPONSE);
                T resInstance = response.get();
                logger.debug(String.format(MESSAGES.SUCCESSFUL_WEB_RESPONSE, resInstance.getClass().toString()));
                futureCallback.onSuccess(resInstance);
                futureCallback.onComplete(resInstance, null);
            } catch (ExecutionException | InterruptedException e) {
                futureCallback.onFailure(e);
                futureCallback.onComplete(null, e);
            }
        } else {
            logger.error(MESSAGES.WEB_RESPONSE_NOT_DONE);
        }
    }
}
