package com.movilizer.mds.webservice.models;

public interface FutureCallback<T> {
    void completed(T futureVar);

    void failed(Exception futureException);

    void cancelled();
}
