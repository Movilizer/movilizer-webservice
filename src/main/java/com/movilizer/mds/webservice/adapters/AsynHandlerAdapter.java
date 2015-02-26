package com.movilizer.mds.webservice.adapters;

import com.movilizer.mds.webservice.models.FutureCallback;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;

public class AsynHandlerAdapter<T> implements AsyncHandler<T> {

    private FutureCallback<T> futureCallback;

    public AsynHandlerAdapter(FutureCallback<T> futureCallback) {
        this.futureCallback = futureCallback;
    }

    @Override
    public void handleResponse(Response<T> res) {
        if (res.isCancelled()) {
            futureCallback.cancelled();
            return;
        }
        if (res.isDone()) {
            try {
                futureCallback.completed(res.get());
            } catch (ExecutionException | InterruptedException e) {
                futureCallback.failed(e);
            }
        }
    }
}
